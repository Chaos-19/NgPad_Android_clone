package com.chaosdev.ngpad.view.course;

import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.chaosdev.ngpad.R;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.chaosdev.ngpad.data.repository.NgPadRepository;
import com.chaosdev.ngpad.model.main.Course;

public class CourseDetailActivity extends AppCompatActivity {

  private NgPadRepository repository;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_course_detail);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    repository = NgPadRepository.getInstance(this);

    // Retrieve the course ID from the Intent
    int courseId = getIntent().getIntExtra("course_id", -1);
    TextView courseIdTextView = findViewById(R.id.courseIdTextView);

    // Retrieve the Course from the repository
    repository = NgPadRepository.getInstance(this);

    repository.getCourseById(
        courseId,
        new NgPadRepository.CourseCallback() {
          @Override
          public void onCourseFetched(Course course) {
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
              Toast.makeText(CourseDetailActivity.this, "Course not found", Toast.LENGTH_SHORT)
                  .show();
              courseIdTextView.setText("Course ID: " + courseId);
              finish();
            }
          }
        });
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
