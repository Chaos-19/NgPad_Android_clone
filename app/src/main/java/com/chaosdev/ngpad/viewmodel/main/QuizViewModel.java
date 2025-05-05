package com.chaosdev.ngpad.viewmodel.main;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.chaosdev.ngpad.data.repository.QuizRepository;
import com.chaosdev.ngpad.model.main.Question;
import com.chaosdev.ngpad.model.main.Quiz;
import java.util.List;

public class QuizViewModel extends ViewModel {
  private final QuizRepository repository;
  private final MutableLiveData<List<Quiz>> quizzes = new MutableLiveData<>();
  private final MutableLiveData<List<Question>> questions = new MutableLiveData<>();
  private final MutableLiveData<String> error = new MutableLiveData<>();
  private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

  public QuizViewModel(Context context) {
    this.repository = QuizRepository.getInstance(context);
  }

  public LiveData<List<Quiz>> getQuizzes() {
    return quizzes;
  }

  public LiveData<List<Question>> getQuestions() {
    return questions;
  }

  public LiveData<String> getError() {
    return error;
  }

  public LiveData<Boolean> getIsLoading() {
    return isLoading;
  }

  public void fetchQuizzes() {
    isLoading.setValue(true);
    repository.fetchQuizzes(
        new QuizRepository.QuizCallback() {
          @Override
          public void onQuizzesFetched(List<Quiz> quizList) {
            quizzes.setValue(quizList);
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
    repository.fetchQuestionsByQuizSlug(
        quizSlug,
        new QuizRepository.QuestionCallback() {
          @Override
          public void onQuestionsFetched(List<Question> questionList) {
            questions.setValue(questionList);
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
