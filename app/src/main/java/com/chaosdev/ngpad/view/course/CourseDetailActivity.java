package com.chaosdev.ngpad.view.course;

import androidx.appcompat.widget.Toolbar;
import com.chaosdev.ngpad.R;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CourseDetailActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_course_detail);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Enable the back icon
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setTitle("Course Details");
    }

    // Retrieve the course ID from the Intent
    int courseId = getIntent().getIntExtra("course_id", -1);

    // Display the course details (for now, just the ID)
    TextView courseIdTextView = findViewById(R.id.courseIdTextView);
    courseIdTextView.setText("Course ID: " + courseId);
  }
}
