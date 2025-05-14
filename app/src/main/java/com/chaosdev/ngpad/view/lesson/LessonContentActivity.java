package com.chaosdev.ngpad.view.lesson;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import com.chaosdev.markdown.MyGrammarLocator;
import com.chaosdev.ngpad.R;
import com.chaosdev.ngpad.data.repository.NgPadRepository;
import com.chaosdev.ngpad.model.main.BookMark;
import com.chaosdev.ngpad.utils.StringUtils;
import com.chaosdev.ngpad.view.main.BookMarkViewModelFactory;
import com.chaosdev.ngpad.viewmodel.main.BookMarkViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.chaosdev.ngpad.viewmodel.main.SharedViewModelStoreOwner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import io.noties.markwon.Markwon;
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.ext.tasklist.TaskListPlugin;
import io.noties.markwon.image.AsyncDrawable;
import io.noties.markwon.image.ImagesPlugin;
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin;
import io.noties.markwon.linkify.LinkifyPlugin;
import io.noties.markwon.simple.ext.SimpleExtPlugin;
import io.noties.markwon.syntax.Prism4jTheme;
import io.noties.markwon.syntax.Prism4jThemeDarkula;
import io.noties.markwon.syntax.Prism4jThemeDefault;
import io.noties.markwon.syntax.SyntaxHighlightPlugin;
import io.noties.prism4j.Prism4j;
import java.util.List;
import org.commonmark.node.Node;

public class LessonContentActivity extends AppCompatActivity {
  private NgPadRepository repository;
  private int lessonContentId;
  private List<BookMark> bookmarkLessons;
  private BookMarkViewModel viewModel;

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

    // Use SharedViewModelStoreOwner for shared ViewModel
    BookMarkViewModelFactory factory = new BookMarkViewModelFactory(getApplication());
    viewModel =
        new ViewModelProvider(
                SharedViewModelStoreOwner.getInstance(), // Use shared ViewModelStoreOwner
                factory)
            .get(BookMarkViewModel.class);

    TextView contentTectV = findViewById(R.id.lesson_content);
    TextView lessonTitleV = findViewById(R.id.lesson_title);
    ImageView bookMarkBtn = findViewById(R.id.bookmarkBtn);

    // Initialize repository
    repository = NgPadRepository.getInstance(this);

    lessonContentId = getIntent().getIntExtra("lesson_id", -1);
    boolean fromBookmarked = getIntent().getBooleanExtra("bookmarked", false);

    bookMarkBtn.setVisibility(fromBookmarked ? View.GONE : View.VISIBLE);

    contentTectV.setText(String.valueOf(lessonContentId));

    final Prism4j prism4j = new Prism4j(new MyGrammarLocator());
    final Prism4jTheme prism4jTheme = Prism4jThemeDarkula.create();

    Markwon markwon =
        Markwon.builder(this)
            .usePlugin(SyntaxHighlightPlugin.create(prism4j, prism4jTheme))
            .usePlugin(TablePlugin.create(this))
            .usePlugin(TaskListPlugin.create(this))
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(SimpleExtPlugin.create())
            .usePlugin(
                ImagesPlugin.create(
                    new ImagesPlugin.ImagesConfigure() {
                      @Override
                      public void configureImages(@NonNull ImagesPlugin plugin) {
                        plugin.placeholderProvider(
                            new ImagesPlugin.PlaceholderProvider() {
                              @Override
                              public Drawable providePlaceholder(@NonNull AsyncDrawable drawable) {
                                return ContextCompat.getDrawable(
                                    LessonContentActivity.this, R.drawable.placeholder);
                              }
                            });
                      }
                    }))
            .usePlugin(MarkwonInlineParserPlugin.create())
            .build();

    repository.getLessonByID(
        lessonContentId,
        (lesson) -> {
          String escapedTitle = StringUtils.escapSpacialCharacter(lesson.getTitle());

          // parse markdown to commonmark-java Node
          final Node node = markwon.parse(lesson.getContent());

          // create styled text from parsed Node
          final Spanned markdown = markwon.render(node);

          // use it on a TextView
          markwon.setParsedMarkdown(contentTectV, markdown);
          lessonTitleV.setText(escapedTitle);
        });

    bookMarkBtn.setOnClickListener(
        v -> {
          boolean test = false;
          viewModel.addLessonToBookMarks(lessonContentId);
        });

    viewModel.getIsLoading().observe(this, (isLoading) -> {});

    viewModel.getError().observe(this, (error) -> {});

    viewModel
        .getBookMarks()
        .observe(
            this,
            (bookmarks) -> {
              if (!bookmarks.isEmpty()) {

                boolean bookMarkEx =
                    bookmarks.stream().anyMatch(b -> b.getLessonId() == lessonContentId);

                if (bookMarkEx) {
                  bookMarkBtn.setImageResource(R.drawable.ic_bookmarked);

                } else {
                  bookMarkBtn.setImageResource(R.drawable.ic_bookmark);
                }
              }
            });
    viewModel.fetchBookMarks();
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
