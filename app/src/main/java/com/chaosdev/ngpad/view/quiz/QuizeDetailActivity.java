package com.chaosdev.ngpad.view.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chaosdev.ngpad.databinding.ActivityQuizeDetailBinding;
import com.chaosdev.ngpad.model.main.UserQuizAnswer;
import com.chaosdev.ngpad.view.main.QuizViewModelFactory;
import com.chaosdev.ngpad.view.main.adapters.QuizeAdapter;
import com.chaosdev.ngpad.view.quiz.result.QuizResultActivity;
import com.chaosdev.ngpad.viewmodel.main.QuizViewModel;
import java.util.Map;

public class QuizeDetailActivity extends AppCompatActivity {
  private ActivityQuizeDetailBinding binding;

  private String quizeSlug;
  private String quizeTitle;
  private QuizViewModel viewModel;
  private QuizeDetailAdapter adapter;

  private RecyclerView recycleView;

  private int totalQuestion;
  private int remainingQuestion;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityQuizeDetailBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    Toolbar toolbar = binding.toolbar;
    setSupportActionBar(toolbar);

    TextView totalQuestionTv = binding.totalQuestion;
    Button submitBtn = binding.submit;

    recycleView = binding.quizList;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    recycleView.setLayoutManager(layoutManager);

    QuizViewModelFactory factory = new QuizViewModelFactory(this);
    viewModel = new ViewModelProvider(this, factory).get(QuizViewModel.class);

    quizeSlug = getIntent().getStringExtra("quiz_slug");
    quizeTitle = getIntent().getStringExtra("quiz_title");

    submitBtn.setOnClickListener(
        (v) -> {
          viewModel.setOutputDetailVisibility(true);

          int[] quizResult = viewModel.getResultOfQuize();

          if (remainingQuestion != totalQuestion) {
            Intent intent = new Intent(this, QuizResultActivity.class);
            intent.putExtra("quizResult", quizResult);
            intent.putExtra("quizTopic", quizeTitle);
            this.startActivity(intent);
          }
        });

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setTitle(quizeTitle);
    }

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
                adapter = new QuizeDetailAdapter(this, questions, viewModel);
                recycleView.setAdapter(adapter);
                totalQuestion = questions.size();
                remainingQuestion = totalQuestion;
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

    viewModel
        .getUserAnswers()
        .observe(
            this,
            answer -> {
              remainingQuestion = totalQuestion - answer.size();
              totalQuestionTv.setText(
                  String.format("Remaining Question : %d", totalQuestion - answer.size()));
            });

    viewModel.fetchQuestionsByQuizSlug(quizeSlug);

    viewModel
        .getOutputDetailVisibility()
        .observe(
            this,
            visibility -> {
              if (adapter != null) adapter.setOutputDetailVisibility(visibility);
            });
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
