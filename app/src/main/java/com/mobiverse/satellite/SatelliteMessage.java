package com.mobiverse.satellite;

import java.util.UUID;

/**
 * Data model for a satellite message.
 * This class holds all information related to a single message, whether text or voice.
 * Based on the user's plan for Project Nebula.
 * MIT License - Mobiverse Project Nebula
 */
public class SatelliteMessage {

    private String id;
    private String senderId;
    private String receiverId;
    private String text;
    private String voiceUrl; // Can be a local file path or a remote URL
    private long timestamp;
    private MessageStatus status;
    private int retryCount;

    // Default constructor for Firebase/other serialization libraries
    public SatelliteMessage() {}

    public SatelliteMessage(String senderId, String receiverId, String text, String voiceUrl) {
        this.id = UUID.randomUUID().toString();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.voiceUrl = voiceUrl;
        this.timestamp = System.currentTimeMillis();
        this.status = MessageStatus.PENDING;
        this.retryCount = 0;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getVoiceUrl() { return voiceUrl; }
    public void setVoiceUrl(String voiceUrl) { this.voiceUrl = voiceUrl; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public MessageStatus getStatus() { return status; }
    public void setStatus(MessageStatus status) { this.status = status; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
}
