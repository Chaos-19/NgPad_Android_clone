package com.chaosdev.ngpad.model.main;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.List;

@Entity(
    tableName = "interviewQuestions",
    foreignKeys =
        @ForeignKey(
            entity = Interview.class,
            parentColumns = "slug",
            childColumns = "interviewSlug",
            onDelete = ForeignKey.CASCADE))
public class InterviewQuestion {
  @PrimaryKey
  @SerializedName("id")
  private int id;

  @SerializedName("interview_question")
  private String interviewQuestion;

  @SerializedName("answer")
  private String answer;

  private String interviewSlug; // Foreign key to interview

  public InterviewQuestion() {}

  public InterviewQuestion(int id, String interviewQuestion, String answer, String interviewSlug) {
    this.id = id;
    this.interviewQuestion = interviewQuestion;
    this.answer = answer;
    this.interviewSlug = interviewSlug;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getInterviewQuestion() {
    return this.interviewQuestion;
  }

  public void setInterviewQuestion(String interviewQuestion) {
    this.interviewQuestion = interviewQuestion;
  }

  public String getAnswer() {
    return this.answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public String getInterviewSlug() {
    return this.interviewSlug;
  }

  public void setInterviewSlug(String interviewSlug) {
    this.interviewSlug = interviewSlug;
  }
}
