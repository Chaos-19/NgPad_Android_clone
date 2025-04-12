package com.chaosdev.ngpad;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class SplashActivity extends AppCompatActivity {
  private SplashViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_splash);
    super.onCreate(savedInstanceState);

    // Initialize ViewModel
    viewModel = new ViewModelProvider(this).get(SplashViewModel.class);

    // Start initialization checks
    viewModel.checkInitialization();

    // Observe initialization status
    viewModel
        .getIsInitialized()
        .observe(
            this,
            isReady -> {
              if (isReady != null && isReady) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
              }
            });
  }
}
