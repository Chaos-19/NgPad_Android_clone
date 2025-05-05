package com.chaosdev.ngpad.data.database;

import androidx.room.Embedded;
import androidx.room.Relation;
import com.chaosdev.ngpad.model.main.Option;
import com.chaosdev.ngpad.model.main.Question;
import java.util.List;

public class QuestionWithOptions {
  @Embedded public Question question;

  @Relation(parentColumn = "id", entityColumn = "questionId")
  public List<Option> options;
}
