package com.chaosdev.ngpad.view.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.model.main.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;

import com.chaosdev.ngpad.model.main.NgPad;
import java.util.List;

public class NgPadAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Category> courses;

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
        return courses.get(groupPosition).getCourses().size();
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

    // Group (Course) view
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Category course = getGroup(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.category_list_header, parent, false);
        }

        TextView title = convertView.findViewById(R.id.title);
        ImageView indicator = convertView.findViewById(R.id.indicator);

        title.setText(course.getCategoryName());

        indicator.setRotation(isExpanded ? 180 : 0);

        return convertView;
    }

    // Child (Lesson) view
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        Course lesson = getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.course_card, parent, false);
        }

        TextView lessonTitle = convertView.findViewById(R.id.childItem);
        //TextView lessonType = convertView.findViewById(R.id.lesson_type);

        lessonTitle.setText(lesson.getCourseTitle());
        //lessonType.setText(lesson.getSubLessons().isEmpty() ? "Lesson" : "Section");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

/*
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import com.chaosdev.ngpad.R;

public class NgPadAdapter extends BaseExpandableListAdapter {

  private Context context;
  private HashMap<String, List<String>> childtitles;
  List<String> headertitles;

  public NgPadAdapter(
      Context context, List<String> headertitles, HashMap<String, List<String>> childtitles) {

    this.context = context;
    this.childtitles = childtitles;
    this.headertitles = headertitles;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return this.headertitles.get(groupPosition);
  }

  @Override
  public int getGroupCount() {
    return this.headertitles.size();
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public View getGroupView(
      int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    String listTitle = (String) getGroup(groupPosition);
    if (convertView == null) {
      LayoutInflater inflater =
          (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.category_list_header, null);
    }

    TextView title = convertView.findViewById(R.id.title);
    ImageView indicator = convertView.findViewById(R.id.indicator);
    title.setText(listTitle);

    // Rotate the arrow based on expansion state
    if (isExpanded) {
      indicator.setRotation(180); // Point upwards when expanded
    } else {
      indicator.setRotation(0); // Point downwards when collapsed
    }

    return convertView;
  }

  /*
  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
      String listTitle = (String) getGroup(groupPosition);

      if (convertView == null) {

          LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

          convertView = layoutInflater.inflate(R.layout.group_header, null);
      }

      TextView listTitleTextView = (TextView) convertView.findViewById(R.id.title);

      listTitleTextView.setText(listTitle);

      return convertView;
  }*

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return this.childtitles.get(this.headertitles.get(groupPosition)).get(childPosition);
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return this.childtitles.get(this.headertitles.get(groupPosition)).size();
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public View getChildView(
      int groupPosition,
      int childPosition,
      boolean isLastChild,
      View convertView,
      ViewGroup parent) {

    final String expandedListText = (String) getChild(groupPosition, childPosition);

    if (convertView == null) {

      LayoutInflater layoutInflater =
          (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      convertView = layoutInflater.inflate(R.layout.course_card, null);
    }

    TextView expandedListTextView = (TextView) convertView.findViewById(R.id.childItem);

    expandedListTextView.setText(expandedListText);

    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }
}
*/