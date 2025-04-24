package com.chaosdev.ngpad.utils;

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