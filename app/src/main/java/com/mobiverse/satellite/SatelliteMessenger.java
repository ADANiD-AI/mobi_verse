package com.mobiverse.satellite;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Main logic class for asynchronous satellite messaging.
 * Handles sending, queuing, and uploading messages with offline-first support.
 * Based on the user's plan for Project Nebula.
 * MIT License - Mobiverse Project Nebula
 */
public class SatelliteMessenger {

    private final Context context;
    private final FirebaseFirestore firestore;
    private final FirebaseStorage storage;
    // private final OpusEncoder opusEncoder; // Will be integrated later
    private final List<SatelliteMessage> pendingMessages = new ArrayList<>();
    private final Handler satelliteMonitorHandler = new Handler(Looper.getMainLooper());

    public SatelliteMessenger(Context context) {
        this.context = context.getApplicationContext();
        this.firestore = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
        // this.opusEncoder = new OpusEncoder();
        // loadPendingMessagesFromDb(); // Load any previously unsent messages
    }

    /**
     * Sends a text message with offline support.
     * @param text The text content.
     * @param receiverId The recipient's user ID.
     */
    public void sendTextMessage(String text, String receiverId) {
        String senderId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "unknown_sender";
        SatelliteMessage msg = new SatelliteMessage(senderId, receiverId, text, null);
        saveAndTrySend(msg);
    }

    /**
     * Sends a voice message after compressing it.
     * @param rawPcmFile The raw audio file.
     * @param receiverId The recipient's user ID.
     */
    public void sendVoiceMessage(File rawPcmFile, String receiverId) {
        // File compressedOpusFile = new File(context.getCacheDir(), "voice_" + System.currentTimeMillis() + ".opus");
        // opusEncoder.encode(rawPcmFile, compressedOpusFile, 6000);
        
        // For now, we will simulate the compressed file path
        String compressedFilePath = rawPcmFile.getAbsolutePath();

        String senderId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "unknown_sender";
        SatelliteMessage msg = new SatelliteMessage(senderId, receiverId, null, compressedFilePath);
        saveAndTrySend(msg);
    }

    private void saveAndTrySend(SatelliteMessage message) {
        // TODO: Save to a local Room database for persistence
        // AppDatabase.getInstance(context).messageDao().insert(message.toEntity());
        pendingMessages.add(message);

        if (ConnectivityHelper.isSatelliteOrInternetAvailable(context)) {
            uploadMessage(message);
        } else {
            startSatelliteLinkMonitor();
        }
    }

    private void uploadMessage(final SatelliteMessage msg) {
        msg.setStatus(MessageStatus.SENDING);
        // TODO: Update UI with the new status

        if (msg.getVoiceUrl() != null && msg.getVoiceUrl().startsWith("/")) { // It's a local file
            uploadVoiceFileAndSend(msg);
        } else {
            sendToFirestore(msg);
        }
    }

    private void uploadVoiceFileAndSend(final SatelliteMessage msg) {
        File voiceFile = new File(msg.getVoiceUrl());
        StorageReference ref = storage.getReference().child("voices/" + msg.getId() + ".opus");

        ref.putFile(Uri.fromFile(voiceFile))
            .addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                msg.setVoiceUrl(uri.toString()); // Update with remote URL
                sendToFirestore(msg);
            }))
            .addOnFailureListener(e -> handleUploadFailure(msg));
    }

    private void sendToFirestore(final SatelliteMessage msg) {
        firestore.collection("satellite_messages").document(msg.getId()).set(msg)
            .addOnSuccessListener(aVoid -> {
                msg.setStatus(MessageStatus.SENT);
                // TODO: Remove from local DB and update UI
                pendingMessages.remove(msg);
            })
            .addOnFailureListener(e -> handleUploadFailure(msg));
    }

    private void handleUploadFailure(SatelliteMessage msg) {
        msg.setStatus(MessageStatus.FAILED);
        if (msg.getRetryCount() < 10) {
            msg.setRetryCount(msg.getRetryCount() + 1);
            // TODO: Update in local DB
            startSatelliteLinkMonitor(); // Retry when connection is back
        } else {
            // TODO: Mark as permanently failed in UI
        }
    }

    private void startSatelliteLinkMonitor() {
        satelliteMonitorHandler.postDelayed(satelliteCheckRunnable, 5000); // Check every 5 seconds
    }

    private final Runnable satelliteCheckRunnable = new Runnable() {
        @Override
        public void run() {
            if (ConnectivityHelper.isSatelliteOrInternetAvailable(context) && !pendingMessages.isEmpty()) {
                // Create a copy of the list to avoid ConcurrentModificationException
                List<SatelliteMessage> messagesToUpload = new ArrayList<>(pendingMessages);
                for (SatelliteMessage msg : messagesToUpload) {
                    uploadMessage(msg);
                }
            } else if (!pendingMessages.isEmpty()){
                // Reschedule if there are still pending messages
                satelliteMonitorHandler.postDelayed(this, 5000);
            }
        }
    };
}
