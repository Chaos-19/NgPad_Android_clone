package com.chaosdev.ngpad.data.repository;

import android.util.Log;
import com.chaosdev.ngpad.data.database.InterviewDao;
import com.chaosdev.ngpad.data.datasource.remote.ApiService;
import com.chaosdev.ngpad.model.main.Interview;
import com.chaosdev.ngpad.model.main.InterviewQuestion;
import java.util.concurrent.ExecutorService;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.chaosdev.ngpad.data.database.AppDatabase;
import com.chaosdev.ngpad.data.database.QuestionWithOptions;
import com.chaosdev.ngpad.data.database.QuizDao;
import com.chaosdev.ngpad.data.database.QuizWithQuestions;
import com.chaosdev.ngpad.data.datasource.remote.ApiService;
import com.chaosdev.ngpad.data.datasource.remote.RetrofitClient;
import com.chaosdev.ngpad.model.main.Option;
import com.chaosdev.ngpad.model.main.Question;
import com.chaosdev.ngpad.model.main.Quiz;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterviewRepository {
  private static final String TAG = "InterviewRepository";
  private static InterviewRepository instance;
  private final ApiService apiService;
  private final InterviewDao interviewDao;
  private final ExecutorService executorService;
  private final Handler mainHandler;

  private InterviewRepository(Context context) {
    apiService = RetrofitClient.getApiService();
    interviewDao = AppDatabase.getInstance(context).interviewDao();
    executorService = Executors.newSingleThreadExecutor();
    mainHandler = new Handler(Looper.getMainLooper());
  }

  public static synchronized InterviewRepository getInstance(Context context) {
    if (instance == null) {
      instance = new InterviewRepository(context);
    }

    return instance;
  }

 public void fetchInterviews(final InterviewCallback callback) {
    executorService.execute(
        () -> {
          List<Interview> catchinterviews = interviewDao.getAllInterviews();
          if (!catchinterviews.isEmpty()) {
            mainHandler.post(() -> callback.onInterviewsFetched(catchinterviews));
            return;
          }

          apiService
              .getInterviews()
              .enqueue(
                  new Callback<List<Interview>>() {

                    @Override
                    public void onResponse(
                        Call<List<Interview>> call, Response<List<Interview>> response) {
                      if (response.isSuccessful() && response.body() != null) {
                        List<Interview> interviews = response.body();
                        executorService.execute(
                            () -> {
                              interviewDao.clearInterviews();
                              interviewDao.insertInterviews(interviews);
                            });
                        mainHandler.post(() -> callback.onInterviewsFetched(interviews));
                      } else {
                        mainHandler.post(
                            () ->
                                callback.onError("Failed to fetch interviews: " + response.code()));
                      }
                    }

                    @Override
                    public void onFailure(Call<List<Interview>> call, Throwable t) {
                      mainHandler.post(
                          () -> callback.onError("Failed to fetch interviews: " + t.getMessage()));
                    }
                  });
        });
  }

 public void fetchInterviewQuestionBySlug(String slug, final InterviewQuestionCallback callback) {
    executorService.execute(
        () -> {
          List<InterviewQuestion> catchInterviewQuestions =
              interviewDao.getInterviewQuestionsByInterviewSlug(slug);
          if (!catchInterviewQuestions.isEmpty()) {
            //Log.d("Interview Questions Catch :", "" + catchInterviewQuestions.size());
            mainHandler.post(() -> callback.onInterviewsQuesrionFetched(catchInterviewQuestions));
            return;
          }

          apiService
              .getInterviewQuestionsByQuizSlug(slug)
              .enqueue(
                  new Callback<List<InterviewQuestion>>() {

                    @Override
                    public void onResponse(
                        Call<List<InterviewQuestion>> call,
                        Response<List<InterviewQuestion>> response) {
                      if (response.isSuccessful() && response.body() != null) {
                        List<InterviewQuestion> interviewQuestions = response.body();

                        executorService.execute(
                            () -> {
                              //interviewDao.clearInterviewQuestions();
                              for (InterviewQuestion interviewQe : interviewQuestions) {
                                interviewQe.setInterviewSlug(slug);
                                interviewDao.insertInterviewQuestions(interviewQuestions);
                              }
                            });
                        mainHandler.post(
                            () -> callback.onInterviewsQuesrionFetched(interviewQuestions));

                      } else {
                        mainHandler.post(
                            () ->
                                callback.onError(
                                    "Failed to fetch interview Questions: " + response.code()));
                      }
                    }

                    @Override
                    public void onFailure(Call<List<InterviewQuestion>> call, Throwable t) {
                      mainHandler.post(
                          () ->
                              callback.onError(
                                  "Failed to fetch interview Questions: " + t.getMessage()));
                    }
                  });
        });
  }

  public interface InterviewCallback {
    void onInterviewsFetched(List<Interview> interviews);

    void onError(String message);
  }

  public interface InterviewQuestionCallback {
    void onInterviewsQuesrionFetched(List<InterviewQuestion> questions);

    void onError(String message);
  }
}
