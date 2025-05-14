package com.chaosdev.ngpad.view.course;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.data.repository.NgPadRepository;
import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.main.NgPad;
import com.chaosdev.ngpad.utils.StringUtils;
import com.chaosdev.ngpad.utils.SvgLoader;
import com.chaosdev.ngpad.view.course.adapter.LessonAdapter;
import java.util.ArrayList;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {
  private static final String TAG = "CourseDetailActivity";
  private NgPadRepository repository;
  private RecyclerView recyclerView;
  private LessonAdapter adapter;
  private List<Lesson> lessons = new ArrayList<>();
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course_detail);

    
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    
    progressBar = findViewById(R.id.progressBar);

    
    recyclerView = findViewById(R.id.lessonsRecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new LessonAdapter(this, lessons);
    recyclerView.setAdapter(adapter);

    
    repository = NgPadRepository.getInstance(this);

    
    int courseId = getIntent().getIntExtra("course_id", -1);
    TextView courseIdTextView = findViewById(R.id.courseIdTextView);
    ImageView svgImageView = findViewById(R.id.course_icon);

    
    progressBar.setVisibility(View.VISIBLE);
    repository.getCourseById(
        courseId,
        course -> {
          if (course != null) {
            courseIdTextView.setText(course.getDescription());
            String imageUrl = course.getIcon();
            svgImageView.setTag(imageUrl); 
            SvgLoader.loadSvgFromUrl(this, svgImageView, imageUrl, R.drawable.advwebdev);

            
            if (getSupportActionBar() != null) {
              getSupportActionBar().setDisplayHomeAsUpEnabled(true);
              getSupportActionBar().setDisplayShowHomeEnabled(true);
              getSupportActionBar().setTitle(StringUtils.escapSpacialCharacter(course.getTitle()));
            }

            
            fetchCourseContent(course);
          } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(CourseDetailActivity.this, "Course not found", Toast.LENGTH_SHORT)
                .show();
            courseIdTextView.setText("Course ID: " + courseId);
            finish();
          }
        });
  }

  private void fetchCourseContent(Course course) {
    if (course.isNested()) {
      fetchLessonsAndSections(course);
    } else {
      fetchLessonsOnly(course);
    }
  }

  private void fetchLessonsAndSections(Course course) {
    repository.fetchLessonsForCourse(
        course,
        new NgPadRepository.NgPadCallback() {
          @Override
          public void onCategoriesFetched(NgPad ngPad) {}

          @Override
          public void onCoursesFetched(Category category) {}

          @Override
          public void onLessonsFetched(Course updatedCourse) {
            progressBar.setVisibility(View.GONE);
            lessons.clear();
            lessons.addAll(updatedCourse.getLessons());
            adapter.updateLessons(
                updatedCourse); 
            Log.d(
                TAG,
                "Sections fetched for nested course "
                    + course.getId()
                    + ": "
                    + updatedCourse.getSections().size());
            Toast.makeText(
                    CourseDetailActivity.this,
                    "Course Sections: " + updatedCourse.getSections().size(),
                    Toast.LENGTH_SHORT)
                .show();
            if (updatedCourse.getSections().isEmpty()) {
              Toast.makeText(
                      CourseDetailActivity.this,
                      "No sections available for this course",
                      Toast.LENGTH_SHORT)
                  .show();
            }
          }

          @Override
          public void onError(String message) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(CourseDetailActivity.this, message, Toast.LENGTH_SHORT).show();
          }
        });
  }

  private void fetchLessonsOnly(Course course) {
    repository.fetchLessonsForCourse(
        course,
        new NgPadRepository.NgPadCallback() {
          @Override
          public void onCategoriesFetched(NgPad ngPad) {}

          @Override
          public void onCoursesFetched(Category category) {}

          @Override
          public void onLessonsFetched(Course updatedCourse) {
            progressBar.setVisibility(View.GONE);
            lessons.clear();
            lessons.addAll(updatedCourse.getLessons());
            adapter.updateLessons(updatedCourse); 
            Log.d(
                TAG,
                "Lessons fetched for non-nested course " + course.getId() + ": " + lessons.size());
            Toast.makeText(
                    CourseDetailActivity.this,
                    "Course Lessons: " + lessons.size(),
                    Toast.LENGTH_SHORT)
                .show();
            if (lessons.isEmpty()) {
              Toast.makeText(
                      CourseDetailActivity.this,
                      "No lessons available for this course",
                      Toast.LENGTH_SHORT)
                  .show();
            }
          }

          @Override
          public void onError(String message) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(CourseDetailActivity.this, message, Toast.LENGTH_SHORT).show();
          }
        });
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
