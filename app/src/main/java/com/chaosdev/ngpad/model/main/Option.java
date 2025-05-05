package com.chaosdev.ngpad.model.main;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "options",
        foreignKeys = @ForeignKey(entity = Question.class,
                parentColumns = "id",
                childColumns = "questionId",
                onDelete = ForeignKey.CASCADE))
public class Option {
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("key")
    private String key;

    @SerializedName("text")
    private String text;

    @SerializedName("is_correct")
    private boolean isCorrect;

    private int questionId; // Foreign key to Question

    // Default constructor for Room
    public Option() {}

    // Constructor for API/Gson
    public Option(int id, String key, String text, boolean isCorrect, int questionId) {
        this.id = id;
        this.key = key;
        this.text = text;
        this.isCorrect = isCorrect;
        this.questionId = questionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}