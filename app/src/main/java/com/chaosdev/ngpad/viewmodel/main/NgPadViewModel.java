package com.chaosdev.ngpad.viewmodel.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.chaosdev.ngpad.model.main.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.main.NgPad;

public class NgPadViewModel extends ViewModel {
  private MutableLiveData<NgPad> ngPadLiveData = new MutableLiveData<>();

  // Simulate data fetch (e.g., from API/database)
  public void fetchUser() {
    NgPad ngpad = new NgPad();

    Category category = new Category(20, "Basics", "http://localhost:8080/icon.jpj", "basics_web");

    Course course =
        new Course(
            2003,
            "HTML",
            "http://localhost:8080/icon.jpj",
            "Designed for beginners willing to learn computer programming");

    Course course2 =
        new Course(
            2003,
            "JavaScript",
            "http://localhost:8080/icon.jpj",
            "Designed for beginners willing to learn computer programming");

    Course course3 =
        new Course(
            2003,
            "TypeScript",
            "http://localhost:8080/icon.jpj",
            "Designed for beginners willing to learn computer programming");

    Lesson lesson = new Lesson("213", "Introduction", "This is introduction content of the Data");

    course.addLesson(lesson);
    category.addCourse(course);
    category.addCourse(course2);
    category.addCourse(course3);

    ngpad.addCategory(category);

    ngpad.addCategory(category);
    ngpad.addCategory(category);

    ngPadLiveData.setValue(ngpad);
  }

  public MutableLiveData<NgPad> getUserLiveData() {
    return ngPadLiveData;
  }
}
