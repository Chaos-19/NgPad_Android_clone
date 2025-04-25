package com.chaosdev.ngpad.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;

import java.util.List;

@Dao
public interface NgPadDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertCategories(List<Category> categories);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertCourses(List<Course> courses);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertLessons(List<Lesson> lessons);

  @Query("SELECT * FROM categories")
  List<Category> getAllCategories();

  @Query("SELECT * FROM courses WHERE categoryId = :categoryId")
  List<Course> getCoursesForCategory(int categoryId);

  @Query("SELECT * FROM lessons WHERE courseId = :courseId AND parentLessonId IS NULL")
  List<Lesson> getTopLevelLessonsForCourse(int courseId);

  @Query("SELECT * FROM lessons WHERE parentLessonId = :parentLessonId")
  List<Lesson> getSubLessonsForLesson(int parentLessonId);

  @Query("SELECT * FROM courses WHERE id = :courseId")
  Course getCourseById(int courseId);

  @Query("DELETE FROM categories")
  void clearCategories();

  @Query("DELETE FROM courses")
  void clearCourses();

  @Query("DELETE FROM lessons")
  void clearLessons();

  @Transaction
  default void clearAllData() {
    clearCategories();
    clearCourses();
    clearLessons();
  }
}
