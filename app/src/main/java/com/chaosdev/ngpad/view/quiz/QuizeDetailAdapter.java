package com.chaosdev.ngpad.view.quiz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Question;
import com.chaosdev.ngpad.utils.SvgLoader;
import com.chaosdev.ngpad.view.course.CourseDetailActivity;
import java.util.ArrayList;
import java.util.List;

public class QuizeDetailAdapter extends RecyclerView.Adapter<QuizeDetailAdapter.ViewHolder> {
  private List<Question> questions;
  private Context context;

  public QuizeDetailAdapter(Context context, List<Question> questions) {
    this.questions = questions;
    this.context = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Question question = questions.get(position);

    holder.questionNoTv.setText(String.valueOf(position + 1));
    holder.questionTextTv.setText(question.getText());

    Toast.makeText(
            context,
            "QuizeDetailAdapter Option :" + question.getOptions().size(),
            Toast.LENGTH_LONG)
        .show();
    OptionAdapter optionAdapter = new OptionAdapter(context, (ArrayList) question.getOptions());

    holder.optionListV.setAdapter(optionAdapter);
  }

  @Override
  public int getItemCount() {
    return questions.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    TextView questionNoTv;
    TextView questionTextTv;
    ListView optionListV;

    ViewHolder(View itemView) {
      super(itemView);
      questionNoTv = itemView.findViewById(R.id.questionNo);
      questionTextTv = itemView.findViewById(R.id.questionText);
      optionListV = itemView.findViewById(R.id.optionList);
    }
  }
}
