package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.List;

public class ContributorEntryList {
    private List<ContributorEntry> contributorEntryList;

    public ContributorEntryList() {
        this.contributorEntryList = new ArrayList<>();
    }

    public ContributorEntryList(List<ContributorEntry> contributorEntryList) {
        this.contributorEntryList = contributorEntryList;
    }

    public void add(ContributorEntry entry) {
        contributorEntryList.add(entry);

    }


    public void setContributorEntryList(List<ContributorEntry> contributorEntryList) {
        this.contributorEntryList = contributorEntryList;
    }

    public List<ContributorEntry> getContributorEntryList() {
        return contributorEntryList;
    }
}
