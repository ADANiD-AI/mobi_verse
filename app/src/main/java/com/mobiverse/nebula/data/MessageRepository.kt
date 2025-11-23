package com.mobiverse.nebula.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.jakewharton.timber.Timber
import com.mobiverse.nebula.data.dao.MessageDao
import com.mobiverse.nebula.data.entity.SatelliteMessageEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository for handling message data operations.
 * It abstracts the data sources (local Room DB and remote Firestore) from the rest of the app.
 */
class MessageRepository(private val messageDao: MessageDao, private val firestore: FirebaseFirestore) {

    private val currentUserId: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid

    /**
     * Gets all messages for a specific conversation from the local database.
     */
    fun getConversation(recipientId: String): Flow<List<SatelliteMessageEntity>> {
        val userId = currentUserId ?: return kotlinx.coroutines.flow.flowOf(emptyList())
        return messageDao.getMessagesBetweenUsers(userId, recipientId)
    }

    /**
     * Listens for real-time message updates from Firestore.
     * It fetches new messages sent to the current user and saves them to the local database.
     */
    fun startListeningForMessages() {
        val userId = currentUserId ?: return

        firestore.collection("messages")
            .whereEqualTo("receiverId", userId)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Timber.w(e, "Listen failed.")
                    return@addSnapshotListener
                }

                for (doc in snapshots!!.documentChanges) {
                    if (doc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                        val message = doc.document.toObject<SatelliteMessageEntity>()
                        // Here, you would normally trigger a download for the voice file.
                        // For now, we'll just save the message metadata.
                        // We also need to update the message status to DOWNLOADED/RECEIVED.
                        message.status = "RECEIVED" // Placeholder status
                        suspend { messageDao.insert(message) } 
                        Timber.d("New message received and saved: ${message.id}")
                    }
                }
            }
    }
}
