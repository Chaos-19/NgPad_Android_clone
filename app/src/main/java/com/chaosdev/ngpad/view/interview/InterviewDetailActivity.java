package com.chaosdev.ngpad.view.interview;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.chaosdev.ngpad.databinding.ActivityInterviewDetailBinding;
import com.chaosdev.ngpad.view.main.InterviewViewModelFactory;
import com.chaosdev.ngpad.viewmodel.main.InterviewViewModel;

public class InterviewDetailActivity extends AppCompatActivity {
  private ActivityInterviewDetailBinding binding;
  private String interviewSlug;
  private String interviewTitle;
  private ExpandableListView expandableListView;
  private InterviewViewModel viewModel;
  private InterviewDetailAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityInterviewDetailBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    expandableListView = binding.expListView;
    ProgressBar progressBar = binding.progressBar;
    Toolbar toolbar = binding.toolbar;
    setSupportActionBar(toolbar);

    interviewSlug = getIntent().getStringExtra("interview_slug");
    interviewTitle = getIntent().getStringExtra("interview_title");

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setTitle(String.format("%s IQ", interviewTitle));
    }

    InterviewViewModelFactory factory = new InterviewViewModelFactory(this);
    viewModel = new ViewModelProvider(this, factory).get(InterviewViewModel.class);

    viewModel
        .getInterviewQuestions()
        .observe(
            this,
            interviewQuestions -> {
              if (interviewQuestions != null && !interviewQuestions.isEmpty()) {
                adapter = new InterviewDetailAdapter(this, interviewQuestions);
                expandableListView.setAdapter(adapter);
                Toast.makeText(
                        this,
                        "Interview Questions : " + interviewQuestions.size(),
                        Toast.LENGTH_LONG)
                    .show();
                // quizGrid.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

              } else {
                progressBar.setVisibility(View.GONE);
              }
            });

    viewModel
        .getIsLoading()
        .observe(
            this,
            isLoading -> {
              progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            });

    viewModel
        .getError()
        .observe(
            this,
            error -> {
              if (error != null) {
                progressBar.setVisibility(View.GONE);
              }
            });

    viewModel.fetchQuestionsByQuizSlug(interviewSlug);
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
