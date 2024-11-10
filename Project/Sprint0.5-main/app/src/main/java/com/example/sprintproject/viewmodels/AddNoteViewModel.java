package com.example.sprintproject.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.BR;
import com.example.sprintproject.model.AuthRepository;
import com.example.sprintproject.model.ContributorEntry;
import com.example.sprintproject.model.ContributorEntryList;
import com.example.sprintproject.model.UserDurationDatabase;

public class AddNoteViewModel extends BaseObservable {
    private ContributorEntry entry;
    private final MutableLiveData<ContributorEntryList> contributorsListLiveData = new MutableLiveData<>();


    private String successMessage = "Note Saved";
    private String errorMessage = "Note Not Saved";

    @Bindable
    private String toastMessage = null;

    public String getToastMessage() {
        return toastMessage;
    }

    public void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    public void setContributorEmail(String contributorEmail) {
        entry.setContributorEmail(contributorEmail);
        notifyPropertyChanged(BR.contributorEmail);
    }

    @Bindable
    public String getContributorEmail() {
        return entry.getContributorEmail();
    }

    @Bindable
    public String getNotes() {
        return entry.getNotes();
    }

    public void setNotes(String notes) {
        entry.setNotes(notes);
        notifyPropertyChanged(BR.notes);
    }

    public AddNoteViewModel() { entry = new ContributorEntry("", "");}

    public LiveData<ContributorEntryList> getContributorsListLiveData() {
        return contributorsListLiveData;
    }

    public void fetchContributorsList() {
        UserDurationDatabase.getInstance().getContributorsList(new UserDurationDatabase.DataStatus2() {
            @Override
            public void DataIsLoaded(String userId, ContributorEntryList contributorsList) {
                contributorsListLiveData.setValue(contributorsList);
            }

            @Override
            public void DataNotFound(String userId) {
                // Handle the case where data is not found (e.g., set null or notify the UI)
                contributorsListLiveData.setValue(null);
            }
        });
    }

    public void onSaveClicked() {
        // Return the result to the previous activity
        ContributorEntryList contributorEntryList = getContributorsListLiveData().getValue();
        if (contributorEntryList == null) {
            // Handle the case where the data is still loading
            setToastMessage("Loading contributors, please wait...");
            return;
        }
        if (addNoteSaved(contributorEntryList)) {
            setToastMessage(successMessage);
        } else {
            setToastMessage(errorMessage);
        }
    }

    private boolean addNoteSaved (ContributorEntryList contributorEntryList) {
        boolean saved = false;
        String contributorEmail = entry.getContributorEmail();
        String notes = entry.getNotes();
        if (contributorEmail != null && notes != null) {
            addNote(contributorEmail, notes, contributorEntryList);
            saved = true;
        }
        return saved;
    }

    public void addNote(String contributorUserId, String notes, ContributorEntryList contributorEntryList) {
        ContributorEntry newEntry = new ContributorEntry(contributorUserId, notes);
        UserDurationDatabase.getInstance().addContributorsListEntry(newEntry,contributorEntryList);
    }
}
