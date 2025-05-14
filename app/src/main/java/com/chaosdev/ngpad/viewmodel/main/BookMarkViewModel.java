package com.chaosdev.ngpad.viewmodel.main;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.chaosdev.ngpad.data.repository.BookmarkRepository;
import com.chaosdev.ngpad.data.repository.InterviewRepository;
import com.chaosdev.ngpad.model.main.BookMark;
import com.chaosdev.ngpad.model.main.Interview;
import com.chaosdev.ngpad.model.main.InterviewQuestion;
import java.util.List;

public class BookMarkViewModel extends ViewModel {
  private final BookmarkRepository repository;
  private final MutableLiveData<List<BookMark>> bookmarks = new MutableLiveData<>();

  private final MutableLiveData<String> error = new MutableLiveData<>();
  private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

  public BookMarkViewModel(Context context) {
    this.repository = BookmarkRepository.getInstance(context);
  }

  public LiveData<List<BookMark>> getBookMarks() {
    return bookmarks;
  }

  public LiveData<String> getError() {
    return error;
  }

  public LiveData<Boolean> getIsLoading() {
    return isLoading;
  }

  public void fetchBookMarks() {
    isLoading.setValue(true);
    repository.fetchAllBookMark(
        new BookmarkRepository.BookMarkCallback() {

          @Override
          public void onBookMarkInsert(String msg) {}

          @Override
          public void onBookMarkedFetched(List<BookMark> bookmark) {
            bookmarks.setValue(bookmark);
            isLoading.setValue(false);
          }

          @Override
          public void onError(String message) {
            error.setValue(message);
            isLoading.setValue(false);
          }
        });
  }

  public void addLessonToBookMarks(int lessonId) {
    isLoading.setValue(true);
    repository.addBookMark(
        lessonId,
        new BookmarkRepository.BookMarkCallback() {

          @Override
          public void onBookMarkInsert(String msg) {
            isLoading.setValue(false);
            fetchBookMarks();
          }

          @Override
          public void onBookMarkedFetched(List<BookMark> bookmark) {}

          @Override
          public void onError(String message) {
            error.setValue(message);
            isLoading.setValue(false);
          }
        });
  }
}
