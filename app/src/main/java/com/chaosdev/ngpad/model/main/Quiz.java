package com.chaosdev.ngpad.model.main;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.List;

@Entity(tableName = "quizzes")
public class Quiz {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @PrimaryKey
    @SerializedName("slug")
    @NonNull
    private String slug;

    @SerializedName("icon")
    private String iconUrl;

    @Ignore
    private List<Question> questions; // Ignored in Room, handled via relation

    // Default constructor for Room
    public Quiz() {}

    // Constructor for API/Gson
    @Ignore
    public Quiz(int id, String title, String slug, String iconUrl, List<Question> questions) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.iconUrl = iconUrl;
        this.questions = questions;
    }

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}