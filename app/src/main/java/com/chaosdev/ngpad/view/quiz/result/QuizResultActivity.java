package com.chaosdev.ngpad.view.quiz.result;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import com.chaosdev.ngpad.databinding.ActivityQuizResultBinding;
import com.chaosdev.ngpad.view.main.QuizViewModelFactory;
import com.chaosdev.ngpad.viewmodel.main.QuizViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.chaosdev.ngpad.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizResultActivity extends AppCompatActivity {
  private ActivityQuizResultBinding binding;
  private PieChart pieChart;
  private int[] resultData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityQuizResultBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    Toolbar toolbar = binding.toolbar;
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
      getSupportActionBar().setTitle("Quiz Results");
    }

    pieChart = binding.pieChart;
    TextView gobackTv = binding.goBackaBtn;

    int[] resultData = getIntent().getIntArrayExtra("quizResult");
    Toast.makeText(this, "resultData : " + resultData.length, Toast.LENGTH_LONG).show();

    int correctAnswers = resultData[0];
    int incorrectAnswers = resultData[1];
    int totalQuestions = resultData[2];
    int totalScore = resultData[3];

    setupPieChart(correctAnswers, incorrectAnswers, totalQuestions, totalScore);

    binding.shareButton.setOnClickListener(
        v -> {
          // Capture screenshot of the root view
          View rootView = binding.getRoot();
          rootView.setDrawingCacheEnabled(true);
          Bitmap screenshot = Bitmap.createBitmap(rootView.getDrawingCache());
          rootView.setDrawingCacheEnabled(false);

          // Save screenshot to a temporary file
          File imageFile = new File(getExternalCacheDir(), "quiz_result_screenshot.png");
          try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
            screenshot.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
          } catch (IOException e) {
            Log.e("QuizResult", "Failed to save screenshot", e);
            Toast.makeText(this, "Failed to share screenshot", Toast.LENGTH_SHORT).show();
            return;
          }

          // Create share intent with image URI
          Uri imageUri =
              FileProvider.getUriForFile(this, "com.chaosdev.ngpad.fileprovider", imageFile);
          Intent shareIntent = new Intent(Intent.ACTION_SEND);
          shareIntent.setType("image/png");
          shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
          shareIntent.putExtra(
              Intent.EXTRA_TEXT, "I scored " + totalScore + "/" + totalQuestions + " on the quiz!");
          shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

          // Start share activity
          startActivity(Intent.createChooser(shareIntent, "Share Your Progress"));
        });

    gobackTv.setOnClickListener(
        (v) -> {
          onBackPressed();
        });
  }

  private void setupPieChart(
      int correctAnswers, int incorrectAnswers, int totalQuestions, int totalScore) {

    if (totalQuestions == 0) {
      Toast.makeText(this, "No quiz data available", Toast.LENGTH_SHORT).show();
      return;
    }

    // Calculate percentage score
    float percentage = (correctAnswers * 100.0f) / totalQuestions;
    int percentageInt = Math.round(percentage);

    // Get quiz topic from Intent (default to "Quiz" if not provided)
    String quizTopic = getIntent().getStringExtra("quizTopic");
    if (quizTopic == null) {
      quizTopic = "Quiz";
    }

    // Select performance analysis string based on percentage
    String performanceText;
    if (percentageInt <= 40) {
      performanceText = getString(R.string.performance_poor, percentageInt, quizTopic);
    } else if (percentageInt <= 60) {
      performanceText = getString(R.string.performance_average, percentageInt, quizTopic);
    } else if (percentageInt <= 80) {
      performanceText = getString(R.string.performance_good, percentageInt, quizTopic);
    } else {
      performanceText = getString(R.string.performance_excellent, percentageInt, quizTopic);
    }

    // Set performance text in TextView
    TextView performanceTextView = binding.performanceText;
    performanceTextView.setText(performanceText);

    // Calculate unanswered questions (if any)
    int unansweredQuestions = totalQuestions - (correctAnswers + incorrectAnswers);

    ArrayList<PieEntry> entries = new ArrayList<>();
    if (correctAnswers > 0) {
      entries.add(new PieEntry(correctAnswers, "Correct"));
    }
    if (incorrectAnswers > 0) {
      entries.add(new PieEntry(incorrectAnswers, "Incorrect"));
    }
    if (unansweredQuestions > 0) {
      entries.add(new PieEntry(unansweredQuestions, "Unanswered"));
    }

    if (entries.isEmpty()) {
      Toast.makeText(this, "No valid quiz data to display", Toast.LENGTH_SHORT).show();
      return;
    }

    PieDataSet dataSet = new PieDataSet(entries, "Quiz Results");
    dataSet.setColors(
        new int[] {
          ContextCompat.getColor(this, android.R.color.holo_green_light),
          ContextCompat.getColor(this, android.R.color.holo_red_light),
          ContextCompat.getColor(this, android.R.color.darker_gray)
        });

    dataSet.setValueTextSize(14f);
    dataSet.setValueTextColor(getThemeColor(R.color.colorPrimaryVariant));
    dataSet.setSliceSpace(3f);
    dataSet.setSelectionShift(10f);

    PieData pieData = new PieData(dataSet);
    pieData.setValueFormatter(new PercentFormatter(pieChart));
    pieChart.setData(pieData);

    pieChart.getDescription().setEnabled(false);
    pieChart.setCenterText("Score: " + totalScore + "/" + totalQuestions);
    pieChart.setCenterTextSize(18f);
    pieChart.setCenterTextColor(getThemeColor(android.R.attr.colorPrimary));
    pieChart.setDrawHoleEnabled(true);
    pieChart.setHoleColor(ContextCompat.getColor(this, android.R.color.transparent));
    pieChart.setHoleRadius(40f);
    pieChart.setTransparentCircleRadius(45f);
    pieChart.setUsePercentValues(true);
    pieChart.getLegend().setEnabled(false);
    pieChart.setEntryLabelColor(getThemeColor(R.color.colorPrimaryVariant));
    pieChart.setEntryLabelTextSize(12f);
    pieChart.animateY(1000);
    pieChart.invalidate();
  }

  private int getThemeColor(int attr) {
    android.content.res.TypedArray a = getTheme().obtainStyledAttributes(new int[] {attr});
    int color = a.getColor(0, ContextCompat.getColor(this, android.R.color.black));
    a.recycle();
    return color;
  }

  @Override
  public boolean onSupportNavigateUp() {
    finish();
    return true;
  }
}
