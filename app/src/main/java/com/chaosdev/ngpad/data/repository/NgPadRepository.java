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
import com.chaosdev.ngpad.model.main.Section;
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
  private final Set<String> fetchedSections = new HashSet<>();
  private NgPad ngPad;
  private boolean isFetching = false;
  private final ExecutorService executorService;
  private final Handler mainHandler;

  private NgPadRepository(Context context) {
    ngPad = new NgPad();
    ngPadDao = AppDatabase.getInstance(context).ngPadDao();
    executorService = Executors.newFixedThreadPool(2);
    mainHandler = new Handler(Looper.getMainLooper());
    loadDataFromDatabase();
  }

  public static synchronized NgPadRepository getInstance(Context context) {
    if (instance == null) {
      instance = new NgPadRepository(context);
    }
    return instance;
  }

  private void loadDataFromDatabase() {
    executorService.execute(
        () -> {
          List<Category> categories = ngPadDao.getAllCategories();
          ngPad.getCategories().clear();
          ngPad.getCategories().addAll(categories);

          for (Category category : ngPad.getCategories()) {
            List<Course> courses = ngPadDao.getCoursesForCategory(category.getId());
            category.getCourses().clear();
            category.getCourses().addAll(courses);
          }
          mainHandler.post(() -> {});
        });
  }

  public void fetchNgPadData(final NgPadCallback callback) {
    if (isFetching) {
      mainHandler.post(() -> callback.onError("Data fetch already in progress"));
      return;
    }
    isFetching = true;

    // Check if categories exist in Room
    executorService.execute(
        () -> {
          List<Category> categoriesFromDb = ngPadDao.getAllCategories();
          if (!categoriesFromDb.isEmpty()) {
            ngPad.getCategories().clear();
            ngPad.getCategories().addAll(categoriesFromDb);
            for (Category category : categoriesFromDb) {
              List<Course> courses = ngPadDao.getCoursesForCategory(category.getId());
              category.getCourses().clear();
              category.getCourses().addAll(courses);
            }
            mainHandler.post(() -> callback.onCategoriesFetched(ngPad));
            isFetching = false;
          } else {
            // Fetch from API if not in Room
            apiService
                .getCategories()
                .enqueue(
                    new Callback<List<Category>>() {
                      @Override
                      public void onResponse(
                          Call<List<Category>> call, Response<List<Category>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                          executorService.execute(
                              () -> {
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
                          mainHandler.post(
                              () ->
                                  callback.onError(
                                      "Failed to fetch categories: " + response.code()));
                        }
                        isFetching = false;
                      }

                      @Override
                      public void onFailure(Call<List<Category>> call, Throwable t) {
                        mainHandler.post(
                            () ->
                                callback.onError("Failed to fetch categories: " + t.getMessage()));
                        isFetching = false;
                      }
                    });
          }
        });
  }

  private void fetchCoursesForCategory(final Category category, final NgPadCallback callback) {
    // Check if courses exist in Room
    executorService.execute(
        () -> {
          List<Course> coursesFromDb = ngPadDao.getCoursesForCategory(category.getId());
          if (!coursesFromDb.isEmpty()) {
            category.getCourses().clear();
            category.getCourses().addAll(coursesFromDb);
            mainHandler.post(() -> callback.onCoursesFetched(category));
          } else {
            // Fetch from API if not in Room
            apiService
                .getCoursesByCategory(category.getSlug())
                .enqueue(
                    new Callback<List<Course>>() {
                      @Override
                      public void onResponse(
                          Call<List<Course>> call, Response<List<Course>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                          executorService.execute(
                              () -> {
                                List<Course> courses = response.body();
                                Log.d("courses Api respons:", response.body().get(0).toString());
                                for (Course course : courses)
                                  Log.d(
                                      "courses Api respons:",
                                      String.valueOf(course)
                                          + " : "
                                          + String.valueOf(course.isNested())
                                          + " : "
                                          + course.getTitle());

                                for (Course course : courses) {
                                  Log.d("courses Api respons:", course.toString());

                                  course.setCategoryId(category.getId());

                                  if (!category.getCourses().contains(course)) {
                                    category.getCourses().add(course);
                                  }
                                }
                                ngPadDao.insertCourses(courses);
                                mainHandler.post(() -> callback.onCoursesFetched(category));
                              });
                        } else {
                          mainHandler.post(
                              () ->
                                  callback.onError(
                                      "Failed to fetch courses for category "
                                          + category.getName()
                                          + ": "
                                          + response.code()));
                        }
                      }

                      @Override
                      public void onFailure(Call<List<Course>> call, Throwable t) {
                        mainHandler.post(
                            () -> callback.onError("Failed to fetch courses: " + t.getMessage()));
                      }
                    });
          }
        });
  }

  public void fetchLessonsForCourse(final Course course, final NgPadCallback callback) {
    // First, check if data exists in Room
    executorService.execute(
        () -> {
          List<Lesson> lessonsFromDb = loadLessonsForCourse(course.getId(), course.isNested());
          List<Section> sectionsFromDb =
              course.isNested() ? ngPadDao.getSectionsForCourse(course.getId()) : new ArrayList<>();

          // If we have data in Room, use it
          if ((!lessonsFromDb.isEmpty() && !course.isNested())
              || (!sectionsFromDb.isEmpty() && course.isNested())) {
            course.getLessons().clear();
            course.getLessons().addAll(lessonsFromDb);
            if (course.isNested()) {
              course.getSections().clear();
              course.getSections().addAll(sectionsFromDb);
            }
            mainHandler.post(() -> callback.onLessonsFetched(course));
            return;
          }

          // If not in Room, fetch from API
          if (course.isNested()) {
            apiService
                .getSectionsByCourse(course.getId())
                .enqueue(
                    new Callback<List<Section>>() {
                      @Override
                      public void onResponse(
                          Call<List<Section>> call, Response<List<Section>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                          executorService.execute(
                              () -> {
                                List<Section> sections = response.body();
                                for (Section section : sections) {
                                  section.setCourseId(course.getId());
                                }
                                course.getSections().clear();
                                course.getSections().addAll(sections);
                                ngPadDao.insertSections(sections);

                                // Fetch lessons for each section
                                fetchLessonsForSections(course, sections, callback);
                              });
                        } else {
                          mainHandler.post(
                              () ->
                                  callback.onError(
                                      "Failed to fetch sections for course "
                                          + course.getTitle()
                                          + ": "
                                          + response.code()));
                        }
                      }

                      @Override
                      public void onFailure(Call<List<Section>> call, Throwable t) {
                        mainHandler.post(
                            () -> callback.onError("Failed to fetch sections: " + t.getMessage()));
                      }
                    });
          } else {
            apiService
                .getLessonsByCourse(course.getId())
                .enqueue(
                    new Callback<List<Lesson>>() {
                      @Override
                      public void onResponse(
                          Call<List<Lesson>> call, Response<List<Lesson>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                          executorService.execute(
                              () -> {
                                List<Lesson> lessons = response.body();
                                for (Lesson lesson : lessons) {
                                  lesson.setCourseId(course.getId());
                                  lesson.setSectionId(null);
                                }
                                course.getLessons().clear();
                                course.getLessons().addAll(lessons);
                                ngPadDao.insertLessons(lessons);
                                fetchedCourses.add(String.valueOf(course.getId()));
                                Log.d("course count :", course.getTitle());
                                Log.d("course count :", "" + course.getLessons().size());
                                mainHandler.post(() -> callback.onLessonsFetched(course));
                              });
                        } else {
                          mainHandler.post(
                              () ->
                                  callback.onError(
                                      "Failed to fetch lessons for course "
                                          + course.getTitle()
                                          + ": "
                                          + response.code()));
                        }
                      }

                      @Override
                      public void onFailure(Call<List<Lesson>> call, Throwable t) {
                        mainHandler.post(
                            () -> callback.onError("Failed to fetch lessons: " + t.getMessage()));
                      }
                    });
          }
        });
  }

  private void fetchLessonsForSections(
      final Course course, final List<Section> sections, final NgPadCallback callback) {
    if (sections.isEmpty()) {
      fetchedCourses.add(String.valueOf(course.getId()));
      mainHandler.post(() -> callback.onLessonsFetched(course));
      return;
    }

    final int[] sectionsProcessed = {0};
    for (Section section : sections) {
      if (fetchedSections.contains(section.getSlug())) {
        sectionsProcessed[0]++;
        if (sectionsProcessed[0] == sections.size()) {
          fetchedCourses.add(String.valueOf(course.getId()));
          mainHandler.post(() -> callback.onLessonsFetched(course));
        }
        continue;
      }

      apiService
          .getLessonsBySection(section.getSlug())
          .enqueue(
              new Callback<List<Lesson>>() {
                @Override
                public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                  if (response.isSuccessful() && response.body() != null) {
                    executorService.execute(
                        () -> {
                          List<Lesson> lessons = response.body();
                          for (Lesson lesson : lessons) {
                            lesson.setCourseId(course.getId());
                            lesson.setSectionId(section.getSlug());
                          }
                          course.getLessons().addAll(lessons);
                          ngPadDao.insertLessons(lessons);
                          fetchedSections.add(section.getSlug());

                          sectionsProcessed[0]++;
                          if (sectionsProcessed[0] == sections.size()) {
                            fetchedCourses.add(String.valueOf(course.getId()));

                            mainHandler.post(() -> callback.onLessonsFetched(course));
                          }
                        });
                  } else {
                    mainHandler.post(
                        () ->
                            callback.onError(
                                "Failed to fetch lessons for section "
                                    + section.getTitle()
                                    + ": "
                                    + response.code()));
                  }
                }

                @Override
                public void onFailure(Call<List<Lesson>> call, Throwable t) {
                  mainHandler.post(
                      () ->
                          callback.onError(
                              "Failed to fetch lessons for section: " + t.getMessage()));
                }
              });
    }
  }

  public void fetchLessonsForSection(
      final String sectionId,
      final String sectionSlug,
      final int courseId,
      final SectionLessonsCallback callback) {
    executorService.execute(
        () -> {
          List<Lesson> lessonsFromDb = ngPadDao.getLessonsBySectionId(sectionId);

          if (!lessonsFromDb.isEmpty()) {
            mainHandler.post(() -> callback.onLessonsFetched(lessonsFromDb));
            return;
          }

          apiService
              .getLessonsBySection(sectionSlug)
              .enqueue(
                  new Callback<List<Lesson>>() {
                    @Override
                    public void onResponse(
                        Call<List<Lesson>> call, Response<List<Lesson>> response) {
                      if (response.isSuccessful() && response.body() != null) {
                        executorService.execute(
                            () -> {
                              List<Lesson> lessons = response.body();
                              for (Lesson lesson : lessons) {
                                lesson.setCourseId(courseId);
                                lesson.setSectionId(sectionSlug);
                              }
                              ngPadDao.insertLessons(lessons);
                              fetchedSections.add(String.valueOf(sectionId));
                              mainHandler.post(() -> callback.onLessonsFetched(lessons));
                            });
                      } else {
                        mainHandler.post(
                            () ->
                                callback.onError(
                                    "Failed to fetch lessons for section: " + response.code()));
                      }
                    }

                    @Override
                    public void onFailure(Call<List<Lesson>> call, Throwable t) {
                      mainHandler.post(
                          () ->
                              callback.onError(
                                  "Failed to fetch lessons for section: " + t.getMessage()));
                    }
                  });
        });
  }

  private List<Lesson> loadLessonsForCourse(int courseId, boolean isNested) {
    List<Lesson> lessons = new ArrayList<>();
    if (isNested) {
      List<Section> sections = ngPadDao.getSectionsForCourse(courseId);
      for (Section section : sections) {
        List<Lesson> sectionLessons = ngPadDao.getLessonsForSection(courseId, section.getSlug());
        lessons.addAll(sectionLessons);
      }
    } else {
      lessons.addAll(ngPadDao.getDirectLessonsForCourse(courseId));
    }
    return lessons;
  }

  public NgPad getNgPad() {
    return ngPad;
  }

  public void getCourseById(int courseId, CourseCallback callback) {
    executorService.execute(
        () -> {
          Course course = ngPadDao.getCourseById(courseId);
          if (course != null) {
            course.getLessons().clear();
          }
          mainHandler.post(() -> callback.onCourseFetched(course));
        });
  }

  public void getLessonByID(int lessonsId, LessonCallback callback) {
    executorService.execute(
        () -> {
          Lesson lesson = ngPadDao.getLessonsByLessonId(lessonsId);

          mainHandler.post(() -> callback.onLessonFetched(lesson));
        });
  }

  public interface NgPadCallback {
    void onCategoriesFetched(NgPad ngPad);

    void onCoursesFetched(Category category);

    void onLessonsFetched(Course course);

    void onError(String message);
  }

  public interface CourseCallback {
    void onCourseFetched(Course course);
  }

  public interface SectionLessonsCallback {
    void onLessonsFetched(List<Lesson> lessons);

    void onError(String message);
  }

  public interface LessonCallback {
    void onLessonFetched(Lesson lesson);
  }
}
