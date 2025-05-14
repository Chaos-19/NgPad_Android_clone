package com.chaosdev.ngpad.viewmodel.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

public class SharedViewModelStoreOwner implements ViewModelStoreOwner {
    private static SharedViewModelStoreOwner instance;
    private final ViewModelStore viewModelStore;

    private SharedViewModelStoreOwner() {
        viewModelStore = new ViewModelStore();
    }

    public static synchronized SharedViewModelStoreOwner getInstance() {
        if (instance == null) {
            instance = new SharedViewModelStoreOwner();
        }
        return instance;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }
}