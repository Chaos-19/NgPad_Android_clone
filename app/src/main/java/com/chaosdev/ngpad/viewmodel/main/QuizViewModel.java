package com.chaosdev.ngpad.viewmodel.main;

import android.content.Context;
import android.util.Log;
import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.chaosdev.ngpad.data.repository.QuizRepository;
import com.chaosdev.ngpad.model.main.Option;
import com.chaosdev.ngpad.model.main.Question;
import com.chaosdev.ngpad.model.main.Quiz;
import com.chaosdev.ngpad.model.main.UserQuizAnswer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuizViewModel extends ViewModel {
  private final QuizRepository repository;
  private final MutableLiveData<List<Quiz>> quizzes = new MutableLiveData<>();
  private final MutableLiveData<List<Question>> questions = new MutableLiveData<>();
  private final MutableLiveData<Map<Integer, UserQuizAnswer>> userAnswers = new MutableLiveData<>(new HashMap<>());  
  private final MutableLiveData<String> error = new MutableLiveData<>();
  private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

  // LiveData to hold the visibility state
  private final MutableLiveData<Integer> outputDetailVisibility = new MutableLiveData<>(View.GONE);

  // Expose LiveData to the View
  public LiveData<Integer> getOutputDetailVisibility() {
    return outputDetailVisibility;
  }

  // Method to update visibility (called by business logic)
  public void setOutputDetailVisibility(boolean isVisible) {
    outputDetailVisibility.setValue(isVisible ? View.VISIBLE : View.GONE);
  }

  public QuizViewModel(Context context) {
    this.repository = QuizRepository.getInstance(context);
  }

  public LiveData<List<Quiz>> getQuizzes() {
    return quizzes;
  }

  public LiveData<List<Question>> getQuestions() {
    return questions;
  }

  // Get LiveData for user answers
  public LiveData<Map<Integer, UserQuizAnswer>> getUserAnswers() {
    return userAnswers;
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

  // Update user answer for a question
  public void setUserAnswer(int questionPosition, UserQuizAnswer value,boolean isChecked) {
    Map<Integer, UserQuizAnswer> answers = userAnswers.getValue();
    if (answers != null) {
      if (isChecked) {
        answers.put(questionPosition, value);
      } else {
        answers.remove(questionPosition);
      }
      userAnswers.setValue(answers);
    }
  }

  // Calculate score or results (example)
  public int calculateScore() {
    int score = 0;
    Map<Integer, UserQuizAnswer> answers = userAnswers.getValue();
    if (answers != null && questions != null) {
      List<String> correctOptions =
          questions.getValue().stream()
              .map(Question::getOptions)
              .map(
                  options ->
                      options.stream()
                          .filter(Option::isCorrect)
                          .map(Option::getKey)
                          .findFirst()
                          .orElse(""))
              .collect(Collectors.toList());

      List<String> userQuestionOption =
          userAnswers.getValue().values().stream()
              .map(option -> option.optionKey)
              .collect(Collectors.toList());

      score =
          IntStream.range(0, Math.min(correctOptions.size(), userQuestionOption.size()))
              .map(i -> correctOptions.get(i).toLowerCase().equals(userQuestionOption.get(i).toLowerCase()) ? 1 : 0)
              .sum();
    }
    return score;
  }

  public int[] getResultOfQuize() {
    int score = calculateScore();
    int totalQuestions = questions.getValue().size();
    int incorrectAnswers = totalQuestions - score;
    int totalScore = totalQuestions * 1;

    return new int[] {score, totalQuestions, incorrectAnswers, totalScore};
  }
}
