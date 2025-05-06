package com.chaosdev.ngpad.view.interview;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.chaosdev.markdown.MyGrammarLocator;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.model.main.InterviewQuestion;
import com.chaosdev.ngpad.utils.StringUtils;
import io.noties.markwon.Markwon;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.ext.tasklist.TaskListPlugin;
import io.noties.markwon.image.ImagesPlugin;
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin;
import io.noties.markwon.linkify.LinkifyPlugin;
import io.noties.markwon.simple.ext.SimpleExtPlugin;
import io.noties.markwon.syntax.Prism4jTheme;
import io.noties.markwon.syntax.Prism4jThemeDarkula;
import io.noties.markwon.syntax.SyntaxHighlightPlugin;
import io.noties.prism4j.Prism4j;
import java.util.List;
import org.commonmark.node.Node;

public class InterviewDetailAdapter extends BaseExpandableListAdapter {

  private List<InterviewQuestion> interviewQuestions;
  private Context context;

  public InterviewDetailAdapter(Context context, List<InterviewQuestion> questions) {
    this.context = context;
    this.interviewQuestions = questions;
  }

  @Override
  public int getGroupCount() {
    return interviewQuestions.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return 1;
  }

  @Override
  public InterviewQuestion getGroup(int position) {
    return interviewQuestions.get(position);
  }

  @Override
  public InterviewQuestion getChild(int groupPosition, int childPosition) {
    return interviewQuestions.get(groupPosition);
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
  public View getGroupView(
      int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

    InterviewQuestion interviewQ = getGroup(groupPosition);

    GroupHeaderHolder holder;
    if (convertView == null) {
      convertView =
          LayoutInflater.from(context).inflate(R.layout.interview_list_header, parent, false);

      holder = new GroupHeaderHolder(convertView);
      convertView.setTag(holder);
    } else {
      holder = (GroupHeaderHolder) convertView.getTag();
    }

    holder.title.setText(StringUtils.escapSpacialCharacter(interviewQ.getInterviewQuestion()));
    holder.indicator.setRotation(isExpanded ? 180 : 0);
        
    return convertView;
  }

  @Override
  public View getChildView(
      int groupPosition,
      int childPosition,
      boolean isLastChild,
      View convertView,
      ViewGroup parent) {

    InterviewQuestion interviewQ = getChild(groupPosition, childPosition);
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.interview_answer, parent, false);
    }
    TextView interviewQAnswerTv = convertView.findViewById(R.id.interviewAnswer);

    final Prism4j prism4j = new Prism4j(new MyGrammarLocator());
    final Prism4jTheme prism4jTheme = Prism4jThemeDarkula.create();

    Markwon markwon =
        Markwon.builder(context)
            .usePlugin(SyntaxHighlightPlugin.create(prism4j, prism4jTheme))
            .usePlugin(TablePlugin.create(context))
            .usePlugin(TaskListPlugin.create(context))
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(SimpleExtPlugin.create())
            .usePlugin(ImagesPlugin.create())
            .usePlugin(MarkwonInlineParserPlugin.create())
            .build();

    final Node node = markwon.parse(interviewQ.getAnswer());
    final Spanned markdown = markwon.render(node);

    markwon.setParsedMarkdown(interviewQAnswerTv, markdown);

    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  private static class GroupHeaderHolder {
    TextView title;
    ImageView indicator;
    TextView interviwNo;

    GroupHeaderHolder(View view) {
      interviwNo = view.findViewById(R.id.interviwNo);
      title = view.findViewById(R.id.title);
      indicator = view.findViewById(R.id.indicator);
    }
  }
}
