package com.chaosdev.ngpad.viewmodel.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.chaosdev.ngpad.model.main.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.main.NgPad;

public class NgPadViewModel extends ViewModel {
    private MutableLiveData<NgPad> ngPadLiveData = new MutableLiveData<>();

    public void fetchUser() {
        NgPad ngpad = new NgPad();

        // Horizontal category
        Category category1 = new Category(20, "Featured Courses", "http://localhost:8080/icon.jpj", "featured_web");
        category1.setIsHorizontal(false);
        Course course1 = new Course(2001, "HTML", "http://localhost:8080/icon.jpj", "Learn HTML basics");
        Course course2 = new Course(2002, "JavaScript", "http://localhost:8080/icon.jpj", "Learn JS basics");
        Course course3 = new Course(2003, "TypeScript", "http://localhost:8080/icon.jpj", "Learn TS basics");
        category1.addCourse(course1);
        category1.addCourse(course2);
        category1.addCourse(course3);

        // Vertical category
        Category category2 = new Category(21, "Basics", "http://localhost:8080/icon.jpj", "basics_web");
        category2.setIsHorizontal(true);
        Course course4 = new Course(2004, "CSS", "http://localhost:8080/icon.jpj", "Learn CSS basics");
        Course course5 = new Course(2005, "Python", "http://localhost:8080/icon.jpj", "Learn Python basics");
        Lesson lesson = new Lesson("213", "Introduction", "This is introduction content");
        course4.addLesson(lesson);
        category2.addCourse(course4);
        category2.addCourse(course5);

        ngpad.addCategory(category1);
        ngpad.addCategory(category2);

        ngPadLiveData.setValue(ngpad);
    }

    public MutableLiveData<NgPad> getUserLiveData() {
        return ngPadLiveData;
    }
}