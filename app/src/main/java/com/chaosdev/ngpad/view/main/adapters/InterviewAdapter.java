package com.chaosdev.ngpad.view.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.model.main.Interview;
import com.chaosdev.ngpad.model.main.Quiz;
import com.chaosdev.ngpad.utils.SvgLoader;
import java.util.ArrayList;

public class InterviewAdapter extends ArrayAdapter<Interview> {
  private Context context;

  public InterviewAdapter(Context context, ArrayList<Interview> items) {
    super(context, 0, items);
    this.context = context;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View itemView = convertView;
    if (itemView == null) {
      itemView = LayoutInflater.from(getContext()).inflate(R.layout.quiz_card, parent, false);
    }

    Interview currentItem = getItem(position);
    ImageView imageView = itemView.findViewById(R.id.course_icon_h);

    if (currentItem != null) {
      SvgLoader.loadSvgFromUrl(context, imageView, currentItem.getIconUrl(), R.drawable.advwebdev);
    }

    return itemView;
  }
}
