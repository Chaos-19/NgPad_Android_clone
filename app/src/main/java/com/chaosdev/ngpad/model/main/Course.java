package com.chaosdev.ngpad.model.main;

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
