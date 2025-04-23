package com.chaosdev.ngpad.ui.main;

import android.os.Bundle;
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

public class HomeFragment extends Fragment {
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
    viewModel.getNgPadLiveData().observe(this, this::updateUI);
    viewModel.getErrorMessage().observe(this, this::showError);
    viewModel.fetchNgPadData();

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
    // Update your UI here, e.g., set adapter data for a RecyclerView
    // Example: recyclerViewAdapter.setData(ngPad.getCategories());
 Toast.makeText(requireContext(), "Updating UI : " + ngPad.getCategories().size(), Toast.LENGTH_SHORT).show();

    ngPadAdapter = new NgPadAdapter(requireContext(), ngPad.getCategories());
    expandableListView.setAdapter(ngPadAdapter);
        
  }

  private void showError(String message) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
