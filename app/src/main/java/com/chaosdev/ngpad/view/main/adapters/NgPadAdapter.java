package com.chaosdev.ngpad.view.main.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.utils.StringUtils;
import com.chaosdev.ngpad.utils.SvgLoader;
import com.chaosdev.ngpad.view.course.CourseDetailActivity;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NgPadAdapter extends BaseExpandableListAdapter {
  private Context context;
  private List<Category> courses;

  // View type constants
  private static final int TYPE_VERTICAL_GROUP = 0;
  private static final int TYPE_HORIZONTAL_GROUP = 1;

  public NgPadAdapter(Context context, List<Category> courses) {
    this.context = context;
    this.courses = courses;
  }

  @Override
  public int getGroupCount() {
    return courses.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    // Return 0 for horizontal groups to disable expansion
    Category category = getGroup(groupPosition);
    return new ArrayList<Integer>(Arrays.asList(2, 5, 9)).contains(groupPosition)
        ? 0
        : category.getCourses().size();
  }

  @Override
  public Category getGroup(int groupPosition) {
    return courses.get(groupPosition);
  }

  @Override
  public Course getChild(int groupPosition, int childPosition) {
    return courses.get(groupPosition).getCourses().get(childPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public int getGroupType(int groupPosition) {
    return new ArrayList<Integer>(Arrays.asList(2, 5, 9)).contains(groupPosition)
        ? TYPE_HORIZONTAL_GROUP
        : TYPE_VERTICAL_GROUP;
  }

  @Override
  public int getGroupTypeCount() {
    return 2;
  }

  @Override
  public View getGroupView(
      int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    Category category = getGroup(groupPosition);
    int type = getGroupType(groupPosition);

    if (type == TYPE_VERTICAL_GROUP) {
      // Vertical expandable group
      VerticalGroupHolder holder;
      if (convertView == null || !(convertView.getTag() instanceof VerticalGroupHolder)) {
        convertView =
            LayoutInflater.from(context).inflate(R.layout.category_list_header, parent, false);
        holder = new VerticalGroupHolder(convertView);
        convertView.setTag(holder);
      } else {
        holder = (VerticalGroupHolder) convertView.getTag();
      }

      holder.title.setText(StringUtils.capitilizeStr(category.getName()));

      ImageView svgImageView = convertView.findViewById(R.id.icon_category);

      String imageUrl = category.getIconUrl() + "";
      svgImageView.setTag(imageUrl); // Set the tag before loading
      SvgLoader.loadSvgFromUrl(context, svgImageView, imageUrl, R.drawable.advwebdev);

      holder.indicator.setRotation(isExpanded ? 180 : 0);

    } else {
      // Horizontal scrolling group
      HorizontalGroupHolder holder;
      if (convertView == null || !(convertView.getTag() instanceof HorizontalGroupHolder)) {
        convertView =
            LayoutInflater.from(context).inflate(R.layout.category_horizontal, parent, false);
        holder = new HorizontalGroupHolder(convertView);
        convertView.setTag(holder);
      } else {
        holder = (HorizontalGroupHolder) convertView.getTag();
      }
      holder.title.setText(StringUtils.capitilizeStr(category.getName()));

      ImageView svgImageView = convertView.findViewById(R.id.icon_category_h);

      String imageUrl = category.getIconUrl() + "";
      svgImageView.setTag(imageUrl); // Set the tag before loading
      SvgLoader.loadSvgFromUrl(context, svgImageView, imageUrl, R.drawable.advwebdev);

      holder.recyclerView.addOnScrollListener(
          new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
              if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                Log.d("MainActivity", "Scroll stopped");
              }
            }
          });

      setupHorizontalRecyclerView(holder.recyclerView, category.getCourses());
    }
    return convertView;
  }

  @Override
  public View getChildView(
      int groupPosition,
      int childPosition,
      boolean isLastChild,
      View convertView,
      ViewGroup parent) {
    // Only used for vertical groups
    Course course = getChild(groupPosition, childPosition);
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.course_card, parent, false);
    }
    ImageView courseIcon = convertView.findViewById(R.id.course_icon);
    SvgLoader.loadSvgFromUrl(context, courseIcon, course.getIcon(), R.drawable.advwebdev);
    TextView courseTitleV = convertView.findViewById(R.id.childItem);

    TextView courseDescription = convertView.findViewById(R.id.description);
    courseDescription.setText(course.getDescription());

    String courseTitle = StringUtils.escapSpacialCharacter(course.getTitle());

    courseTitleV.setText(courseTitle);

    // Attach a click listener to the child view
    convertView.setOnClickListener(
        v -> {
          // Navigate to CourseDetailActivity
          Intent intent = new Intent(context, CourseDetailActivity.class);
          intent.putExtra("course_id", course.getId());
          context.startActivity(intent);
        });

    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  // Helper method to set up horizontal RecyclerView
  private void setupHorizontalRecyclerView(RecyclerView recyclerView, List<Course> courses) {
    recyclerView.setLayoutManager(
        new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    HorizontalCourseAdapter adapter = new HorizontalCourseAdapter(context, courses);
    recyclerView.setAdapter(adapter);
  }

  // ViewHolder for vertical groups
  private static class VerticalGroupHolder {
    TextView title;
    ImageView indicator;

    VerticalGroupHolder(View view) {
      title = view.findViewById(R.id.title);
      indicator = view.findViewById(R.id.indicator);
    }
  }

  // ViewHolder for horizontal groups
  private static class HorizontalGroupHolder {
    TextView title;
    RecyclerView recyclerView;

    HorizontalGroupHolder(View view) {
      title = view.findViewById(R.id.group_title);
      recyclerView = view.findViewById(R.id.horizontal_recycler);
    }
  }
}
