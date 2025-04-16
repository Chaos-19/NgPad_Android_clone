package com.chaosdev.ngpad.model.main;

import java.util.ArrayList;
import java.util.List;

public class Category {
  private int Id;
  private String categoryName;
  private String categoryIcon;
  private String slug;
  private List<Course> courses;

  public Category(int Id, String categoryName, String categoryIcon, String slug) {
    this.Id = Id;
    this.categoryName = categoryName;
    this.categoryIcon = categoryIcon;
    this.slug = slug;
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

  public String getSlug() {
    return this.slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public int getId() {
    return this.Id;
  }

  public void setId(int Id) {
    this.Id = Id;
  }
}
