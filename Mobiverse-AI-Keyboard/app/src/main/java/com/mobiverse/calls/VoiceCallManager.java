package com.mobiverse.calls;

import android.content.Context;
import android.media.AudioManager;
import org.webrtc.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The central class that manages all logic for group voice calls.
 * It handles WebRTC connections, signaling, and audio streams.
 */
public class VoiceCallManager implements SignallingClient.SignallingListener {

    private static VoiceCallManager instance;
    private final Context context;
    private final SignallingClient signallingClient;
    private PeerConnectionFactory peerConnectionFactory;
    private AudioSource audioSource; // The source of local audio (microphone)
    private AudioTrack localAudioTrack; // The local audio track that is sent to other participants
    private AudioManager audioManager;

    // A Map to store a separate PeerConnection for each participant.
    // The key is the participant's ID and the value is their PeerConnection object.
    private final Map<String, PeerConnection> peerConnections = new HashMap<>();
    private final List<PeerConnection.IceServer> iceServers = new ArrayList<>();
    private VoiceCallListener voiceCallListener;
    private String currentRoomId; // The ID of the current call room

    /**
     * Interface to notify the UI about call events.
     */
    public interface VoiceCallListener {
        void onParticipantJoined(String participantId);
        void onParticipantLeft(String participantId);
        void onCallEstablished(String participantId);
        void onCallEnded(); // When the entire call is over
    }

    private VoiceCallManager(Context context) {
        this.context = context.getApplicationContext();
        this.signallingClient = new SignallingClient();
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        initializePeerConnectionFactory();
        setupIceServers();
        createLocalAudioTrack();
    }

    public static synchronized VoiceCallManager getInstance(Context context) {
        if (instance == null) {
            instance = new VoiceCallManager(context);
        }
        return instance;
    }

    public void setVoiceCallListener(VoiceCallListener listener) {
        this.voiceCallListener = listener;
    }

    public void initialize(String currentUserId) {
        signallingClient.initialize(currentUserId, this);
    }

    /**
     * Initializes the PeerConnectionFactory for WebRTC.
     */
    private void initializePeerConnectionFactory() {
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(context).createInitializationOptions());
        peerConnectionFactory = PeerConnectionFactory.builder().createPeerConnectionFactory();
    }

    /**
     * Sets up STUN servers which help in traversing NAT.
     */
    private void setupIceServers() {
        iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
    }

    /**
     * Creates the local audio track from the microphone. This track will be sent to all participants.
     */
    private void createLocalAudioTrack() {
        audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
        localAudioTrack = peerConnectionFactory.createAudioTrack("ARDAMSa0", audioSource);
        localAudioTrack.setEnabled(true); // Ensure the track is enabled by default
    }

    /**
     * Joins a specific call room.
     * @param roomId The unique ID of the call room.
     */
    public void joinRoom(String roomId) {
        this.currentRoomId = roomId;
        signallingClient.joinRoom(roomId);
    }

    /**
     * Creates and configures a new PeerConnection for a new participant.
     * @param participantId The ID of the new participant.
     */
    private void createPeerConnectionForParticipant(String participantId) {
        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
        PeerConnection.Observer observer = new PeerConnection.Observer() {
            @Override
            public void onIceCandidate(IceCandidate ic) {
                // Send the found ICE candidate to the other participant via the signaling server.
                signallingClient.sendIceCandidate(ic, participantId);
            }

            @Override
            public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
                if (newState == PeerConnection.PeerConnectionState.CONNECTED && voiceCallListener != null) {
                    voiceCallListener.onCallEstablished(participantId);
                }
            }
            // Other unused methods
            @Override public void onSignalingChange(PeerConnection.SignalingState ss) {}
            @Override public void onIceConnectionChange(PeerConnection.IceConnectionState ics) {}
            @Override public void onIceConnectionReceivingChange(boolean b) {}
            @Override public void onIceGatheringChange(PeerConnection.IceGatheringState igs) {}
            @Override public void onIceCandidatesRemoved(IceCandidate[] ics) {}
            @Override public void onAddStream(MediaStream ms) {}
            @Override public void onRemoveStream(MediaStream ms) {}
            @Override public void onDataChannel(DataChannel dc) {}
            @Override public void onRenegotiationNeeded() {}
            @Override public void onAddTrack(RtpReceiver rr, MediaStream[] mss) {}
        };

        PeerConnection peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, observer);
        
        // Add the local audio track to this new connection so our voice is sent to this participant.
        MediaStream stream = peerConnectionFactory.createLocalMediaStream("ARDAMS");
        stream.addTrack(localAudioTrack);
        peerConnection.addStream(stream);

        // Store the new PeerConnection in the Map.
        peerConnections.put(participantId, peerConnection);
    }

    // --- SignallingClient.SignallingListener Implementation ---

    @Override
    public void onParticipantJoined(String participantId) {
        if (voiceCallListener != null) {
            voiceCallListener.onParticipantJoined(participantId);
        }
        createPeerConnectionForParticipant(participantId);
        PeerConnection pc = peerConnections.get(participantId);
        if (pc != null) {
            // Send a call Offer to the new participant.
            pc.createOffer(new SdpObserverAdapter() {
                @Override
                public void onCreateSuccess(SessionDescription sdp) {
                    pc.setLocalDescription(new SdpObserverAdapter(), sdp);
                    signallingClient.sendOffer(sdp, participantId);
                }
            }, new MediaConstraints());
        }
    }

    @Override
    public void onParticipantLeft(String participantId) {
        if (voiceCallListener != null) {
            voiceCallListener.onParticipantLeft(participantId);
        }
        // Close and remove the PeerConnection for the participant who left.
        PeerConnection pc = peerConnections.remove(participantId);
        if (pc != null) {
            pc.close();
        }
    }

    @Override
    public void onOfferReceived(SessionDescription offer, String senderId) {
        createPeerConnectionForParticipant(senderId);
        PeerConnection pc = peerConnections.get(senderId);
        if (pc != null) {
            pc.setRemoteDescription(new SdpObserverAdapter(), offer);
            // Create and send an Answer to the received offer.
            pc.createAnswer(new SdpObserverAdapter() {
                @Override
                public void onCreateSuccess(SessionDescription sdp) {
                    pc.setLocalDescription(new SdpObserverAdapter(), sdp);
                    signallingClient.sendAnswer(sdp, senderId);
                }
            }, new MediaConstraints());
        }
    }

    @Override
    public void onAnswerReceived(SessionDescription answer, String senderId) {
        PeerConnection pc = peerConnections.get(senderId);
        if (pc != null) {
            // Set the received answer as the remote description.
            pc.setRemoteDescription(new SdpObserverAdapter(), answer);
        }
    }

    @Override
    public void onIceCandidateReceived(IceCandidate iceCandidate, String senderId) {
        PeerConnection pc = peerConnections.get(senderId);
        if (pc != null) {
            // Add the received ICE candidate to the PeerConnection.
            pc.addIceCandidate(iceCandidate);
        }
    }

    /**
     * Mutes or unmutes the local audio.
     */
    public void setMute(boolean isMuted) {
        if (localAudioTrack != null) {
            localAudioTrack.setEnabled(!isMuted);
        }
    }

    /**
     * Turns the speakerphone on or off.
     */
    public void setSpeakerphoneOn(boolean isOn) {
        if (audioManager != null) {
            audioManager.setSpeakerphoneOn(isOn);
        }
    }

    /**
     * Leaves the call. Closes all connections and releases resources.
     */
    public void leaveCall() {
        signallingClient.leaveRoom();
        for (PeerConnection pc : peerConnections.values()) {
            pc.close();
        }
        peerConnections.clear();
        currentRoomId = null;
        if (voiceCallListener != null) {
            voiceCallListener.onCallEnded();
        }
    }
    
    @Override
    public void onCallEnded() {
        leaveCall();
    }

    /**
     * Cleans up and closes the manager completely.
     */
    public void close() {
        if (audioSource != null) {
            audioSource.dispose();
            audioSource = null;
        }
        leaveCall();
    }
}
