package com.chaosdev.ngpad.view.course;

import androidx.appcompat.widget.Toolbar;
import com.chaosdev.ngpad.R;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.chaosdev.ngpad.data.repository.NgPadRepository;
import com.chaosdev.ngpad.model.main.Course;

public class CourseDetailActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_course_detail);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Retrieve the course ID from the Intent
    int courseId = getIntent().getIntExtra("course_id", -1);
    TextView courseIdTextView = findViewById(R.id.courseIdTextView);

    // Retrieve the Course from the repository
    NgPadRepository repository = NgPadRepository.getInstance();
    Course course = repository.getCourseById(courseId);

    // Display course details
    if (course != null) {

      courseIdTextView.setText(
          "Course ID: "
              + courseId
              + "\nTitle: "
              + course.getTitle()
              + "\nDescription: "
              + course.getDescription());

      // Enable the back icon
      if (getSupportActionBar() != null) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(course.getTitle());
      }

    } else {
      courseIdTextView.setText("Course ID: " + courseId);
      // Handle case where course is not found
      // finish(); // Close the activity if course is not found
    }
  }
}
