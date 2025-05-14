package com.chaosdev.ngpad.model.main;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks")
public class BookMark {
  @PrimaryKey private int id;

  private int lessonId;

  private String title;
  @Nullable
  private String sectionTitle;

  public BookMark(int id, int lessonId, String title) {
    this.id = id;
    this.lessonId = lessonId;
    this.title = title;
  }

  public BookMark() {}

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getLessonId() {
    return this.lessonId;
  }

  public void setLessonId(int lessonId) {
    this.lessonId = lessonId;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSectionTitle() {
    return this.sectionTitle;
  }

  public void setSectionTitle(String sectionTitle) {
    this.sectionTitle = sectionTitle;
  }
}
