package com.chaosdev.ngpad.data.datasource.remote;

import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;

import com.chaosdev.ngpad.model.main.Section;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
  @GET("tutorials/Angular M/categories/")
  Call<List<Category>> getCategories();

  @GET("categories/{slug}/courses/")
  Call<List<Course>> getCoursesByCategory(@Path("slug") String slug);

  @GET("courses/{id}/lessons/")
  Call<List<Lesson>> getLessonsByCourse(@Path("id") int id);

  @GET("courses/{id}/sections/")
  Call<List<Section>> getSectionsByCourse(@Path("id") int id);

  @GET("sections/{slug}/lessons/")
  Call<List<Lesson>> getLessonsBySection(@Path("slug") String slug);

  @GET("lessons/by_parent/")
  Call<List<Lesson>> getLessonsByParent(
      @Query("content_type") String contentType, @Query("object_id") String objectId);
}
