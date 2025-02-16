package edu.re.estate.data.models;

import java.io.Serializable;

public class ImagePost implements Serializable {
    private String image;

    public ImagePost(String image) {
        this.image = image;
    }

    public ImagePost() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
