package com.chaosdev.ngpad.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.chaosdev.ngpad.databinding.FragmentTabHomeBinding;
import com.chaosdev.ngpad.model.main.NgPad;
import com.chaosdev.ngpad.view.main.adapters.NgPadAdapter;
import com.chaosdev.ngpad.viewmodel.main.NgPadViewModel;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment {
  private static final String TAG = "HomeFragment";
  private FragmentTabHomeBinding binding;
  private ExpandableListView expandableListView;
  private NgPadViewModel viewModel;
  private NgPadAdapter ngPadAdapter;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentTabHomeBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
    expandableListView = binding.expListView;

    viewModel = new ViewModelProvider(this).get(NgPadViewModel.class);

    // Observe LiveData only if not already observing
    if (!viewModel.getNgPadLiveData().hasObservers()) {
      viewModel.getNgPadLiveData().observe(getViewLifecycleOwner(), this::updateUI);
      viewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::showError);
      viewModel.fetchNgPadData(); // Fetch data only once
    }

    expandableListView.setOnGroupExpandListener(
        groupPosition ->
            Toast.makeText(requireContext(), "Expanded: " + groupPosition, Toast.LENGTH_SHORT)
                .show());

    expandableListView.setOnGroupCollapseListener(
        groupPosition ->
            Toast.makeText(requireContext(), "Collapsed: " + groupPosition, Toast.LENGTH_SHORT)
                .show());

    return root;
  }

  private void updateUI(NgPad ngPad) {
    Log.d(TAG, "Updating UI with " + ngPad.getCategories().size() + " categories");
    Toast.makeText(
            requireContext(), "Updating UI: " + ngPad.getCategories().size(), Toast.LENGTH_SHORT)
        .show();

    ngPadAdapter = new NgPadAdapter(requireContext(), ngPad.getCategories());
    expandableListView.setAdapter(ngPadAdapter);

    // Expand all groups
    int groupCount = ngPadAdapter.getGroupCount();
    for (int i = 0; i < groupCount; i++) {
      if (!(Arrays.asList(2, 5, 9)).contains(i)) expandableListView.expandGroup(i);
    }
  }

  private void showError(String message) {
    Log.e(TAG, "Error: " + message);
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
