package com.chaosdev.ngpad.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.databinding.FragmentTabHomeBinding;
import com.chaosdev.ngpad.view.main.adapters.ExpandableListViewData;
import com.chaosdev.ngpad.view.main.adapters.NgPadAdapter;
import com.chaosdev.ngpad.viewmodel.main.NgPadViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** A placeholder fragment containing a simple view. */
public class HomeFragment extends Fragment {
  private FragmentTabHomeBinding binding;

  ExpandableListView expandableListView;
  private NgPadViewModel viewModel;
  HashMap<String, List<String>> listChild;
  List<String> listHeader;
  NgPadAdapter ngPadAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    binding = FragmentTabHomeBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

    
    expandableListView =
        binding.expListView;
    viewModel = new ViewModelProvider(this).get(NgPadViewModel.class);

    // Observe data changes
    viewModel
        .getUserLiveData()
        .observe(
            this,
            ngpad -> {
              if (ngpad != null) {
                Log.d("MVVM", "Categories: " + ngpad.getCategories());

                ngPadAdapter = new NgPadAdapter(requireContext(), ngpad.getCategories());
                expandableListView.setAdapter(ngPadAdapter);
              }
            });

    viewModel.fetchUser();

    expandableListView.setOnGroupExpandListener(
        new ExpandableListView.OnGroupExpandListener() {
          @Override
          public void onGroupExpand(int groupPosition) {
            Toast.makeText(requireContext(), "clicked Index :" + groupPosition, Toast.LENGTH_LONG);

            /*
            View groupView = expandableListView.getChildAt(groupPosition);
            ImageView indicator = groupView.findViewById(R.id.indicator);
            indicator.animate().rotation(180).setDuration(200).start();
            */
          }
        });

    expandableListView.setOnGroupCollapseListener(
        new ExpandableListView.OnGroupCollapseListener() {
          @Override
          public void onGroupCollapse(int groupPosition) {
            Toast.makeText(requireContext(), "clicked Index :" + groupPosition, Toast.LENGTH_LONG);
            /*
            View groupView = expandableListView.getChildAt(groupPosition);
            ImageView indicator = groupView.findViewById(R.id.indicator);
            indicator.animate().rotation(0).setDuration(200).start();
            */
          }
        });

    return root;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
