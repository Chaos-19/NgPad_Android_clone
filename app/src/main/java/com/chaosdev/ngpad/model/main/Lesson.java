package com.chaosdev.ngpad.model.main;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "lessons",
        foreignKeys = {
                @ForeignKey(entity = Course.class,
                        parentColumns = "id",
                        childColumns = "courseId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Lesson.class,
                        parentColumns = "id",
                        childColumns = "parentLessonId",
                        onDelete = ForeignKey.CASCADE)
        })
public class Lesson {
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    private int courseId; // Foreign key to Course (if directly under a course)
    private Integer parentLessonId; // Foreign key to parent Lesson (if nested)

    @Ignore
    @SerializedName("sections")
    private List<Lesson> subLessons = new ArrayList<>();

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Integer getParentLessonId() {
        return parentLessonId;
    }

    public void setParentLessonId(Integer parentLessonId) {
        this.parentLessonId = parentLessonId;
    }

    public List<Lesson> getSubLessons() {
        return subLessons;
    }

    public void addSubLesson(Lesson subLesson) {
        subLessons.add(subLesson);
    }

    public boolean isContainer() {
        return content == null;
    }
}