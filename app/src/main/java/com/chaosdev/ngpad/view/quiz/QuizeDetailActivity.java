package com.chaosdev.ngpad.view.quiz;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chaosdev.ngpad.databinding.ActivityQuizeDetailBinding;
import com.chaosdev.ngpad.view.main.QuizViewModelFactory;
import com.chaosdev.ngpad.view.main.adapters.QuizeAdapter;
import com.chaosdev.ngpad.viewmodel.main.QuizViewModel;

public class QuizeDetailActivity extends AppCompatActivity {
  private ActivityQuizeDetailBinding binding;

  private String quizeSlug;
  private QuizViewModel viewModel;
  private QuizeAdapter adapter;

  private RecyclerView recycleView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityQuizeDetailBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    Toolbar toolbar = binding.toolbar;
    setSupportActionBar(toolbar);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setTitle("");
    }
    recycleView = binding.quizList;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    recycleView.setLayoutManager(layoutManager);

    QuizViewModelFactory factory = new QuizViewModelFactory(this);
    viewModel = new ViewModelProvider(this, factory).get(QuizViewModel.class);

    quizeSlug = getIntent().getStringExtra("quiz_slug");


    viewModel
        .getIsLoading()
        .observe(
            this,
            isLoading -> {
              Toast.makeText(this, "QuizeDetailActivity : isLoading", Toast.LENGTH_LONG).show();
            });

    viewModel
        .getQuestions()
        .observe(
            this,
            questions -> {
              if (questions != null && !questions.isEmpty()) {
                QuizeDetailAdapter adapter = new QuizeDetailAdapter(this, questions);
                recycleView.setAdapter(adapter);
              }
            });

    viewModel
        .getError()
        .observe(
            this,
            error -> {
              if (error != null) {
                Toast.makeText(this, "QuizeDetailActivity error :" + error, Toast.LENGTH_LONG)
                    .show();
              }
            });

    viewModel.fetchQuestionsByQuizSlug(quizeSlug);
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
