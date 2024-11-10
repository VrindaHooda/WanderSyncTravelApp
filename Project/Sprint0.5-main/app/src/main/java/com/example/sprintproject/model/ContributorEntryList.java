package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.List;

public class ContributorEntryList {
    private List<ContributorEntry> contributorEntryList;

    public ContributorEntryList() {

    }

    public ContributorEntryList(List<ContributorEntry> contributorEntryList) {
        this.contributorEntryList = contributorEntryList;
    }

    public void setContributorEntryList(List<ContributorEntry> contributorEntryList) {
        this.contributorEntryList = contributorEntryList;
    }

    public List<ContributorEntry> getContributorEntryList() {
        return contributorEntryList;
    }
}
