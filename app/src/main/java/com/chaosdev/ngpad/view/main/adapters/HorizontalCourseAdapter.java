package com.chaosdev.ngpad.view.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.utils.SvgLoader;
import com.chaosdev.ngpad.view.course.CourseDetailActivity;
import java.util.List;

public class HorizontalCourseAdapter
    extends RecyclerView.Adapter<HorizontalCourseAdapter.ViewHolder> {
  private List<Course> courses;
  private Context context;

  public HorizontalCourseAdapter(Context context, List<Course> courses) {
    this.courses = courses;
    this.context = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.course_card_horizontal, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Course course = courses.get(position);
    holder.textView.setText(course.getTitle());

    holder.textDescription.setText(course.getDescription());

    SvgLoader.loadSvgFromUrl(
        this.context, holder.courseIcon, course.getIcon(), R.drawable.advwebdev);

    holder.itemView.setOnClickListener(
        v -> {
          // Navigate to CourseDetailActivity
          Intent intent = new Intent(context, CourseDetailActivity.class);
          intent.putExtra("course_id", course.getId());
          context.startActivity(intent);
        });
  }

  @Override
  public int getItemCount() {
    return courses.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    TextView textDescription;
    ImageView courseIcon;

    ViewHolder(View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.h_child_text);
      textDescription = itemView.findViewById(R.id.description);
      courseIcon = itemView.findViewById(R.id.course_icon_h);
    }
  }
}
