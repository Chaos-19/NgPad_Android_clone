package com.chaosdev.ngpad.viewmodel.main;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.chaosdev.ngpad.data.repository.NgPadRepository;
import com.chaosdev.ngpad.model.Category;
import com.chaosdev.ngpad.model.main.Course;
import com.chaosdev.ngpad.model.main.Lesson;
import com.chaosdev.ngpad.model.main.NgPad;

public class NgPadViewModel extends ViewModel {
    private final MutableLiveData<NgPad> ngPadLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final NgPadRepository repository;
    private boolean hasFetched = false;

    public NgPadViewModel(Context context) {
        repository = NgPadRepository.getInstance(context);
        // Initial data might not be loaded yet; fetch or wait for repository to load
        fetchNgPadData();
    }

    public void fetchNgPadData() {
        if (hasFetched) {
            ngPadLiveData.setValue(repository.getNgPad());
            return;
        }
        hasFetched = true;

        repository.fetchNgPadData(new NgPadRepository.NgPadCallback() {
            @Override
            public void onCategoriesFetched(NgPad ngPad) {
                ngPadLiveData.setValue(ngPad);
            }

            @Override
            public void onCoursesFetched(Category category) {
                ngPadLiveData.setValue(repository.getNgPad());
            }

            @Override
            public void onLessonsFetched(Course course) {
                ngPadLiveData.setValue(repository.getNgPad());
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                hasFetched = false;
            }
        });
    }

    public LiveData<NgPad> getNgPadLiveData() {
        return ngPadLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        hasFetched = false;
    }
}