package com.chaosdev.ngpad.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.databinding.FragmentMainBinding;
import com.chaosdev.ngpad.databinding.FragmentTabHelpCenterBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class HelpCenterFragment extends Fragment {
    private FragmentTabHelpCenterBinding binding;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentTabHelpCenterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
