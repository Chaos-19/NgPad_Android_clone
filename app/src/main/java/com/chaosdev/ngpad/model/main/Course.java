package com.chaosdev.ngpad.model.main;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Course {
  @SerializedName("id")
  private int id;

  @SerializedName("title")
  private String title;

  @SerializedName("description")
  private String description;

  @SerializedName("icon")
  private String icon;

  @SerializedName("is_nested")
  private boolean isNested;

  private List<Lesson> lessons = new ArrayList<>();

  // Getters
  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getIcon() {
    return icon;
  }

  public boolean isNested() {
    return isNested;
  }

  public List<Lesson> getLessons() {
    return lessons;
  }

  public void addLesson(Lesson lesson) {
    lessons.add(lesson);
  }
}