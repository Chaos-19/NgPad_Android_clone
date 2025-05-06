package com.chaosdev.ngpad.data.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Interview;
import com.chaosdev.ngpad.model.main.InterviewQuestion;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.SvgCacheEntry;
import com.chaosdev.ngpad.model.main.Option;
import com.chaosdev.ngpad.model.main.Question;
import com.chaosdev.ngpad.model.main.Quiz;
import com.chaosdev.ngpad.model.main.Section;

@Database(
    entities = {Category.class, Course.class, Lesson.class, Section.class,SvgCacheEntry.class,Quiz.class, Question.class, Option.class,Interview.class,InterviewQuestion.class},
    version = 8, // Incremented from 1 to 2
    exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NgPadDao ngPadDao();
    public abstract QuizDao quizDao();
    public abstract InterviewDao interviewDao();
    
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "ngpad_database")
                             //.addMigrations(MIGRATION_1_2)
                            .fallbackToDestructiveMigration() // Wipes database on schema mismatch
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    
  /*
  static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    @Override
    public void migrate(@NonNull SupportSQLiteDatabase database) {
        database.execSQL("CREATE TABLE quizzes (" +
                "id INTEGER NOT NULL, " +
                "title TEXT NOT NULL, " +
                "slug TEXT NOT NULL PRIMARY KEY, " +
                "iconUrl TEXT)");
        
        database.execSQL("CREATE TABLE questions (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "text TEXT NOT NULL, " +
                "detail TEXT, " +
                "output TEXT, " +
                "quizSlug TEXT NOT NULL, " +
                "FOREIGN KEY(quizSlug) REFERENCES quizzes(slug) ON DELETE CASCADE)");
        
        database.execSQL("CREATE TABLE options (" +
                "id INTEGER NOT NULL PRIMARY KEY, " +
                "key TEXT NOT NULL, " +
                "text TEXT NOT NULL, " +
                "isCorrect INTEGER NOT NULL, " +
                "questionId INTEGER NOT NULL, " +
                "FOREIGN KEY(questionId) REFERENCES questions(id) ON DELETE CASCADE)");
    }
};  */
    
}