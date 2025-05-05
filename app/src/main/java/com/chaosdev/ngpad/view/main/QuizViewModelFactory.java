package com.chaosdev.ngpad.view.main;


import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.chaosdev.ngpad.viewmodel.main.QuizViewModel;

public class QuizViewModelFactory implements ViewModelProvider.Factory {
  private final Context context;
        
  public QuizViewModelFactory(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public <T extends ViewModel> T create(Class<T> modelClass) {
    if (modelClass.isAssignableFrom(QuizViewModel.class)) {
      return (T) new QuizViewModel(context);
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
