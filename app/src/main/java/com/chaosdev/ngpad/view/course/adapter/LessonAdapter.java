package com.chaosdev.ngpad.view.course.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.main.Section;

import com.chaosdev.ngpad.utils.StringUtils;
import com.chaosdev.ngpad.utils.SvgLoader;
import com.chaosdev.ngpad.view.lesson.LessonContentActivity;
import com.chaosdev.ngpad.view.section.SectionDetailActivity;
import java.util.ArrayList;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int VIEW_TYPE_SECTION = 0;
  private static final int VIEW_TYPE_LESSON = 1;

  private final Context context;
  private List<Object> items = new ArrayList<>(); 
  private boolean isNested;

  public LessonAdapter(Context context, List<Lesson> lessons) {
    this.context = context;
    this.items = new ArrayList<>(lessons);
    this.isNested = false; 
  }

  public void updateLessons(Course course) {
    items.clear();
    this.isNested = course.isNested();

    if (course.isNested()) {
      
      items.addAll(course.getSections());
    } else {
      
      items.addAll(course.getLessons());
    }
    notifyDataSetChanged();
  }

  @Override
  public int getItemViewType(int position) {
    return items.get(position) instanceof Section ? VIEW_TYPE_SECTION : VIEW_TYPE_LESSON;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == VIEW_TYPE_SECTION) {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.section_card, parent, false);
      return new SectionViewHolder(view);
    } else {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_card, parent, false);
      return new LessonViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (getItemViewType(position) == VIEW_TYPE_SECTION) {
      Section section = (Section) items.get(position);
      SectionViewHolder sectionHolder = (SectionViewHolder) holder;

      String sectionTitle = StringUtils.escapSpacialCharacter(section.getTitle());

      sectionHolder.sectionTitleTextView.setText(sectionTitle);
      sectionHolder.sectionDescription.setText(
          section.getDescription() != null ? section.getDescription() : "No description");

      SvgLoader.loadSvgFromUrl(
          context, sectionHolder.sectionIcon, section.getIcon(), R.drawable.advwebdev);

      sectionHolder.itemView.setOnClickListener(
          v -> {
            Intent intent = new Intent(context, SectionDetailActivity.class);
            intent.putExtra("section_id", section.getSlug());
            intent.putExtra("section_no", position + 1);
            intent.putExtra("section_title", section.getTitle());
            intent.putExtra("section_slug", section.getSlug());
            intent.putExtra("course_id", section.getCourseId());
            context.startActivity(intent);
          });
    } else {
      Lesson lesson = (Lesson) items.get(position);
      LessonViewHolder lessonHolder = (LessonViewHolder) holder;

      String lessonTitle = StringUtils.escapSpacialCharacter(lesson.getTitle());

      lessonHolder.titleTextView.setText(lessonTitle);

      lessonHolder.lessonNoView.setText(
          String.format(position < 11 ? "0%d." : "%d.", position + 1));

      lessonHolder.itemView.setOnClickListener(
          v -> {
            Intent intent = new Intent(context, LessonContentActivity.class);
            intent.putExtra("lesson_id", lesson.getId());
            context.startActivity(intent);
          });
    }
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  static class SectionViewHolder extends RecyclerView.ViewHolder {
    TextView sectionTitleTextView;
    ImageView sectionIcon;
    TextView sectionDescription;

    SectionViewHolder(@NonNull View itemView) {
      super(itemView);
      sectionTitleTextView = itemView.findViewById(R.id.section_title);
      sectionDescription = itemView.findViewById(R.id.description);
      sectionIcon = itemView.findViewById(R.id.section_icon);
    }
  }

  static class LessonViewHolder extends RecyclerView.ViewHolder {
    TextView titleTextView;
    TextView lessonNoView;

    LessonViewHolder(@NonNull View itemView) {
      super(itemView);
      titleTextView = itemView.findViewById(R.id.lessonTitle);
      lessonNoView = itemView.findViewById(R.id.lesson_no);
    }
  }
}
