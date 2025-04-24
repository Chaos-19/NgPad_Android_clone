package com.chaosdev.ngpad.viewmodel.main;

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
    private final NgPadRepository repository = new NgPadRepository();
    private boolean hasFetched = false; // Track if data has been fetched

    public void fetchNgPadData() {
        if (hasFetched) {
            return; // Prevent re-fetching if already fetched
        }
        hasFetched = true;

        repository.fetchNgPadData(new NgPadRepository.NgPadCallback() {
            @Override
            public void onCategoriesFetched(NgPad ngPad) {
                ngPadLiveData.setValue(ngPad);
            }

            @Override
            public void onCoursesFetched(Category category) {
                // Only update LiveData if necessary
                NgPad currentNgPad = ngPadLiveData.getValue();
                if (currentNgPad != null) {
                    ngPadLiveData.setValue(currentNgPad); // Trigger UI update
                }
            }

            @Override
            public void onLessonsFetched(Course course) {
                NgPad currentNgPad = ngPadLiveData.getValue();
                if (currentNgPad != null) {
                    ngPadLiveData.setValue(currentNgPad);
                }
            }

            @Override
            public void onSubLessonsFetched(Lesson lesson) {
                NgPad currentNgPad = ngPadLiveData.getValue();
                if (currentNgPad != null) {
                    ngPadLiveData.setValue(currentNgPad);
                }
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                hasFetched = false; // Allow re-fetching on error
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
        hasFetched = false; // Reset on ViewModel destruction
    }
}