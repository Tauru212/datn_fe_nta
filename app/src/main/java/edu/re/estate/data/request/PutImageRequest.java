package edu.re.estate.data.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PutImageRequest implements Serializable {
    @SerializedName("post_id")
    private int postId;
    @SerializedName("image")
    private String image;

    public PutImageRequest(int postId, String image) {
        this.postId = postId;
        this.image = image;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
