package com.chaosdev.ngpad.data.repository;

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

public class QuizRepository {
    private static final String TAG = "QuizRepository";
    private static QuizRepository instance;
    private final ApiService apiService;
    private final QuizDao quizDao;
    private final ExecutorService executorService;
    private final Handler mainHandler;

    private QuizRepository(Context context) {
        apiService = RetrofitClient.getApiService();
        quizDao = AppDatabase.getInstance(context).quizDao();
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized QuizRepository getInstance(Context context) {
        if (instance == null) {
            instance = new QuizRepository(context);
        }
        return instance;
    }

    public void fetchQuizzes(final QuizCallback callback) {
        executorService.execute(() -> {
            List<QuizWithQuestions> cachedQuizzes = quizDao.getQuizzesWithQuestions();
            if (!cachedQuizzes.isEmpty()) {
                List<Quiz> quizzes = cachedQuizzes.stream()
                        .map(quizWithQuestions -> {
                            Quiz quiz = quizWithQuestions.quiz;
                            quiz.setQuestions(quizWithQuestions.questions);
                            return quiz;
                        })
                        .collect(Collectors.toList());
                mainHandler.post(() -> callback.onQuizzesFetched(quizzes));
                return;
            }

            apiService.getQuizzes().enqueue(new Callback<List<Quiz>>() {
                @Override
                public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Quiz> quizzes = response.body();
                        executorService.execute(() -> {
                            quizDao.clearQuizzes();
                            quizDao.clearQuestions();
                            quizDao.clearOptions();
                            for (Quiz quiz : quizzes) {
                                quizDao.insertQuizzes(Collections.singletonList(quiz));
                                if (quiz.getQuestions() != null) {
                                    for (Question question : quiz.getQuestions()) {
                                        question.setQuizSlug(quiz.getSlug());
                                        quizDao.insertQuestions(Collections.singletonList(question));
                                        if (question.getOptions() != null) {
                                            for (Option option : question.getOptions()) {
                                                option.setQuestionId(question.getId());
                                                quizDao.insertOptions(Collections.singletonList(option));
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        mainHandler.post(() -> callback.onQuizzesFetched(quizzes));
                    } else {
                        mainHandler.post(() -> callback.onError("Failed to fetch quizzes: " + response.code()));
                    }
                }

                @Override
                public void onFailure(Call<List<Quiz>> call, Throwable t) {
                    mainHandler.post(() -> callback.onError("Failed to fetch quizzes: " + t.getMessage()));
                }
            });
        });
    }

    public void fetchQuestionsByQuizSlug(String quizSlug, final QuestionCallback callback) {
        executorService.execute(() -> {
            // Check Room cache first
            List<QuestionWithOptions> cachedQuestions = quizDao.getQuestionsWithOptions(quizSlug);
            if (!cachedQuestions.isEmpty()) {
                List<Question> questions = cachedQuestions.stream()
                        .map(questionWithOptions -> {
                            Question question = questionWithOptions.question;
                            question.setOptions(questionWithOptions.options);
                            return question;
                        })
                        .collect(Collectors.toList());
                mainHandler.post(() -> callback.onQuestionsFetched(questions));
                return;
            }

            // Fetch from API
            apiService.getQuestionsByQuizSlug(quizSlug).enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Question> questions = response.body();
                        executorService.execute(() -> {
                            quizDao.clearQuestions();
                            quizDao.clearOptions();
                            for (Question question : questions) {
                                question.setQuizSlug(quizSlug);
                                quizDao.insertQuestions(Collections.singletonList(question));
                                if (question.getOptions() != null) {
                                    for (Option option : question.getOptions()) {
                                        option.setQuestionId(question.getId());
                                        quizDao.insertOptions(Collections.singletonList(option));
                                    }
                                }
                            }
                        });
                        mainHandler.post(() -> callback.onQuestionsFetched(questions));
                    } else {
                        mainHandler.post(() -> callback.onError("Failed to fetch questions: " + response.code()));
                    }
                }

                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {
                    mainHandler.post(() -> callback.onError("Failed to fetch questions: " + t.getMessage()));
                }
            });
        });
    }

    public interface QuizCallback {
        void onQuizzesFetched(List<Quiz> quizzes);
        void onError(String message);
    }

    public interface QuestionCallback {
        void onQuestionsFetched(List<Question> questions);
        void onError(String message);
    }
}