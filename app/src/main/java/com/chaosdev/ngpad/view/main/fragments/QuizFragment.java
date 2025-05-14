package com.chaosdev.ngpad.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.databinding.FragmentTabQuizBinding;
import com.chaosdev.ngpad.model.main.Quiz;
import com.chaosdev.ngpad.view.main.QuizViewModelFactory;
import com.chaosdev.ngpad.view.main.adapters.QuizeAdapter;
import com.chaosdev.ngpad.view.quiz.QuizeDetailActivity;
import com.chaosdev.ngpad.viewmodel.main.QuizViewModel;
import java.util.ArrayList;

public class QuizFragment extends Fragment {
  private FragmentTabQuizBinding binding;
  private QuizViewModel viewModel;
  private QuizeAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    QuizViewModelFactory factory = new QuizViewModelFactory(requireContext());
    viewModel = new ViewModelProvider(this, factory).get(QuizViewModel.class);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentTabQuizBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    Context context = getContext();
    GridView quizGrid = binding.gridView;
    ProgressBar progressBar = binding.progressBar;
    // TextView errorText = binding.errorText;

    // Observe quizzes
    viewModel
        .getQuizzes()
        .observe(
            getViewLifecycleOwner(),
            quizzes -> {
              if (quizzes != null && !quizzes.isEmpty()) {
                adapter = new QuizeAdapter(context, (ArrayList) quizzes);

                quizGrid.setAdapter(adapter);
                quizGrid.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                // errorText.setVisibility(View.GONE);
              } else {
                quizGrid.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
              }
            });

   

    viewModel
        .getIsLoading()
        .observe(
            getViewLifecycleOwner(),
            isLoading -> {
              progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
              quizGrid.setVisibility(isLoading ? View.GONE : View.VISIBLE);
              // errorText.setVisibility(View.GONE);
            });

    viewModel
        .getError()
        .observe(
            getViewLifecycleOwner(),
            error -> {
              if (error != null) {
                // errorText.setText(error);
                // errorText.setVisibility(View.VISIBLE);
                quizGrid.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
              }
            });

    // Setup click listener for quizzes
    quizGrid.setOnItemClickListener(
        (parent, view, position, id) -> {
          Quiz selectedQuiz = adapter.getItem(position);
          
          Intent intent = new Intent(context, QuizeDetailActivity.class);
          intent.putExtra("quiz_slug", selectedQuiz.getSlug());
          intent.putExtra("quiz_title", selectedQuiz.getTitle());
          context.startActivity(intent);
        });

    // Trigger quiz fetching
    viewModel.fetchQuizzes();

    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}