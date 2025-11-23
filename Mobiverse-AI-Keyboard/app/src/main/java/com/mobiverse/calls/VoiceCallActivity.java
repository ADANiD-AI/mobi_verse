package com.mobiverse.calls;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.mobiverse.aikeyboard.R;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * The user interface for the group voice call.
 * This activity handles the creation/joining of a call room and manages the UI state.
 */
public class VoiceCallActivity extends AppCompatActivity implements VoiceCallManager.VoiceCallListener {

    private VoiceCallManager voiceCallManager;
    private TextView tvCallState; // To display the call status (e.g., "In call (3 participants)")
    private ImageButton btnMute, btnEndCall, btnSpeaker;
    private boolean isMuted = false;
    private boolean isSpeakerOn = false;
    // A set to track the IDs of all participants in the call.
    private final Set<String> participants = new HashSet<>();
    private String roomId; // The unique ID of the current call room.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);

        // Get the VoiceCallManager instance and set the listener.
        voiceCallManager = VoiceCallManager.getInstance(this);
        voiceCallManager.setVoiceCallListener(this);

        // Get the current user's ID and initialize the manager.
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        voiceCallManager.initialize(currentUserId);

        initializeViews();
        setupClickListeners();

        // Check if we are creating a new room or joining an existing one.
        if (getIntent().hasExtra("ROOM_ID")) {
            // If ROOM_ID is in the intent, it means we are joining an existing call.
            roomId = getIntent().getStringExtra("ROOM_ID");
        } else {
            // If there is no ROOM_ID, start a new call room by creating a new, unique ID.
            roomId = UUID.randomUUID().toString();
        }

        // Join the call room. This will start the group call process.
        voiceCallManager.joinRoom(roomId);
        setupInCallUI();
    }

    /**
     * Binds the UI views (Buttons, TextViews) with their IDs.
     */
    private void initializeViews() {
        tvCallState = findViewById(R.id.tv_caller_id);
        btnMute = findViewById(R.id.btn_mute);
        btnEndCall = findViewById(R.id.btn_end_call);
        btnSpeaker = findViewById(R.id.btn_speaker);
        
        // Hide unused buttons from the old layout.
        // These buttons were for one-to-one calls and are no longer needed.
        findViewById(R.id.btn_accept_call).setVisibility(View.GONE);
        findViewById(R.id.btn_reject_call).setVisibility(View.GONE);
        findViewById(R.id.btn_record).setVisibility(View.GONE); // Recording is disabled for now.
    }

    /**
     * Sets up the UI to be used during the call.
     */
    private void setupInCallUI() {
        tvCallState.setText("Joining room: " + roomId.substring(0, 8) + "...");
        btnMute.setVisibility(View.VISIBLE);
        btnSpeaker.setVisibility(View.VISIBLE);
        btnEndCall.setVisibility(View.VISIBLE);
    }

    /**
     * Sets up the click listeners for all the buttons.
     */
    private void setupClickListeners() {
        // Mute button: Toggles audio mute.
        btnMute.setOnClickListener(v -> {
            isMuted = !isMuted;
            voiceCallManager.setMute(isMuted);
            btnMute.setAlpha(isMuted ? 0.5f : 1.0f); // Change button appearance
        });

        // End Call button: Leaves the call.
        btnEndCall.setOnClickListener(v -> voiceCallManager.leaveCall());

        // Speaker button: Toggles the speakerphone.
        btnSpeaker.setOnClickListener(v -> {
            isSpeakerOn = !isSpeakerOn;
            voiceCallManager.setSpeakerphoneOn(isSpeakerOn);
            btnSpeaker.setAlpha(isSpeakerOn ? 0.5f : 1.0f);
        });
    }

    /**
     * Updates the participant count on the UI.
     */
    private void updateParticipantCount() {
        // +1 is for the current user.
        int count = participants.size() + 1;
        tvCallState.setText("In call (" + count + " participants)");
    }

    // --- VoiceCallManager.VoiceCallListener Implementation ---

    @Override
    public void onParticipantJoined(String participantId) {
        runOnUiThread(() -> {
            participants.add(participantId);
            updateParticipantCount();
            Toast.makeText(this, "Participant joined", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onParticipantLeft(String participantId) {
        runOnUiThread(() -> {
            participants.remove(participantId);
            updateParticipantCount();
            Toast.makeText(this, "Participant left", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onCallEstablished(String participantId) {
        runOnUiThread(() -> {
            // Connection with one participant is stable.
            Toast.makeText(this, "Connection established", Toast.LENGTH_SHORT).show();
            if (participants.size() == 1) { // If this is the first connection
                 updateParticipantCount();
            }
        });
    }

    @Override
    public void onCallEnded() {
        runOnUiThread(() -> {
            Toast.makeText(this, "Call Ended", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity.
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure we leave the call when the activity is destroyed.
        voiceCallManager.leaveCall();
        voiceCallManager.setVoiceCallListener(null);
    }
}
