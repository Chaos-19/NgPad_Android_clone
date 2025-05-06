package com.chaosdev.ngpad.view.main;

import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.chaosdev.ngpad.viewmodel.main.InterviewViewModel;

public class InterviewViewModelFactory implements ViewModelProvider.Factory {
  private final Context context;

  public InterviewViewModelFactory(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public <T extends ViewModel> T create(Class<T> modelClass) {
    if (modelClass.isAssignableFrom(InterviewViewModel.class)) {
      return (T) new InterviewViewModel(context);
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
