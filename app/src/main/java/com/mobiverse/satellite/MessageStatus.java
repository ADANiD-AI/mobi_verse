package com.mobiverse.satellite;

/**
 * Represents the status of a satellite message.
 * Based on the plan by the user for Project Nebula.
 */
public enum MessageStatus {
    /**
     * The message is waiting locally on the device to be sent.
     */
    PENDING,
    /**
     * The message is currently being uploaded.
     */
    SENDING,
    /**
     * The message has been successfully sent to the server/cloud.
     */
    SENT,
    /**
     * The message has been confirmed as delivered to the recipient's device.
     */
    DELIVERED,
    /**
     * The message failed to send after multiple retries.
     */
    FAILED
}
