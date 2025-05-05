package com.chaosdev.ngpad.view.lesson;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.chaosdev.markdown.MyGrammarLocator;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.data.repository.NgPadRepository;
import com.chaosdev.ngpad.utils.StringUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import io.noties.markwon.syntax.Prism4jThemeDefault;
import io.noties.markwon.syntax.SyntaxHighlightPlugin;
import io.noties.prism4j.Prism4j;
import org.commonmark.node.Node;

public class LessonContentActivity extends AppCompatActivity {
  private NgPadRepository repository;
  private int lessonContentId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lesson_content);

    FloatingActionButton fab = findViewById(R.id.backButton);

    fab.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // Use the FragmentManager to handle back navigation if you're in a Fragment
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
              getSupportFragmentManager().popBackStack();
            } else {
              // If not in a Fragment or no back stack entries, handle activity back
              onBackPressed();
            }
          }
        });

    TextView contentTectV = findViewById(R.id.lesson_content);
    TextView lessonTitleV = findViewById(R.id.lesson_title);

    // Initialize repository
    repository = NgPadRepository.getInstance(this);

    lessonContentId = getIntent().getIntExtra("lesson_id", -1);

    contentTectV.setText(String.valueOf(lessonContentId));

    final Prism4j prism4j = new Prism4j(new MyGrammarLocator());
    final Prism4jTheme prism4jTheme = Prism4jThemeDarkula.create();

    Markwon markwon =
        Markwon.builder(this)
            //.usePlugin(io.noties.markwon.html.HtmlPlugin.create()) // for HTML
            .usePlugin(SyntaxHighlightPlugin.create(prism4j, prism4jTheme))
            .usePlugin(TablePlugin.create(this))
            .usePlugin(TaskListPlugin.create(this))
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(SimpleExtPlugin.create())
            .usePlugin(ImagesPlugin.create())
            .usePlugin(MarkwonInlineParserPlugin.create())
            .build();

    repository.getLessonByID(
        lessonContentId,
        (lesson) -> {
          // contentTectV.setText(String.valueOf(lesson.getContent()));

          String escapedTitle =
              Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                  ? Html.fromHtml(lesson.getTitle(), Html.FROM_HTML_MODE_LEGACY).toString()
                  : Html.fromHtml(lesson.getTitle()).toString();

          // parse markdown to commonmark-java Node
          final Node node = markwon.parse(lesson.getContent());

          // create styled text from parsed Node
          final Spanned markdown = markwon.render(node);

          // use it on a TextView
          markwon.setParsedMarkdown(contentTectV, markdown);
          lessonTitleV.setText(escapedTitle);
        });
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
