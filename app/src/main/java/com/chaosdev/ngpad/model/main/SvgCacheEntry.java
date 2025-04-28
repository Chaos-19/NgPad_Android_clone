package com.chaosdev.ngpad.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "svg_cache")
public class SvgCacheEntry {
    @PrimaryKey
    @NonNull
    private String url;
    @NonNull
    private String filePath;

    public SvgCacheEntry(@NonNull String url, @NonNull String filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    @NonNull
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(@NonNull String filePath) {
        this.filePath = filePath;
    }
}