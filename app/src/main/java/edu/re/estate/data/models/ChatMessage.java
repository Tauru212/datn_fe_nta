package edu.re.estate.data.models;

import java.io.Serializable;

public class ChatMessage implements Serializable {

    private String content;
    private long timeSendMessage;
    private int senderId;
    private String topicId;
    private String type = "text";

    public ChatMessage() {
    }

    public ChatMessage(String content, long timeSendMessage, int senderId, String topicId) {
        this.content = content;
        this.timeSendMessage = timeSendMessage;
        this.senderId = senderId;
        this.topicId = topicId;
    }

    public ChatMessage(String content, long timeSendMessage, int senderId, String topicId, String type) {
        this.content = content;
        this.timeSendMessage = timeSendMessage;
        this.senderId = senderId;
        this.topicId = topicId;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimeSendMessage() {
        return timeSendMessage;
    }

    public void setTimeSendMessage(long timeSendMessage) {
        this.timeSendMessage = timeSendMessage;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
