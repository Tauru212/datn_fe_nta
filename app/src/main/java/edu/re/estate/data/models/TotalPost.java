package edu.re.estate.data.models;

public class TotalPost {
    private int all;
    private int processing;
    private int approved;
    private int refused;

    public TotalPost(int all, int processing, int approved, int refused) {
        this.all = all;
        this.processing = processing;
        this.approved = approved;
        this.refused = refused;
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

    public int getProcessing() {
        return processing;
    }

    public void setProcessing(int processing) {
        this.processing = processing;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getRefused() {
        return refused;
    }

    public void setRefused(int refused) {
        this.refused = refused;
    }
}
