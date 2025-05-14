package com.chaosdev.ngpad.view.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.model.main.Option;
import java.util.ArrayList;
import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {
  private final Context context;
  private final List<Option> options;
  private final int questionPosition;
  private int selectedPosition;
  private final OnOptionSelectedListener listener;

  public OptionAdapter(
      Context context,
      List<Option> options,
      int questionPosition,
      int selectedPosition,
      OnOptionSelectedListener listener) {
    this.context = context;
    this.options = options;
    this.questionPosition = questionPosition;
    this.selectedPosition = selectedPosition;
    this.listener = listener;
  }

  @NonNull
  @Override
  public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.option_item, parent, false);
    return new OptionViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
    Option option = options.get(position);
    holder.optionKey.setText(String.format("%s.",option.getKey().toUpperCase()));
    holder.optionText.setText(option.getText());

    // Clear listener to prevent interference
    holder.checkBox.setOnCheckedChangeListener(null);
    holder.checkBox.setChecked(position == selectedPosition);

    holder.checkBox.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          if (isChecked) {
            int previousSelected = selectedPosition;
            int newPosition = holder.getAdapterPosition();

            if (newPosition == RecyclerView.NO_POSITION) return;

            selectedPosition = newPosition;

            // Post notifications to avoid layout conflicts
            holder.itemView.post(
                () -> {
                  if (previousSelected != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousSelected);
                  }
                  notifyItemChanged(newPosition);
                });

            listener.onOptionSelected(questionPosition, newPosition, true);
          } else {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition == RecyclerView.NO_POSITION) return;

            selectedPosition = -1;

            holder.itemView.post(
                () -> {
                  notifyItemChanged(currentPosition);
                });

            listener.onOptionSelected(questionPosition, -1, false);
          }
        });
  }

  @Override
  public int getItemCount() {
    return options.size();
  }

  static class OptionViewHolder extends RecyclerView.ViewHolder {
    TextView optionKey;
    TextView optionText;
    CheckBox checkBox;

    public OptionViewHolder(@NonNull View itemView) {
      super(itemView);
      optionKey = itemView.findViewById(R.id.optionKey);
      optionText = itemView.findViewById(R.id.optionText);
      checkBox = itemView.findViewById(R.id.optionCheck);
    }
  }

  public interface OnOptionSelectedListener {
    void onOptionSelected(int questionPosition, int optionPosition, boolean isChecked);
  }
}

/*
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
*/
