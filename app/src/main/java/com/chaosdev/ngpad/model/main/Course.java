package com.chaosdev.ngpad.model.main;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.chaosdev.ngpad.model.Category;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(
    tableName = "courses",
    foreignKeys = @ForeignKey(
        entity = Category.class,
        parentColumns = "id",
        childColumns = "categoryId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index(value = {"categoryId"})}
)
public class Course implements android.os.Parcelable {
    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("icon")
    private String icon;

    @SerializedName("description")
    private String description;

    private int categoryId;
    
    @SerializedName("is_nested")
    private boolean isNested;

    @Ignore
    private List<Lesson> lessons = new ArrayList<>();

    @Ignore
    @SerializedName("sections")
    private List<Section> sections = new ArrayList<>();

    public Course() {
        this.lessons = new ArrayList<>();
        this.sections = new ArrayList<>();
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isNested() {
        return isNested;
    }

    public void setIsNested(boolean isNested) {
        this.isNested = isNested;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    protected Course(android.os.Parcel in) {
        id = in.readInt();
        title = in.readString();
        icon = in.readString();
        description = in.readString();
        categoryId = in.readInt();
        isNested = in.readByte() != 0;
        lessons = new ArrayList<>();
        in.readList(lessons, Lesson.class.getClassLoader());
        sections = new ArrayList<>();
        in.readList(sections, Section.class.getClassLoader());
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(android.os.Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(icon);
        dest.writeString(description);
        dest.writeInt(categoryId);
        dest.writeByte((byte) (isNested ? 1 : 0));
        dest.writeList(lessons);
        dest.writeList(sections);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}