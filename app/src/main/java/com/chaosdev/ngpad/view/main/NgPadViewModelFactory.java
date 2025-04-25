package com.chaosdev.ngpad.view.main;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.chaosdev.ngpad.viewmodel.main.NgPadViewModel;

public class NgPadViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public NgPadViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NgPadViewModel.class)) {
            return (T) new NgPadViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}