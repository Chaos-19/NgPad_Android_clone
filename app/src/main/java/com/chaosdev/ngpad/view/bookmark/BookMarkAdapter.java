package com.chaosdev.ngpad.view.bookmark;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.model.main.BookMark;
import com.chaosdev.ngpad.model.main.Question;
import com.chaosdev.ngpad.view.lesson.LessonContentActivity;
import com.chaosdev.ngpad.view.quiz.OptionAdapter;
import java.util.ArrayList;
import java.util.List;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder> {
  private List<BookMark> bookmarks;
  private Context context;

  public BookMarkAdapter(Context context, List<BookMark> bookmarks) {
    this.bookmarks = bookmarks;
    this.context = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_card, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    BookMark bookmark = bookmarks.get(position);

    holder.lessonTitleTv.setText(bookmark.getTitle());
    holder.sectionTitleTv.setText(bookmark.getSectionTitle());

    holder.itemView.setOnClickListener(
        v -> {
          Intent intent = new Intent(context, LessonContentActivity.class);
          intent.putExtra("lesson_id", bookmark.getLessonId());
          intent.putExtra("bookmarked",true);      
          context.startActivity(intent);
        });
  }

  @Override
  public int getItemCount() {
    return bookmarks.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    TextView lessonTitleTv;
    TextView sectionTitleTv;
    ListView optionListV;

    ViewHolder(View itemView) {
      super(itemView);
      lessonTitleTv = itemView.findViewById(R.id.lessonTitle);
      sectionTitleTv = itemView.findViewById(R.id.sectionTitle);
    }
  }
}
