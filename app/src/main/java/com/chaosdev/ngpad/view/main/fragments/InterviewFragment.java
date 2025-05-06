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

import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.databinding.FragmentMainBinding;
import com.chaosdev.ngpad.databinding.FragmentTabInterviewBinding;
import com.chaosdev.ngpad.model.main.Interview;
import com.chaosdev.ngpad.view.interview.InterviewDetailActivity;
import com.chaosdev.ngpad.view.main.InterviewViewModelFactory;
import com.chaosdev.ngpad.view.main.adapters.InterviewAdapter;
import com.chaosdev.ngpad.view.main.adapters.QuizeAdapter;
import com.chaosdev.ngpad.viewmodel.main.InterviewViewModel;
import java.util.ArrayList;

/** A placeholder fragment containing a simple view. */
public class InterviewFragment extends Fragment {
  private FragmentTabInterviewBinding binding;

  private InterviewViewModel viewModel;
  private InterviewAdapter adapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    InterviewViewModelFactory factory = new InterviewViewModelFactory(requireContext());
    viewModel = new ViewModelProvider(this, factory).get(InterviewViewModel.class);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentTabInterviewBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    Context context = getContext();
    GridView quizGrid = binding.gridView;
    ProgressBar progressBar = binding.progressBar;

    viewModel
        .getInterviews()
        .observe(
            getViewLifecycleOwner(),
            interview -> {
              if (interview != null && !interview.isEmpty()) {
                adapter = new InterviewAdapter(context, (ArrayList) interview);
                Toast.makeText(context, "size : " + interview.size(), Toast.LENGTH_LONG).show();

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

    viewModel.fetchInterviews();

    quizGrid.setOnItemClickListener(
        (parent, view, position, id) -> {
          Interview selectedInterview = adapter.getItem(position);

          Intent intent = new Intent(context, InterviewDetailActivity.class);
          intent.putExtra("interview_slug", selectedInterview.getSlug());
          intent.putExtra("interview_title", selectedInterview.getTitle());
          context.startActivity(intent);
        });

    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
