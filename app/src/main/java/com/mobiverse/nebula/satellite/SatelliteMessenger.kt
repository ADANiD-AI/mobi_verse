package com.mobiverse.nebula.satellite

import android.content.Context
import androidx.work.*
import com.google.firebase.auth.FirebaseAuth
import com.mobiverse.nebula.data.AppDatabase
import com.mobiverse.nebula.data.entity.SatelliteMessageEntity
import com.mobiverse.nebula.crypto.SignalEncryption
// import com.soywiz.korau.format.org.gragravarr.opus.OpusEncoder // TODO: Verify and implement correct Opus encoder library
import java.io.File
import java.util.*

class SatelliteMessenger(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val workManager = WorkManager.getInstance(context)
    private var encryption: SignalEncryption? = null

    /**
     * Encodes, encrypts, and schedules a voice message for sending.
     * This is a suspend function and must be called from a coroutine.
     */
    suspend fun sendVoiceMessageAsync(rawPcmFile: File, receiverId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
            ?: throw IllegalStateException("User must be logged in to send messages.")

        // Lazily initialize encryption for the current user
        if (encryption == null) {
            encryption = SignalEncryption(context, currentUser.uid)
        }

        // 1. Compress the audio file using Opus Encoder (Conceptual)
        // TODO: Implement actual Opus encoding. For now, we use the uncompressed file.
        val compressedFile = rawPcmFile

        // 2. Encrypt the compressed file using Signal Protocol
        val encryptedFile = encryption!!.encryptFile(compressedFile, receiverId)

        // 3. Create a message entity to be stored locally
        val message = SatelliteMessageEntity(
            id = UUID.randomUUID().toString(),
            senderId = currentUser.uid,
            receiverId = receiverId,
            text = null,
            voicePath = encryptedFile.absolutePath, // Path to the local encrypted file
            timestamp = System.currentTimeMillis(),
            status = "PENDING",
            retryCount = 0
        )

        // 4. Save the message to the local Room database
        db.messageDao().insert(message)

        // 5. Schedule the background upload using WorkManager
        val uploadWorkRequest = OneTimeWorkRequestBuilder<SatelliteUploadWorker>()
            .setInputData(workDataOf("msgId" to message.id))
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED) // Any available network
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(message.id, ExistingWorkPolicy.KEEP, uploadWorkRequest)
    }
}
