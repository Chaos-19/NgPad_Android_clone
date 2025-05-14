package com.chaosdev.ngpad;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import com.chaosdev.ngpad.databinding.ActivityMainBinding;
import com.chaosdev.ngpad.ui.main.SectionsPagerAdapter;
import com.chaosdev.ngpad.utils.SvgLoader;
import com.chaosdev.ngpad.view.bookmark.BookMarkActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    SvgLoader.init(this);

    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
    NavigationView navView = findViewById(R.id.nav_view);

    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    drawerLayout.addDrawerListener(toggle);
    toggle.syncState(); // shows the icon and syncs its state

    SectionsPagerAdapter sectionsPagerAdapter =
        new SectionsPagerAdapter(this, getSupportFragmentManager());
    ViewPager viewPager = binding.viewPager;
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = binding.tabs;
    tabs.setupWithViewPager(viewPager);

    // Customize Settings item with expandable content
    MenuItem settingsItem = navView.getMenu().findItem(R.id.nav_settings);

    if (settingsItem != null) {
      View customView =
          LayoutInflater.from(this).inflate(R.layout.custom_menu_item, navView, false);
      TextView parentText = customView.findViewById(R.id.parent_text);
      LinearLayout subItemsContainer = customView.findViewById(R.id.sub_items_container);
      SeekBar seekBar = customView.findViewById(R.id.seek_bar_font);
      ToggleButton toggleTheme = customView.findViewById(R.id.toggle_theme);

      parentText.setText(settingsItem.getTitle());
      parentText.setOnClickListener(
          v -> {
            boolean isVisible = subItemsContainer.getVisibility() == View.VISIBLE;
            subItemsContainer.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            // Log or debug to confirm toggle
            System.out.println("SubItemsContainer visibility: " + (isVisible ? "GONE" : "VISIBLE"));
          });

      // Handle SeekBar (Font Size)
      if (seekBar != null) {
        seekBar.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
              @Override
              public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                parentText.setTextSize(12 + progress); // Example: Adjust text size
                System.out.println("SeekBar progress: " + progress);
              }

              @Override
              public void onStartTrackingTouch(SeekBar seekBar) {}

              @Override
              public void onStopTrackingTouch(SeekBar seekBar) {}
            });
      } else {
        System.out.println("SeekBar is null!");
      }

      // Handle ToggleButton (Theme)
      toggleTheme.setOnCheckedChangeListener(
          (buttonView, isChecked) -> {
            // Implement theme change logic here
            if (isChecked) {
              // Apply light theme
            } else {
              // Apply dark theme
            }
          });

      settingsItem.setActionView(customView);
    }

    // Handle menu item clicks
    navView.setNavigationItemSelectedListener(
        item -> {
          int id = item.getItemId();
          switch (id) {
            case R.id.nav_bookmark:
              Intent intent = new Intent(this, BookMarkActivity.class);
              this.startActivity(intent);
              break;
          }

          drawerLayout.closeDrawer(GravityCompat.START);
          return true;
        });
  }

  /*

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    SvgLoader.init(this);

    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

    NavigationView navView = findViewById(R.id.nav_view);

    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);

    drawerLayout.addDrawerListener(toggle);
    toggle.syncState(); // shows the icon and syncs its state

    SectionsPagerAdapter sectionsPagerAdapter =
        new SectionsPagerAdapter(this, getSupportFragmentManager());
    ViewPager viewPager = binding.viewPager;
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = binding.tabs;
    tabs.setupWithViewPager(viewPager);

    // Customize Settings item with expandable content
    MenuItem settingsItem = navView.getMenu().findItem(R.id.nav_settings);

    if (settingsItem != null) {
      View customView =
          LayoutInflater.from(this).inflate(R.layout.custom_menu_item, navView, false);
      TextView parentText = customView.findViewById(R.id.parent_text);
      LinearLayout subItemsContainer = customView.findViewById(R.id.sub_items_container);
      SeekBar seekBar = customView.findViewById(R.id.seek_bar_font);
      ToggleButton toggleTheme = customView.findViewById(R.id.toggle_theme);

      parentText.setText(settingsItem.getTitle());
      parentText.setOnClickListener(
          v -> {
            subItemsContainer.setVisibility(
                subItemsContainer.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
          });

      // Handle SeekBar (Font Size)
      seekBar.setOnSeekBarChangeListener(
          new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
              parentText.setTextSize(12 + progress); // Example: Adjust text size
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
          });

      // Handle ToggleButton (Theme)
      toggleTheme.setOnCheckedChangeListener(
          (buttonView, isChecked) -> {
            // Implement theme change logic here
            if (isChecked) {
              // Apply light theme
            } else {
              // Apply dark theme
            }
          });

      settingsItem.setActionView(customView);
    }

    // Handle menu item clicks
    navView.setNavigationItemSelectedListener(
        item -> {
          int id = item.getItemId();
          if (id == R.id.nav_like) {
            // Handle Like Us
          } else if (id == R.id.nav_share) {
            // Handle Share App
          }
          drawerLayout.closeDrawer(GravityCompat.START);
          return true;
        });
  }
    */

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (isFinishing()) { // Check if the Activity is finishing (app is closing)
      SvgLoader.shutdown();
    }
  }
}
