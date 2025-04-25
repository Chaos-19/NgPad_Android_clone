package com.chaosdev.ngpad.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.chaosdev.ngpad.data.database.AppDatabase;
import com.chaosdev.ngpad.data.database.NgPadDao;
import com.chaosdev.ngpad.data.datasource.remote.ApiService;
import com.chaosdev.ngpad.data.datasource.remote.RetrofitClient;
import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.main.NgPad;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NgPadRepository {
    private static final String TAG = "NgPadRepository";
    private static NgPadRepository instance;
    private final ApiService apiService = RetrofitClient.getApiService();
    private final NgPadDao ngPadDao;
    private final Set<String> fetchedCategories = new HashSet<>();
    private final Set<String> fetchedCourses = new HashSet<>();
    private NgPad ngPad;
    private boolean isFetching = false;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    private NgPadRepository(Context context) {
        ngPad = new NgPad();
        ngPadDao = AppDatabase.getInstance(context).ngPadDao();
        executorService = Executors.newFixedThreadPool(2); // Background thread pool
        mainHandler = new Handler(Looper.getMainLooper()); // For posting to main thread
        loadDataFromDatabase();
    }

    public static synchronized NgPadRepository getInstance(Context context) {
        if (instance == null) {
            instance = new NgPadRepository(context);
        }
        return instance;
    }

    private void loadDataFromDatabase() {
        executorService.execute(() -> {
            List<Category> categories = ngPadDao.getAllCategories();
            ngPad.getCategories().clear();
            ngPad.getCategories().addAll(categories);

            for (Category category : ngPad.getCategories()) {
                List<Course> courses = ngPadDao.getCoursesForCategory(category.getId());
                category.getCourses().clear();
                category.getCourses().addAll(courses);

                for (Course course : category.getCourses()) {
                    List<Lesson> lessons = loadLessonsForCourse(course.getId());
                    course.getLessons().clear();
                    course.getLessons().addAll(lessons);
                }
            }
            // Notify main thread that data is loaded
            mainHandler.post(() -> {
                // This callback can be used to notify ViewModel or UI
            });
        });
    }

    private List<Lesson> loadLessonsForCourse(int courseId) {
        List<Lesson> lessons = new ArrayList<>();
        List<Lesson> topLevelLessons = ngPadDao.getTopLevelLessonsForCourse(courseId);
        for (Lesson lesson : topLevelLessons) {
            lessons.add(lesson);
            if (lesson.isContainer()) {
                lesson.getSubLessons().addAll(loadSubLessons(lesson.getId()));
            }
        }
        return lessons;
    }

    private List<Lesson> loadSubLessons(int parentLessonId) {
        List<Lesson> subLessons = ngPadDao.getSubLessonsForLesson(parentLessonId);
        for (Lesson subLesson : subLessons) {
            if (subLesson.isContainer()) {
                subLesson.getSubLessons().addAll(loadSubLessons(subLesson.getId()));
            }
        }
        return subLessons;
    }

    public void fetchNgPadData(final NgPadCallback callback) {
        if (isFetching) {
            mainHandler.post(() -> callback.onError("Data fetch already in progress"));
            return;
        }
        isFetching = true;

        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executorService.execute(() -> {
                        ngPadDao.clearAllData();
                        ngPad.getCategories().clear();

                        List<Category> categories = response.body();
                        ngPadDao.insertCategories(categories);
                        ngPad.getCategories().addAll(categories);

                        mainHandler.post(() -> callback.onCategoriesFetched(ngPad));

                        for (Category category : ngPad.getCategories()) {
                            if (!fetchedCategories.contains(category.getSlug())) {
                                fetchedCategories.add(category.getSlug());
                                fetchCoursesForCategory(category, callback);
                            }
                        }
                    });
                } else {
                    mainHandler.post(() -> callback.onError("Failed to fetch categories: " + response.code()));
                }
                isFetching = false;
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                mainHandler.post(() -> callback.onError("Failed to fetch categories: " + t.getMessage()));
                isFetching = false;
            }
        });
    }

    private void fetchCoursesForCategory(final Category category, final NgPadCallback callback) {
        apiService.getCoursesByCategory(category.getSlug()).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executorService.execute(() -> {
                        List<Course> courses = response.body();
                        for (Course course : courses) {
                            course.setCategoryId(category.getId());
                            if (!category.getCourses().contains(course)) {
                                category.getCourses().add(course);
                            }
                        }
                        ngPadDao.insertCourses(courses);
                        mainHandler.post(() -> callback.onCoursesFetched(category));

                        for (Course course : category.getCourses()) {
                            if (!fetchedCourses.contains(String.valueOf(course.getId()))) {
                                fetchedCourses.add(String.valueOf(course.getId()));
                                fetchCourseLessons(course, callback);
                            }
                        }
                    });
                } else {
                    mainHandler.post(() -> callback.onError("Failed to fetch courses for category " + category.getName() + ": " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                mainHandler.post(() -> callback.onError("Failed to fetch courses: " + t.getMessage()));
            }
        });
    }

    private void fetchCourseLessons(final Course course, final NgPadCallback callback) {
        apiService.getLessonsByParent("course", String.valueOf(course.getId()))
                .enqueue(new Callback<List<Lesson>>() {
                    @Override
                    public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            executorService.execute(() -> {
                                List<Lesson> lessons = response.body();
                                for (Lesson lesson : lessons) {
                                    lesson.setCourseId(course.getId());
                                    lesson.setParentLessonId(null);
                                }
                                course.getLessons().addAll(lessons);
                                ngPadDao.insertLessons(lessons);
                                mainHandler.post(() -> callback.onLessonsFetched(course));
                                fetchNestedLessons(course.getLessons(), callback);
                            });
                        } else {
                            mainHandler.post(() -> callback.onError("Failed to fetch lessons for course " + course.getTitle() + ": " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Lesson>> call, Throwable t) {
                        mainHandler.post(() -> callback.onError("Failed to fetch lessons: " + t.getMessage()));
                    }
                });
    }

    private void fetchNestedLessons(List<Lesson> parentLessons, final NgPadCallback callback) {
        for (Lesson lesson : parentLessons) {
            if (lesson.isContainer()) {
                apiService.getLessonsByParent("lesson", String.valueOf(lesson.getId()))
                        .enqueue(new Callback<List<Lesson>>() {
                            @Override
                            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    executorService.execute(() -> {
                                        List<Lesson> subLessons = response.body();
                                        for (Lesson subLesson : subLessons) {
                                            subLesson.setCourseId(lesson.getCourseId());
                                            subLesson.setParentLessonId(lesson.getId());
                                        }
                                        lesson.getSubLessons().addAll(subLessons);
                                        ngPadDao.insertLessons(subLessons);
                                        mainHandler.post(() -> callback.onSubLessonsFetched(lesson));
                                        fetchNestedLessons(lesson.getSubLessons(), callback);
                                    });
                                } else {
                                    mainHandler.post(() -> callback.onError("Failed to fetch sub-lessons for lesson " + lesson.getTitle() + ": " + response.code()));
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Lesson>> call, Throwable t) {
                                mainHandler.post(() -> callback.onError("Failed to fetch sub-lessons: " + t.getMessage()));
                            }
                        });
            }
        }
    }

    public NgPad getNgPad() {
        return ngPad;
    }

    public void getCourseById(int courseId, CourseCallback callback) {
        executorService.execute(() -> {
            Course course = ngPadDao.getCourseById(courseId);
            if (course != null) {
                course.getLessons().addAll(loadLessonsForCourse(courseId));
            }
            mainHandler.post(() -> callback.onCourseFetched(course));
        });
    }

    public interface NgPadCallback {
        void onCategoriesFetched(NgPad ngPad);
        void onCoursesFetched(Category category);
        void onLessonsFetched(Course course);
        void onSubLessonsFetched(Lesson lesson);
        void onError(String message);
    }

    public interface CourseCallback {
        void onCourseFetched(Course course);
    }
}