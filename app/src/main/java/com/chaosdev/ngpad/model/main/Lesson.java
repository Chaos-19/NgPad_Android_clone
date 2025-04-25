package com.chaosdev.ngpad.model.main;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Lesson {
  @SerializedName("id")
  private String id;

  @SerializedName("title")
  private String title;

  @SerializedName("content")
  private String content;

  @SerializedName("parent_type")
  private String parentType;

  @SerializedName("parent_id")
  private String parentId;

  private List<Lesson> subLessons = new ArrayList<>();

  // Getters
  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getParentType() {
    return parentType;
  }

  public String getParentId() {
    return parentId;
  }

  public List<Lesson> getSubLessons() {
    return subLessons;
  }

  public void addSubLesson(Lesson lesson) {
    subLessons.add(lesson);
  }

  public boolean isContainer() {
    return content == null || content.isEmpty();
  }
}