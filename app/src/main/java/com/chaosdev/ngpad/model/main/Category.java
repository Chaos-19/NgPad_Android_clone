package com.chaosdev.ngpad.model.main;

import java.util.ArrayList;
import java.util.List;

public class Category {
  private String categoryName;
  private String categoryIcon;
  private List<Course> courses;

  public Category(String categoryName, String categoryIcon) {
    this.categoryName = categoryName;
    this.categoryIcon = categoryIcon;
    this.courses = new ArrayList<>();
  }

  public List<Course> getCourses() {
    return courses;
  }

  public void addCourse(Course course) {
    courses.add(course);
  }

  public String getCategoryName() {
    return this.categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getCategoryIcon() {
    return this.categoryIcon;
  }

  public void setCategoryIcon(String categoryIcon) {
    this.categoryIcon = categoryIcon;
  }
}
