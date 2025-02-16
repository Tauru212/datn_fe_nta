package edu.re.estate.data.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavoriteRequest implements Serializable {
    @SerializedName("post_id")
    private int postId;

    public FavoriteRequest(int postId) {
        this.postId = postId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
