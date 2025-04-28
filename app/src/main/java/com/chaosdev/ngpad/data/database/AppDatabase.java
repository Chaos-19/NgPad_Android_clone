package com.chaosdev.ngpad.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.SvgCacheEntry;
import com.chaosdev.ngpad.model.main.Section;

@Database(
    entities = {Category.class, Course.class, Lesson.class, Section.class,SvgCacheEntry.class},
    version = 6, // Incremented from 1 to 2
    exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NgPadDao ngPadDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "ngpad_database")
                            .fallbackToDestructiveMigration() // Wipes database on schema mismatch
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}