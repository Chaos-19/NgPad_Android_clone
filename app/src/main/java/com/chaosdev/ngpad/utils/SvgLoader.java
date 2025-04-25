package com.chaosdev.ngpad.utils;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SvgLoader {
    private static final String TAG = "SvgLoader";
    private static final int THREAD_POOL_SIZE = 4; // Adjust based on needs
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private static final Map<String, PictureDrawable> svgCache = new HashMap<>();
    private static final int CONNECT_TIMEOUT = 5000; // 5 seconds
    private static final int READ_TIMEOUT = 5000; // 5 seconds

    /**
     * Loads an SVG from a URL or assets and displays it in the provided ImageView.
     * @param context The application context for accessing resources.
     * @param imageView The ImageView to display the SVG.
     * @param url The URL or asset path of the SVG (e.g., HTTP URL or "image.svg" for assets).
     * @param placeholderResId Resource ID for the placeholder/error image.
     */
    public static void loadSvgFromUrl(final Context context, final ImageView imageView,
                                      final String url, final int placeholderResId) {
        if (url == null || url.isEmpty()) {
            Log.e(TAG, "Invalid URL: null or empty");
            setPlaceholder(imageView, context, placeholderResId);
            return;
        }

        // Clear previous image and set tag
        imageView.setImageResource(0);
        imageView.setTag(url);

        // Check cache
        if (svgCache.containsKey(url)) {
            PictureDrawable cachedDrawable = svgCache.get(url);
            if (cachedDrawable != null && url.equals(imageView.getTag())) {
                imageView.setImageDrawable(cachedDrawable);
            }
            return;
        }

        // Load SVG asynchronously
        executorService.execute(() -> {
            PictureDrawable drawable = loadSvg(context, url);
            imageView.post(() -> {
                if (url.equals(imageView.getTag())) {
                    if (drawable != null) {
                        imageView.setImageDrawable(drawable);
                    } else {
                        setPlaceholder(imageView, context, placeholderResId);
                    }
                }
            });
        });
    }

    private static PictureDrawable loadSvg(Context context, String url) {
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            if (url.startsWith("http")) {
                // Load from URL
                URL imageUrl = new URL(url);
                connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setConnectTimeout(CONNECT_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setDoInput(true);
                connection.connect();
                inputStream = connection.getInputStream();
            } else {
                // Load from assets
                inputStream = context.getAssets().open(url);
            }

            SVG svg = SVG.getFromInputStream(inputStream);
            PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());

            // Cache the drawable
            synchronized (svgCache) {
                svgCache.put(url, drawable);
            }

            return drawable;

        } catch (FileNotFoundException e) {
            Log.e(TAG, "SVG not found at URL: " + url, e);
            return null;
        } catch (IOException e) {
            Log.e(TAG, "IO error loading SVG: " + url, e);
            return null;
        } catch (SVGParseException e) {
            Log.e(TAG, "Failed to parse SVG: " + url, e);
            return null;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "SVG rendering error (likely Canvas save flag issue): " + url, e);
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error loading SVG: " + url, e);
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Log.w(TAG, "Failed to close input stream", e);
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static void setPlaceholder(ImageView imageView, Context context, int placeholderResId) {
        if (placeholderResId != 0) {
            Drawable placeholder = ContextCompat.getDrawable(context, placeholderResId);
            imageView.setImageDrawable(placeholder);
        }
    }

    /**
     * Clears the SVG cache to free memory.
     */
    public static void clearCache() {
        synchronized (svgCache) {
            svgCache.clear();
        }
    }

    /**
     * Shuts down the executor service. Call when the app is destroyed.
     */
    public static void shutdown() {
        executorService.shutdown();
    }
}
/*
import android.graphics.drawable.PictureDrawable;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SvgLoader {

  private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
  private static final Map<String, PictureDrawable> svgCache = new HashMap<>();

  public static void loadSvgFromUrl(final ImageView imageView, final String url) {

    imageView.setImageResource(0); // Optional: Clear previous image
    imageView.setTag(url); // Set the URL as a tag for identification

    // Check if the SVG is already in the cache
    if (svgCache.containsKey(url)) {
      imageView.setImageDrawable(svgCache.get(url));
      return; // No need for a network request
    }

    executorService.execute(
        new Runnable() {
          @Override
          public void run() {
            try {
              URL imageUrl = new URL(url);
              HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
              connection.setDoInput(true);
              connection.connect();
              InputStream inputStream = connection.getInputStream();
              SVG svg = SVG.getFromInputStream(inputStream);
              final PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());

              // Cache the downloaded SVG
              svgCache.put(url, drawable);

              imageView.post(
                  new Runnable() {
                    @Override
                    public void run() {
                      if (url.equals(imageView.getTag())) {
                        imageView.setImageDrawable(drawable);
                      }
                    }
                  });

              inputStream.close();
              connection.disconnect();

            } catch (Exception e) {
              e.printStackTrace();
              // Optionally set a placeholder or error image using imageView.post(...)
            }
          }
        });
  }
}

/*
import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SvgLoader {

  private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

  public static void loadSvgFromUrl(final ImageView imageView, final String url) {

    imageView.setImageResource(0); // Optional: Clear previous image

    executorService.execute(
        new Runnable() {
          @Override
          public void run() {
            try {
              URL imageUrl = new URL(url);
              HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
              connection.setDoInput(true);
              connection.connect();
              InputStream inputStream = connection.getInputStream();
              SVG svg = SVG.getFromInputStream(inputStream);
              final PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());

              imageView.post(
                  new Runnable() {
                    @Override
                    public void run() {
                      if (url.equals(imageView.getTag())) {
                        imageView.setImageDrawable(drawable);
                      }
                      // imageView.setImageDrawable(drawable);
                    }
                  });

              inputStream.close();
              connection.disconnect();

            } catch (Exception e) {
              e.printStackTrace();
              // Optionally set a placeholder or error image using imageView.post(...)
            }
          }
        });
  }
}
*/