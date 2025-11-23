package com.mobiverse.nebula.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "satellite_messages")
data class SatelliteMessageEntity(
    @PrimaryKey val id: String,
    val senderId: String,
    val receiverId: String,
    val text: String?,
    val voicePath: String?,
    val timestamp: Long,
    val status: String, // e.g., PENDING, SENDING, SENT, FAILED
    val retryCount: Int
)
