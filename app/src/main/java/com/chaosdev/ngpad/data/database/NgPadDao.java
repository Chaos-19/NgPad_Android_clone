package com.chaosdev.ngpad.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.SvgCacheEntry;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.main.Section;
import java.util.List;

@Dao
public interface NgPadDao {
    // Category methods
    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategories(List<Category> categories);

    // Course methods
    @Query("SELECT * FROM courses WHERE categoryId = :categoryId")
    List<Course> getCoursesForCategory(int categoryId);

    @Query("SELECT * FROM courses WHERE id = :courseId")
    Course getCourseById(int courseId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourses(List<Course> courses);

    // Section methods
    @Query("SELECT * FROM sections WHERE courseId = :courseId")
    List<Section> getSectionsForCourse(int courseId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSections(List<Section> sections);

    // Lesson methods
    @Query("SELECT * FROM lessons WHERE courseId = :courseId AND sectionId IS NULL")
    List<Lesson> getDirectLessonsForCourse(int courseId);

    @Query("SELECT * FROM lessons WHERE courseId = :courseId AND sectionId = :sectionId")
    List<Lesson> getLessonsForSection(int courseId, String sectionId);

    @Query("SELECT * FROM lessons WHERE sectionId = :sectionId")
    List<Lesson> getLessonsBySectionId(String sectionId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLessons(List<Lesson> lessons);

    // SvgCacheEntry methods
    @Query("SELECT * FROM svg_cache WHERE url = :url")
    SvgCacheEntry getSvgCacheEntry(String url);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSvgCacheEntry(SvgCacheEntry entry);

    @Query("DELETE FROM svg_cache")
    void clearSvgCache();

    // Clear all data
    @Query("DELETE FROM categories")
    void clearCategories();

    @Query("DELETE FROM courses")
    void clearCourses();

    @Query("DELETE FROM lessons")
    void clearLessons();

    @Query("DELETE FROM sections")
    void clearSections();

    default void clearAllData() {
        clearCategories();
        clearCourses();
        clearLessons();
        clearSections();
        clearSvgCache();
    }
}