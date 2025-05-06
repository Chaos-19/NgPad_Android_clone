package com.chaosdev.ngpad.model.main;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import java.util.List;

@Entity(tableName = "interviews")
public class Interview {
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

  @Ignore private List<InterviewQuestion> questions;

  public Interview() {}

  public Interview(
      int id, String title, String slug, String iconUrl, List<InterviewQuestion> questions) {
    this.id = id;
    this.title = title;
    this.slug = slug;
    this.iconUrl = iconUrl;
    this.questions = questions;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSlug() {
    return this.slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getIconUrl() {
    return this.iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public List<InterviewQuestion> getQuestions() {
    return this.questions;
  }

  public void setQuestions(List<InterviewQuestion> questions) {
    this.questions = questions;
  }
}
