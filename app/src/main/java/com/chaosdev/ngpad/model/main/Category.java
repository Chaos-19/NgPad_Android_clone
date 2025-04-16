package com.chaosdev.ngpad.model.main;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private int id;
    private String categoryName;
    private String iconUrl;
    private String description;
    private List<Course> courses;
    private boolean isHorizontal; // New field

    public Category(int id, String categoryName, String iconUrl, String description) {
        this.id = id;
        this.categoryName = categoryName;
        this.iconUrl = iconUrl;
        this.description = description;
        this.courses = new ArrayList<>();
        this.isHorizontal = false; // Default to vertical
    }

    // Existing getters and setters...

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setIsHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public String getCategoryName() {
        return categoryName;
    }
}