package com.chaosdev.ngpad.view.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.model.main.Question;
import com.chaosdev.ngpad.model.main.UserQuizAnswer;
import com.chaosdev.ngpad.viewmodel.main.QuizViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizeDetailAdapter extends RecyclerView.Adapter<QuizeDetailAdapter.ViewHolder> {
  private final List<Question> questions;
  private final Context context;
  private QuizViewModel viewModel;
  private int outputDetailVisibility = View.GONE;   

  public QuizeDetailAdapter(Context context, List<Question> questions, QuizViewModel viewModel) {
    this.questions = questions;
    this.context = context;
    this.viewModel = viewModel;
  }

  public void setOutputDetailVisibility(int visibility) {
    this.outputDetailVisibility = visibility;
    notifyDataSetChanged(); 
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
    holder.questionNoTv.setText(String.format("%d.",position + 1));
    holder.questionTextTv.setText(question.getText());
    holder.outputDetailTv.setText(question.getOutput());

    // Set visibility from the stored value
    holder.outputDetailTv.setVisibility(outputDetailVisibility);


    Map<Integer, UserQuizAnswer> selectedOptions =viewModel.getUserAnswers().getValue();

    Integer selectedOptionIndex =
        selectedOptions.get(position) != null
            ? selectedOptions.get(position).selectedOptionIndex
            : null;

    OptionAdapter optionAdapter =
        new OptionAdapter(
            context,
            question.getOptions(),
            position,
            selectedOptionIndex != null ? selectedOptionIndex : -1,
            (questionPosition, optionPosition, isChecked) -> {
              String optionKey =
                  optionPosition >= 0 ? question.getOptions().get(optionPosition).getKey() : "";
              viewModel.setUserAnswer(
                  questionPosition, new UserQuizAnswer(optionKey, optionPosition), isChecked);
            });

    holder.optionRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    holder.optionRecyclerView.setAdapter(optionAdapter);
  }

  @Override
  public int getItemCount() {
    return questions.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    TextView questionNoTv;
    TextView questionTextTv;
    TextView outputDetailTv;    
    RecyclerView optionRecyclerView;

    ViewHolder(View itemView) {
      super(itemView);
      questionNoTv = itemView.findViewById(R.id.questionNo);
      questionTextTv = itemView.findViewById(R.id.questionText);
      outputDetailTv = itemView.findViewById(R.id.outputDetail);       
      optionRecyclerView = itemView.findViewById(R.id.optionList);
            
    }
  }
}
/*
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
*/
