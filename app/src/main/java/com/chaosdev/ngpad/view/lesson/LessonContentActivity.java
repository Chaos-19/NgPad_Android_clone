package com.chaosdev.ngpad.view.lesson;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.data.repository.NgPadRepository;
import com.chaosdev.ngpad.utils.StringUtils;

public class LessonContentActivity extends AppCompatActivity {
  private NgPadRepository repository;
  private int lessonContentId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lesson_content);

    // Set up Toolbar
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Enable the back icon and set the title
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
        
    TextView contentTectV = findViewById(R.id.lesson_content);
    TextView lessonTitleV = findViewById(R.id.lessonTitle);

    // Initialize repository
    repository = NgPadRepository.getInstance(this);

    lessonContentId = getIntent().getIntExtra("lesson_id", -1);

    contentTectV.setText(String.valueOf(lessonContentId));

    repository.getLessonByID(lessonContentId, (lesson) -> {
        contentTectV.setText(String.valueOf(lesson.getContent()));
        lessonTitleV.setText(StringUtils.escapSpacialCharacter(lesson.getTitle()));        
    });
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
