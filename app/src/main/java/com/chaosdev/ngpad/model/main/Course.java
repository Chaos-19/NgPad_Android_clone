package com.chaosdev.ngpad.model.main;

import java.util.ArrayList;
import java.util.List;

public class Course {
  private String courseTitle;
  private String courseDescription;
  private List<Lesson> lessons;

  public Course(String courseTitle, String courseDescription) {
    this.courseTitle = courseTitle;
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
}
