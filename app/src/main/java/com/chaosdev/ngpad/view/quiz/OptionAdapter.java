package com.chaosdev.ngpad.view.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.chaosdev.ngpad.model.main.Option;
import com.chaosdev.ngpad.R;
import java.util.ArrayList;

public class OptionAdapter extends ArrayAdapter<Option> {

  public OptionAdapter(Context context, ArrayList<Option> items) {
    super(context,0,items);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View itemView = convertView;
    if (itemView == null) {
      itemView = LayoutInflater.from(getContext()).inflate(R.layout.option_item, parent, false);
    }

    Option currentItem = getItem(position);

    TextView optionKeyTv = itemView.findViewById(R.id.optionKey);
    TextView optionTextTv = itemView.findViewById(R.id.optionText);
    CheckBox optionCbV = itemView.findViewById(R.id.optionCheck);

    if (currentItem != null) {
      optionKeyTv.setText(currentItem.getKey().toUpperCase());
      optionTextTv.setText(currentItem.getText());
    }

    return itemView;
  }
}
