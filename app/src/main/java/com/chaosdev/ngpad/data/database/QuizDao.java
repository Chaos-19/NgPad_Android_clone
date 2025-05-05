package com.chaosdev.ngpad.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import com.chaosdev.ngpad.model.main.Option;
import com.chaosdev.ngpad.model.main.Question;
import com.chaosdev.ngpad.model.main.Quiz;
import java.util.List;

@Dao
public interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuizzes(List<Quiz> quizzes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestions(List<Question> questions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOptions(List<Option> options);

    @Query("SELECT * FROM quizzes")
    List<Quiz> getAllQuizzes();

    @Query("SELECT * FROM questions WHERE quizSlug = :quizSlug")
    List<Question> getQuestionsByQuizSlug(String quizSlug);

    @Query("DELETE FROM quizzes")
    void clearQuizzes();

    @Query("DELETE FROM questions")
    void clearQuestions();

    @Query("DELETE FROM options")
    void clearOptions();

    @Transaction
    @Query("SELECT * FROM quizzes")
    List<QuizWithQuestions> getQuizzesWithQuestions();

    @Transaction
    @Query("SELECT * FROM questions WHERE quizSlug = :quizSlug")
    List<QuestionWithOptions> getQuestionsWithOptions(String quizSlug);
}