package com.chaosdev.ngpad.model.main;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Lesson {
  @SerializedName("id")
  private String id;

  @SerializedName("title")
  private String title;

  @SerializedName("content")
  private String content;

  @SerializedName("parent_type")
  private String parentType;

  @SerializedName("parent_id")
  private String parentId;

  private List<Lesson> subLessons = new ArrayList<>();

  // Getters
  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getParentType() {
    return parentType;
  }

  public String getParentId() {
    return parentId;
  }

  public List<Lesson> getSubLessons() {
    return subLessons;
  }

  public void addSubLesson(Lesson lesson) {
    subLessons.add(lesson);
  }

  public boolean isContainer() {
    return content == null || content.isEmpty();
  }
}

/*
import java.util.ArrayList;
import java.util.List;

public class Lesson {
    private String id;
    private String title;
    // Direct content held by the lesson.
    private String content;
    // Child lessons, representing nested lessons (or sections).
    private List<Lesson> subLessons;
    // Optionally, the parent lesson.
    private Lesson parentLesson;

    // Constructor for a direct lesson (with content)
    public Lesson(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.subLessons = new ArrayList<>();
    }

    // Constructor for a container lesson (with sub-lessons)
    public Lesson(String id, String title, List<Lesson> subLessons) {
        this.id = id;
        this.title = title;
        this.subLessons = new ArrayList<>(subLessons);
        this.content = null;  // content is null because it's a container.
        // Optionally link the child lessons back to this parent:
        for (Lesson subLesson : subLessons) {
            subLesson.setParentLesson(this);
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<Lesson> getSubLessons() {
        return subLessons;
    }

    public Lesson getParentLesson() {
        return parentLesson;
    }

    public void setParentLesson(Lesson parentLesson) {
        this.parentLesson = parentLesson;
    }

    // Utility method: add a sub-lesson (ensuring this lesson is a container)
    public void addSubLesson(Lesson lesson) {
        if(this.content != null) {
            throw new IllegalStateException("This lesson already has direct content. Cannot add sub-lessons.");
        }
        lesson.setParentLesson(this);
        subLessons.add(lesson);
    }

    // Example validation method (optional): enforce that either content is set or subLessons is used
    public boolean isValid() {
        boolean hasContent = content != null && !content.isEmpty();
        boolean hasSubLessons = subLessons != null && !subLessons.isEmpty();
        // Should have one or the other, not both.
        return (hasContent ^ hasSubLessons); // Bitwise XOR, true if exactly one is true.
    }
}
*/
