package com.mobiverse.nebula.satellite

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mobiverse.nebula.data.AppDatabase
import com.mobiverse.nebula.crypto.SignalEncryption // Assuming you have this
import kotlinx.coroutines.tasks.await
import java.io.File

class SatelliteUploadWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val msgId = inputData.getString("msgId") ?: return Result.failure()
        val db = AppDatabase.getInstance(applicationContext)
        val message = db.messageDao().get(msgId) ?: return Result.success() // Or failure if ID must be valid

        // 1. Check for satellite/internet connectivity
        if (!SatelliteHelper.isConnected(applicationContext)) {
            return if (message.retryCount >= 20) Result.failure() else Result.retry()
        }

        return try {
            // 2. Decrypt the file before uploading (conceptually)
            // In a real E2EE, you don't decrypt on the sending client.
            // You'd upload the encrypted file and send the recipient the key to decrypt it.
            // This part is simplified for this example.
            val fileToUpload = File(message.voicePath!!)

            // 3. Upload to Firebase Storage
            val storageRef = FirebaseStorage.getInstance().reference.child("voice_messages/${message.id}.opus")
            val uploadTask = storageRef.putFile(android.net.Uri.fromFile(fileToUpload)).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await().toString()

            // 4. Update Firestore with the download URL
            val updatedMessage = message.copy(voicePath = downloadUrl, status = "SENT")
            FirebaseFirestore.getInstance().collection("messages").document(message.id)
                .set(updatedMessage).await()

            // 5. Clean up local file and DB entry
            fileToUpload.delete()
            db.messageDao().delete(message.id) // Or update status to a final state

            Result.success()
        } catch (e: Exception) {
            // Handle exceptions (e.g., network error, firebase error)
            val updatedMsg = message.copy(retryCount = message.retryCount + 1)
            db.messageDao().insert(updatedMsg)
            if (updatedMsg.retryCount > 20) Result.failure() else Result.retry()
        }
    }
}
