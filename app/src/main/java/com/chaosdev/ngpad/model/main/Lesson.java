package com.chaosdev.ngpad.model.main;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(
    tableName = "lessons",
    foreignKeys = {
        @ForeignKey(
            entity = Course.class,
            parentColumns = "id",
            childColumns = "courseId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Section.class,
            parentColumns = "slug",
            childColumns = "sectionId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {@Index(value = {"courseId"}), @Index(value = {"sectionId"})}
)
public class Lesson {
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    private int courseId;

    private String sectionId; // Links to a Section; null if the lesson is directly under a course

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

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return id == lesson.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}