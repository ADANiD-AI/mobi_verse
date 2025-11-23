package com.mobiverse.calls;

import androidx.annotation.NonNull;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles signaling for group calls using Firebase Realtime Database.
 * This class is responsible for joining/leaving rooms and sending/receiving signals (offers, answers, ICE candidates).
 */
public class SignallingClient {

    private DatabaseReference roomRef; // Firebase reference to the current call room
    private String currentUserId; // The ID of the current user
    private SignallingListener listener;
    private final Gson gson = new Gson(); // For serializing/deserializing objects to/from JSON

    /**
     * Interface for signaling events.
     */
    public interface SignallingListener {
        void onParticipantJoined(String participantId);
        void onParticipantLeft(String participantId);
        void onOfferReceived(SessionDescription offer, String senderId);
        void onAnswerReceived(SessionDescription answer, String senderId);
        void onIceCandidateReceived(IceCandidate iceCandidate, String senderId);
        void onCallEnded();
    }

    public void initialize(String userId, SignallingListener signallingListener) {
        this.currentUserId = userId;
        this.listener = signallingListener;
    }

    /**
     * Joins a specific call room and starts listening for participants and signals.
     * @param roomId The unique ID of the call room to join.
     */
    public void joinRoom(String roomId) {
        // Get a reference to 'call_rooms/{roomId}' in Firebase
        roomRef = FirebaseDatabase.getInstance().getReference("call_rooms").child(roomId);
        
        // Add the current user to the list of participants in this room
        roomRef.child("participants").child(currentUserId).setValue(true);

        listenForParticipants(); // Listen for new participants
        listenForSignals();      // Listen for incoming signals
    }

    /**
     * Listens for participants joining and leaving the room.
     */
    private void listenForParticipants() {
        roomRef.child("participants").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                String participantId = snapshot.getKey();
                // If a new participant joins and it's not us, notify the listener.
                if (participantId != null && !participantId.equals(currentUserId) && listener != null) {
                    listener.onParticipantJoined(participantId);
                }
            }
            
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String participantId = snapshot.getKey();
                // If a participant leaves, notify the listener.
                if (participantId != null && !participantId.equals(currentUserId) && listener != null) {
                    listener.onParticipantLeft(participantId);
                }
            }

            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, String s) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, String s) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Listens for signaling messages (offers, answers, ICE) targeted at the current user.
     */
    private void listenForSignals() {
        // Query for signals where 'recipientId' matches our ID.
        roomRef.child("signals").orderByChild("recipientId").equalTo(currentUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String s) {
                if (snapshot.exists()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    String type = (String) data.get("type");
                    String senderId = (String) data.get("senderId");
                    String payloadJson = gson.toJson(data.get("payload"));

                    if (type == null || senderId == null) return;

                    // Based on the signal type, call the appropriate listener method.
                    switch (type) {
                        case "offer":
                            listener.onOfferReceived(gson.fromJson(payloadJson, SessionDescription.class), senderId);
                            break;
                        case "answer":
                            listener.onAnswerReceived(gson.fromJson(payloadJson, SessionDescription.class), senderId);
                            break;
                        case "ice-candidate":
                            listener.onIceCandidateReceived(gson.fromJson(payloadJson, IceCandidate.class), senderId);
                            break;
                    }
                    
                    // The signal has been processed, remove it from the database.
                    snapshot.getRef().removeValue();
                }
            }
            @Override public void onChildChanged(@NonNull DataSnapshot snapshot, String s) {}
            @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override public void onChildMoved(@NonNull DataSnapshot snapshot, String s) {}
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Sends a signal to a specific participant in the room.
     * @param type The type of signal (e.g., "offer").
     * @param payload The data to be sent.
     * @param recipientId The user ID of the recipient.
     */
    private void sendSignal(String type, Object payload, String recipientId) {
        if (roomRef == null) return; // Don't send if not in a room
        Map<String, Object> signal = new HashMap<>();
        signal.put("type", type);
        signal.put("senderId", currentUserId);
        signal.put("recipientId", recipientId);
        signal.put("payload", payload);
        roomRef.child("signals").push().setValue(signal);
    }

    public void sendOffer(SessionDescription offer, String targetId) {
        sendSignal("offer", offer, targetId);
    }

    public void sendAnswer(SessionDescription answer, String targetId) {
        sendSignal("answer", answer, targetId);
    }

    public void sendIceCandidate(IceCandidate iceCandidate, String targetId) {
        sendSignal("ice-candidate", iceCandidate, targetId);
    }

    /**
     * Leaves the current room and removes the user from the participants list.
     */
    public void leaveRoom() {
        if (roomRef != null) {
            roomRef.child("participants").child(currentUserId).removeValue();
            roomRef = null;
        }
    }
}
