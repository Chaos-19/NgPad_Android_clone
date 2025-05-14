package com.chaosdev.ngpad.view.section;

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
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.main.Section;
import com.chaosdev.ngpad.utils.SvgLoader;
import com.chaosdev.ngpad.view.course.adapter.LessonAdapter;
import java.util.ArrayList;
import java.util.List;

public class SectionDetailActivity extends AppCompatActivity {
  private static final String TAG = "SectionDetailActivity";
  private NgPadRepository repository;
  private RecyclerView recyclerView;
  private LessonAdapter adapter;
  private List<Lesson> lessons = new ArrayList<>();
  private ProgressBar progressBar;
  private String sectionId;
  private int sectionNum;
  private int courseId;
  private String sectionSlug;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_section_detail);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    progressBar = findViewById(R.id.progressBar);

    recyclerView = findViewById(R.id.lessonsRecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    adapter = new LessonAdapter(this, lessons);
    recyclerView.setAdapter(adapter);

    repository = NgPadRepository.getInstance(this);

    sectionId = getIntent().getStringExtra("section_id");
    sectionNum = getIntent().getIntExtra("section_no", -1);
    courseId = getIntent().getIntExtra("course_id", -1);
    String sectionTitle = getIntent().getStringExtra("section_title");
    sectionSlug = getIntent().getStringExtra("section_slug");
    TextView sectionTitleTextView = findViewById(R.id.sectionTitleTextView);
    TextView sectionNo = findViewById(R.id.section_id);
    ImageView svgImageView = findViewById(R.id.section_icon);

    sectionNo.setText(String.format("Section %d", sectionNum));

    repository.getCourseById(
        courseId,
        course -> {
          if (course != null) {

            String imageUrl = course.getIcon();
            svgImageView.setTag(imageUrl);
            SvgLoader.loadSvgFromUrl(this, svgImageView, imageUrl, R.drawable.advwebdev);

          } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(SectionDetailActivity.this, "Course not found", Toast.LENGTH_SHORT)
                .show();

            finish();
          }
        });

    sectionTitleTextView.setText(sectionTitle);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setTitle(sectionTitle);
    }

    fetchLessonsForSection();
  }

  private void fetchLessonsForSection() {
    progressBar.setVisibility(View.VISIBLE);
    repository.fetchLessonsForSection(
        sectionId,
        sectionSlug,
        courseId,
        new NgPadRepository.SectionLessonsCallback() {
          @Override
          public void onLessonsFetched(List<Lesson> fetchedLessons) {
            progressBar.setVisibility(View.GONE);
            lessons.clear();
            lessons.addAll(fetchedLessons);

            Course dummyCourse = new Course();
            dummyCourse.setId(courseId);
            dummyCourse.setIsNested(false);
            dummyCourse.getLessons().addAll(lessons);
            adapter.updateLessons(dummyCourse);
            Log.d(TAG, "Lessons fetched for section " + sectionId + ": " + lessons.size());
            Toast.makeText(
                    SectionDetailActivity.this,
                    "Section Lessons: " + lessons.size(),
                    Toast.LENGTH_SHORT)
                .show();
            if (lessons.isEmpty()) {
              Toast.makeText(
                      SectionDetailActivity.this,
                      "No lessons available for this section",
                      Toast.LENGTH_SHORT)
                  .show();
            }
          }

          @Override
          public void onError(String message) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(SectionDetailActivity.this, message, Toast.LENGTH_SHORT).show();
          }
        });
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
