package com.chaosdev.ngpad.data.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.chaosdev.ngpad.data.database.AppDatabase;
import com.chaosdev.ngpad.data.database.BookMarkDao;
import com.chaosdev.ngpad.data.database.InterviewDao;
import com.chaosdev.ngpad.data.database.NgPadDao;
import com.chaosdev.ngpad.data.database.QuestionWithOptions;
import com.chaosdev.ngpad.data.database.QuizDao;
import com.chaosdev.ngpad.data.database.QuizWithQuestions;
import com.chaosdev.ngpad.data.datasource.remote.ApiService;
import com.chaosdev.ngpad.data.datasource.remote.RetrofitClient;
import com.chaosdev.ngpad.model.main.BookMark;
import com.chaosdev.ngpad.model.main.Interview;
import com.chaosdev.ngpad.model.main.InterviewQuestion;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.main.NgPad;
import com.chaosdev.ngpad.model.main.Option;
import com.chaosdev.ngpad.model.main.Question;
import com.chaosdev.ngpad.model.main.Quiz;
import com.chaosdev.ngpad.model.main.Section;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarkRepository {
  private static final String TAG = "BookmarkRepository";
  private static BookmarkRepository instance;
  private final BookMarkDao bookMarkDao;
  private final NgPadDao ngPadDao;
  private final ExecutorService executorService;
  private final Handler mainHandler;
  private final Context context;  

  private BookmarkRepository(Context context) {
    this.context = context.getApplicationContext(); // Store application context
    ngPadDao = AppDatabase.getInstance(context).ngPadDao();
    bookMarkDao = AppDatabase.getInstance(context).bookMarkDao();
    executorService = Executors.newSingleThreadExecutor();
    mainHandler = new Handler(Looper.getMainLooper());
  }

  public static synchronized BookmarkRepository getInstance(Context context) {
    if (instance == null) {
      instance = new BookmarkRepository(context);
    }

    return instance;
  }

  public void addBookMark(int lessonId, final BookMarkCallback callback) {
    executorService.execute(
        () -> {
          try {
            Lesson catchLesson = ngPadDao.getLessonsByLessonId(lessonId);
            BookMark catchBookMark = bookMarkDao.getBookMarkByLessonId(lessonId);
            if (catchBookMark == null) {
              BookMark bookmark =
                  new BookMark(catchLesson.getId(), lessonId, catchLesson.getTitle());
              if (catchLesson != null) {
                if (catchLesson.getSectionId() != null) {
                  Section lessonSection = ngPadDao.getSectionByLessonId(catchLesson.getSectionId());
                  if (lessonSection != null) {
                    bookmark.setSectionTitle(lessonSection.getTitle());
                  }
                }
                bookMarkDao.insertBookMark(bookmark);
                mainHandler.post(() -> callback.onBookMarkInsert("Bookmark inserted"));
              } else {
                mainHandler.post(() -> callback.onError("Error inserting Bookmark"));
              }
            } else {
              bookMarkDao.deleteBookMark(lessonId);
              Log.d(TAG, "Else if delete : " + lessonId);          
              mainHandler.post(() -> callback.onBookMarkInsert("Bookmark Deleted"));
            }
          } catch (Error e) {
            mainHandler.post(() -> callback.onError("Error inserting Bookmark"));
            Log.d(TAG, "Error : " + e.getMessage());
          }
        });
  }

  public void fetchAllBookMark(final BookMarkCallback callback) {
    executorService.execute(
        () -> {
          List<BookMark> bookmarks = bookMarkDao.getAllBookMarks();

          if (!bookmarks.isEmpty()) {
            mainHandler.post(() -> callback.onBookMarkedFetched(bookmarks));
          } else {
            mainHandler.post(() -> callback.onError("Error fetching bookmark"));
          }
        });
  }

  public void shutdown() {
    executorService.shutdown();
  }

  public interface BookMarkCallback {
    void onBookMarkInsert(String msg);

    void onBookMarkedFetched(List<BookMark> bookmarks);

    void onError(String message);
  }
}
