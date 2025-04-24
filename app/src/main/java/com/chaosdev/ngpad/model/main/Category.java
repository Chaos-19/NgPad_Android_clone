package com.chaosdev.ngpad.model;

import com.chaosdev.ngpad.model.main.Course;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Category {
  @SerializedName("id")
  private int id;

  @SerializedName("name")
  private String name;

  @SerializedName("slug")
  private String slug;

  @SerializedName("icon")
  private String icon;

  private List<Course> courses = new ArrayList<>();
  

  // Getters and addCourse() method
  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSlug() {
    return slug;
  }

  public String getIcon() {
    return icon;
  }

  public List<Course> getCourses() {
    return courses;
  }

  public void addCourse(Course course) {
    courses.add(course);
  }
}
