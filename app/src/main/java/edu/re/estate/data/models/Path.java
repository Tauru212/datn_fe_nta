package edu.re.estate.data.models;

import com.google.gson.annotations.SerializedName;

public class Path {
    @SerializedName(("path"))
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Path(String path) {
        this.path = path;
    }
}
