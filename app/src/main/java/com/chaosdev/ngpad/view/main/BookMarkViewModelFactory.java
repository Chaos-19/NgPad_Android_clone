package com.chaosdev.ngpad.view.main;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.chaosdev.ngpad.viewmodel.main.BookMarkViewModel;

public class BookMarkViewModelFactory implements ViewModelProvider.Factory {
  private final Context context;

  public BookMarkViewModelFactory(Context context) {
    this.context = context.getApplicationContext(); // Ensure application context
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(BookMarkViewModel.class)) {
      return (T) new BookMarkViewModel(context);
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
