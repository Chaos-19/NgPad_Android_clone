package com.chaosdev.ngpad.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.chaosdev.ngpad.model.main.Interview;
import com.chaosdev.ngpad.model.main.InterviewQuestion;
import java.util.List;

@Dao
public interface InterviewDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertInterviews(List<Interview> interviews);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertInterviewQuestions(List<InterviewQuestion> interviews);

  @Query("SELECT * FROM interviews")
  List<Interview> getAllInterviews();

  @Query("SELECT * FROM interviewQuestions WHERE interviewSlug = :interviewSlug")
  List<InterviewQuestion> getInterviewQuestionsByInterviewSlug(String interviewSlug);

  @Query("DELETE FROM interviews")
  void clearInterviews();

  @Query("DELETE FROM interviewQuestions")
  void clearInterviewQuestions();
}
