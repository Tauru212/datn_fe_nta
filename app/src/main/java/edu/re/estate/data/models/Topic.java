package edu.re.estate.data.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@IgnoreExtraProperties
public class Topic implements Serializable {

    private int guestId;
    private String image;
    private String lastMessage;
    private int ownerId;
    private String title;

    private long timeLastMessage;
    private String name;
    private String avatar;

    @Exclude
    private String topicId;

    @Exclude
    private String documentId;

    public Topic(int guestId, String image, String lastMessage, int ownerId, String title, long timeLastMessage, String name, String avatar, String topicId) {
        this.guestId = guestId;
        this.image = image;
        this.lastMessage = lastMessage;
        this.ownerId = ownerId;
        this.title = title;
        this.timeLastMessage = timeLastMessage;
        this.name = name;
        this.avatar = avatar;
        this.topicId = topicId;
    }

    public Topic(int guestId, String image, String lastMessage, int ownerId, String title) {
        this.guestId = guestId;
        this.image = image;
        this.lastMessage = lastMessage;
        this.ownerId = ownerId;
        this.title = title;
    }

    public Topic() {
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getTimeLastMessage() {
        return timeLastMessage;
    }

    public void setTimeLastMessage(long timeLastMessage) {
        this.timeLastMessage = timeLastMessage;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
