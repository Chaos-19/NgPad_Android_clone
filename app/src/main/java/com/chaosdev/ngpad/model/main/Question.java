package com.chaosdev.ngpad.model.main;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.List;

@Entity(tableName = "questions",
        foreignKeys = @ForeignKey(entity = Quiz.class,
                parentColumns = "slug",
                childColumns = "quizSlug",
                onDelete = ForeignKey.CASCADE))
public class Question {
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("text")
    private String text;

    @SerializedName("detail")
    private String detail;

    @SerializedName("output")
    private String output;

    @Ignore
    private List<Option> options; // Ignored in Room, handled via relation

    private String quizSlug; // Foreign key to Quiz

    // Default constructor for Room
    public Question() {}

    // Constructor for API/Gson
    @Ignore
    public Question(int id, String text, String detail, String output, List<Option> options, String quizSlug) {
        this.id = id;
        this.text = text;
        this.detail = detail;
        this.output = output;
        this.options = options;
        this.quizSlug = quizSlug;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getQuizSlug() {
        return quizSlug;
    }

    public void setQuizSlug(String quizSlug) {
        this.quizSlug = quizSlug;
    }
}