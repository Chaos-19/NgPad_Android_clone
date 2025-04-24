package com.chaosdev.ngpad.data.repository;

import android.util.Log;
import com.chaosdev.ngpad.data.datasource.remote.ApiService;
import com.chaosdev.ngpad.data.datasource.remote.RetrofitClient;
import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.main.NgPad;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NgPadRepository {
  private final ApiService apiService = RetrofitClient.getApiService();
  private boolean isFetching = false;
  private final Set<String> fetchedCategories = new HashSet<>(); // Track fetched category slugs
  private final Set<String> fetchedCourses = new HashSet<>(); // Track fetched course IDs

  public void fetchNgPadData(final NgPadCallback callback) {
    if (isFetching) {
      callback.onError("Data fetch already in progress");
      return;
    }
    isFetching = true;

    final NgPad ngPad = new NgPad();
    apiService
        .getCategories()
        .enqueue(
            new Callback<List<Category>>() {
              @Override
              public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                  ngPad.getCategories().addAll(response.body());
                  callback.onCategoriesFetched(ngPad);

                  for (Category category : ngPad.getCategories()) {
                    if (!fetchedCategories.contains(category.getSlug())) {
                      fetchedCategories.add(category.getSlug());
                      fetchCoursesForCategory(category, callback);
                    }
                  }
                } else {
                  callback.onError("Failed to fetch categories: " + response.code());
                }
                isFetching = false;
              }

              @Override
              public void onFailure(Call<List<Category>> call, Throwable t) {
                callback.onError("Failed to fetch categories: " + t.getMessage());
                isFetching = false;
              }
            });
  }

  private void fetchCoursesForCategory(final Category category, final NgPadCallback callback) {
    apiService
        .getCoursesByCategory(category.getSlug())
        .enqueue(
            new Callback<List<Course>>() {
              @Override
              public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful() && response.body() != null) {
                  // Clear the existing courses to prevent duplicates
                  category.getCourses().clear();

                  category.getCourses().addAll(response.body());
                  callback.onCoursesFetched(category);

                  for (Course course : category.getCourses()) {

                    fetchCourseLessons(course, callback);
                  }
                } else {
                  callback.onError(
                      "Failed to fetch courses for category "
                          + category.getName()
                          + ": "
                          + response.code());
                }
              }

              @Override
              public void onFailure(Call<List<Course>> call, Throwable t) {
                callback.onError("Failed to fetch courses: " + t.getMessage());
              }
            });
  }

  private void fetchCourseLessons(final Course course, final NgPadCallback callback) {
    apiService
        .getLessonsByParent("course", String.valueOf(course.getId()))
        .enqueue(
            new Callback<List<Lesson>>() {
              @Override
              public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                if (response.isSuccessful() && response.body() != null) {
                  course.getLessons().addAll(response.body());
                  callback.onLessonsFetched(course);
                  fetchNestedLessons(course.getLessons(), callback);
                } else {
                  callback.onError(
                      "Failed to fetch lessons for course "
                          + course.getTitle()
                          + ": "
                          + response.code());
                }
              }

              @Override
              public void onFailure(Call<List<Lesson>> call, Throwable t) {
                callback.onError("Failed to fetch lessons: " + t.getMessage());
              }
            });
  }

  private void fetchNestedLessons(List<Lesson> parentLessons, final NgPadCallback callback) {
    for (Lesson lesson : parentLessons) {
      if (lesson.isContainer()) {
        apiService
            .getLessonsByParent("lesson", lesson.getId())
            .enqueue(
                new Callback<List<Lesson>>() {
                  @Override
                  public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                      lesson.getSubLessons().addAll(response.body());
                      callback.onSubLessonsFetched(lesson);
                      fetchNestedLessons(lesson.getSubLessons(), callback);
                    } else {
                      callback.onError(
                          "Failed to fetch sub-lessons for lesson "
                              + lesson.getTitle()
                              + ": "
                              + response.code());
                    }
                  }

                  @Override
                  public void onFailure(Call<List<Lesson>> call, Throwable t) {
                    callback.onError("Failed to fetch sub-lessons: " + t.getMessage());
                  }
                });
      }
    }
  }

  public interface NgPadCallback {
    void onCategoriesFetched(NgPad ngPad);

    void onCoursesFetched(Category category);

    void onLessonsFetched(Course course);

    void onSubLessonsFetched(Lesson lesson);

    void onError(String message);
  }
}
/*
import android.widget.Toast;
import com.chaosdev.ngpad.data.datasource.remote.ApiService;
import com.chaosdev.ngpad.data.datasource.remote.RetrofitClient;
import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.main.NgPad;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NgPadRepository {
    private final ApiService apiService = RetrofitClient.getApiService();
    private boolean isFetching = false;
    private int pendingRequests = 0;

    public void fetchNgPadData(final NgPadCallback callback) {
        final NgPad ngPad = new NgPad();

        if (isFetching) {
            callback.onError("Data fetch already in progress");
            return;
        }
        isFetching = true;

        pendingRequests++;
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    ngPad.getCategories().addAll(response.body());
                    callback.onCategoriesFetched(ngPad);

                    for (Category category : ngPad.getCategories()) {
                        fetchCoursesForCategory(category, callback);
                    }
                } else {
                    callback.onError("Failed to fetch categories");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private void fetchCoursesForCategory(final Category category, final NgPadCallback callback) {
        apiService.getCoursesByCategory(category.getSlug()).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    category.getCourses().addAll(response.body());
                    callback.onCoursesFetched(category);

                    for (Course course : category.getCourses()) {
                        fetchCourseLessons(course, callback);
                    }
                } else {
                    callback.onError("Failed to fetch courses");
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private void fetchCourseLessons(final Course course, final NgPadCallback callback) {
        apiService.getLessonsByParent("course", String.valueOf(course.getId()))
            .enqueue(new Callback<List<Lesson>>() {
                @Override
                public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                    if (response.isSuccessful()) {
                        course.getLessons().addAll(response.body());
                        callback.onLessonsFetched(course);
                        fetchNestedLessons(course.getLessons(), callback);
                    } else {
                        callback.onError("Failed to fetch lessons");
                    }
                }

                @Override
                public void onFailure(Call<List<Lesson>> call, Throwable t) {
                    callback.onError(t.getMessage());
                }
            });
    }

    private void fetchNestedLessons(List<Lesson> parentLessons, final NgPadCallback callback) {
        for (Lesson lesson : parentLessons) {
            if (lesson.isContainer()) {
                apiService.getLessonsByParent("lesson", lesson.getId())
                    .enqueue(new Callback<List<Lesson>>() {
                        @Override
                        public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                            if (response.isSuccessful()) {
                                lesson.getSubLessons().addAll(response.body());
                                callback.onSubLessonsFetched(lesson);
                                // Recursively fetch deeper levels
                                fetchNestedLessons(lesson.getSubLessons(), callback);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Lesson>> call, Throwable t) {
                            callback.onError("Failed to fetch sublessons");
                        }
                    });
            }
        }
    }

    public interface NgPadCallback {
        void onCategoriesFetched(NgPad ngPad);
        void onCoursesFetched(Category category);
        void onLessonsFetched(Course course);
        void onSubLessonsFetched(Lesson lesson);
        void onError(String message);
    }
}
*/
