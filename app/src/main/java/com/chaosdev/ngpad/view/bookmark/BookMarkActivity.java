package com.chaosdev.ngpad.view.bookmark;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chaosdev.ngpad.databinding.ActivityBookmarkBinding;
import com.chaosdev.ngpad.model.main.BookMark;
import com.chaosdev.ngpad.view.main.BookMarkViewModelFactory;
import com.chaosdev.ngpad.viewmodel.main.BookMarkViewModel;
import com.chaosdev.ngpad.viewmodel.main.SharedViewModelStoreOwner;

public class BookMarkActivity extends AppCompatActivity {
  private ActivityBookmarkBinding binding;

  private BookMarkViewModel viewModel;
  private RecyclerView recycleView;
  private BookMarkAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityBookmarkBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    Toolbar toolbar = binding.toolbar;
    setSupportActionBar(toolbar);

    recycleView = binding.recyclerViewBookmarks;
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    recycleView.setLayoutManager(layoutManager);
        
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setTitle("BookMarks");
    }

    // Use SharedViewModelStoreOwner for shared ViewModel
    BookMarkViewModelFactory factory = new BookMarkViewModelFactory(getApplication());
    viewModel =
        new ViewModelProvider(
                SharedViewModelStoreOwner.getInstance(), // Use shared ViewModelStoreOwner
                factory)
            .get(BookMarkViewModel.class);

    viewModel
        .getBookMarks()
        .observe(
            this,
            (bookmarks) -> {
              if (!bookmarks.isEmpty()) {
                    Toast.makeText(
                      this,
                      "BookMark Length : " + bookmarks.size(),
                      Toast.LENGTH_LONG)
                  .show();
                adapter = new BookMarkAdapter(this, bookmarks);
                recycleView.setAdapter(adapter);
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
