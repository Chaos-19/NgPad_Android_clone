package com.chaosdev.ngpad.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.chaosdev.ngpad.data.database.AppDatabase;
import com.chaosdev.ngpad.data.database.NgPadDao;
import com.chaosdev.ngpad.model.SvgCacheEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SvgLoader {
    private static final String TAG = "SvgLoader";
    private static final int THREAD_POOL_SIZE = 4;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final Map<String, PictureDrawable> svgCache = new HashMap<>();
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 5000;
    private static NgPadDao ngPadDao;

    public static void init(Context context) {
        ngPadDao = AppDatabase.getInstance(context).ngPadDao();
    }

    public static void loadSvgFromUrl(final Context context, final ImageView imageView,
                                      final String url, final int placeholderResId) {
        if (url == null || url.isEmpty()) {
            Log.e(TAG, "Invalid URL: null or empty");
            setPlaceholder(imageView, context, placeholderResId);
            return;
        }

        imageView.setImageResource(0);
        imageView.setTag(url);

        if (svgCache.containsKey(url)) {
            PictureDrawable cachedDrawable = svgCache.get(url);
            if (cachedDrawable != null && url.equals(imageView.getTag())) {
                imageView.setImageDrawable(cachedDrawable);
            }
            return;
        }

        executorService.execute(() -> {
            PictureDrawable drawable = loadSvg(context, url);
            mainHandler.post(() -> {
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
        SvgCacheEntry cacheEntry = null;
        try {
            cacheEntry = ngPadDao.getSvgCacheEntry(url);
        } catch (Exception e) {
            Log.e(TAG, "Error querying SVG cache: " + url, e);
        }

        if (cacheEntry != null) {
            File svgFile = new File(cacheEntry.getFilePath());
            if (svgFile.exists()) {
                try (FileInputStream inputStream = new FileInputStream(svgFile)) {
                    SVG svg = SVG.getFromInputStream(inputStream);
                    PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());
                    synchronized (svgCache) {
                        svgCache.put(url, drawable);
                    }
                    return drawable;
                } catch (IOException | SVGParseException e) {
                    Log.e(TAG, "Error loading cached SVG from file: " + url, e);
                }
            }
        }

        InputStream inputStream = null;
        HttpURLConnection connection = null;
        FileOutputStream fileOutputStream = null;

        try {
            if (url.startsWith("http")) {
                URL imageUrl = new URL(url);
                connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setConnectTimeout(CONNECT_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setDoInput(true);
                connection.connect();
                inputStream = connection.getInputStream();

                String fileName = hashUrl(url) + ".svg";
                File cacheDir = new File(context.getCacheDir(), "svg_cache");
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
                File svgFile = new File(cacheDir, fileName);
                fileOutputStream = new FileOutputStream(svgFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
                fileOutputStream.close();

                SvgCacheEntry newEntry = new SvgCacheEntry(url, svgFile.getAbsolutePath());
                ngPadDao.insertSvgCacheEntry(newEntry);

                inputStream.close();
                inputStream = new FileInputStream(svgFile);
            } else {
                inputStream = context.getAssets().open(url);
            }

            SVG svg = SVG.getFromInputStream(inputStream);
            PictureDrawable drawable = new PictureDrawable(svg.renderToPicture());

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
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                Log.w(TAG, "Failed to close streams", e);
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

    public static void clearCache(Context context) {
        synchronized (svgCache) {
            svgCache.clear();
        }
        executorService.execute(() -> {
            ngPadDao.clearSvgCache();
            File cacheDir = new File(context.getCacheDir(), "svg_cache");
            if (cacheDir.exists()) {
                File[] files = cacheDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        file.delete();
                    }
                }
                cacheDir.delete();
            }
        });
    }

    public static void shutdown() {
        executorService.shutdown();
    }

    private static String hashUrl(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(url.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "MD5 algorithm not found", e);
            return String.valueOf(url.hashCode());
        }
    }
}