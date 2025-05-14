package com.chaosdev.ngpad.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.chaosdev.ngpad.model.main.BookMark;
import java.util.List;

@Dao
public interface BookMarkDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertBookMark(BookMark bookmark);

  @Query("SELECT * FROM bookmarks")
  List<BookMark> getAllBookMarks();
  
  @Query("SELECT * FROM bookmarks WHERE id = :lessonId")  
  BookMark getBookMarkByLessonId(int lessonId);  
    
  @Query("DELETE FROM bookmarks WHERE id = :lessonId")
  void deleteBookMark(int lessonId);

  @Query("DELETE FROM bookmarks")
  void clearInterviews();
}
