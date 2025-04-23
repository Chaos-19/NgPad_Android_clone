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

/*
import java.util.ArrayList;
import java.util.List;

public class Course {
  private int Id;
  private String courseTitle;
  private String courseIcon;
  private String courseDescription;
  private List<Lesson> lessons;

  public Course(int Id, String courseTitle, String courseIcon, String courseDescription) {
    this.Id = Id;
    this.courseTitle = courseTitle;
    this.courseIcon = courseIcon;
    this.courseDescription = courseDescription;
    this.lessons = new ArrayList<>();
  }

  public List<Lesson> getLessons() {
    return lessons;
  }

  public void addLesson(Lesson lesson) {
    lessons.add(lesson);
  }

  public String getCourseTitle() {
    return this.courseTitle;
  }

  public void setCourseTitle(String courseTitle) {
    this.courseTitle = courseTitle;
  }

  public String getCourseDescription() {
    return this.courseDescription;
  }

  public void setCourseDescription(String courseDescription) {
    this.courseDescription = courseDescription;
  }

  public int getId() {
    return this.Id;
  }

  public void setId(int Id) {
    this.Id = Id;
  }

  public String getCourseIcon() {
    return this.courseIcon;
  }

  public void setCourseIcon(String courseIcon) {
    this.courseIcon = courseIcon;
  }
}
*/
