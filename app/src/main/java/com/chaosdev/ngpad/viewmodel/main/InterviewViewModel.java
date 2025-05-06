package com.chaosdev.ngpad.viewmodel.main;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.chaosdev.ngpad.data.repository.InterviewRepository;
import com.chaosdev.ngpad.model.main.Interview;
import com.chaosdev.ngpad.model.main.InterviewQuestion;
import java.util.List;

public class InterviewViewModel extends ViewModel {
  private final InterviewRepository repository;
  private final MutableLiveData<List<Interview>> interviews = new MutableLiveData<>();
  private final MutableLiveData<List<InterviewQuestion>> interviewQuestion =
      new MutableLiveData<>();
  private final MutableLiveData<String> error = new MutableLiveData<>();
  private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

  public InterviewViewModel(Context context) {
    this.repository = InterviewRepository.getInstance(context);
  }

  public LiveData<List<Interview>> getInterviews() {
    return interviews;
  }

  public LiveData<List<InterviewQuestion>> getInterviewQuestions() {
    return interviewQuestion;
  }

  public LiveData<String> getError() {
    return error;
  }

  public LiveData<Boolean> getIsLoading() {
    return isLoading;
  }

  public void fetchInterviews() {
    isLoading.setValue(true);
    repository.fetchInterviews(
        new InterviewRepository.InterviewCallback() {
          @Override
          public void onInterviewsFetched(List<Interview> interviewsL) {
            interviews.setValue(interviewsL);
            isLoading.setValue(false);
          }

          @Override
          public void onError(String message) {
            error.setValue(message);
            isLoading.setValue(false);
          }
        });
  }

  public void fetchQuestionsByQuizSlug(String quizSlug) {
    isLoading.setValue(true);
    repository.fetchInterviewQuestionBySlug(
        quizSlug,
        new InterviewRepository.InterviewQuestionCallback() {
          @Override
          public void onInterviewsQuesrionFetched(List<InterviewQuestion> interviewQuestions) {
            interviewQuestion.setValue(interviewQuestions);
            isLoading.setValue(false);
          }

          @Override
          public void onError(String message) {
            error.setValue(message);
            isLoading.setValue(false);
          }
        });
  }
}
