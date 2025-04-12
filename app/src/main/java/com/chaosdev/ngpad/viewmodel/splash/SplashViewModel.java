package com.chaosdev.ngpad;

import android.os.Handler;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.concurrent.Executors;

public class SplashViewModel extends ViewModel {
    private MutableLiveData<Boolean> isInitialized = new MutableLiveData<>();

    public void checkInitialization() {
        // Use background thread for delays
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Thread.sleep(2000); // Simulate 2-second delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isInitialized.postValue(true);
        });
    }

    public LiveData<Boolean> getIsInitialized() {
        return isInitialized;
    }
}
