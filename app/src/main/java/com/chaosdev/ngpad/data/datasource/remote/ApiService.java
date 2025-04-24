package com.chaosdev.ngpad.data.datasource.remote;


import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;

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

    @GET("lessons/by_parent/")
    Call<List<Lesson>> getLessonsByParent(
        @Query("content_type") String contentType,
        @Query("object_id") String objectId
    );
}