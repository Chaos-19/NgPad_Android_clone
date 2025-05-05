package com.chaosdev.ngpad.data.database;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.chaosdev.ngpad.model.main.Option;
import com.chaosdev.ngpad.model.main.Question;
import com.chaosdev.ngpad.model.main.Quiz;
import java.util.List;

public class QuizWithQuestions {
  @Embedded public Quiz quiz;

  @Relation(parentColumn = "slug", entityColumn = "quizSlug")
  public List<Question> questions;
}
