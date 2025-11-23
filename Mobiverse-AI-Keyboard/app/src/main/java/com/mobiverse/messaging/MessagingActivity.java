package com.mobiverse.messaging;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.mobiverse.aikeyboard.R;
import java.util.ArrayList;
import java.util.List;

public class MessagingActivity extends AppCompatActivity implements MessagingManager.MessageListener {

    private MessagingManager messagingManager;
    private RecyclerView rvMessages;
    private EditText etMessage;
    private Button btnSend;
    private MessageAdapter messageAdapter;
    private List<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        messagingManager = MessagingManager.getInstance(this);
        messagingManager.setMessageListener(this); // Set listener

        initializeViews();
        setupRecyclerView();

        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString();
            if (!message.isEmpty()) {
                messagingManager.sendMessageToAll(message);
                addMessage("Me: " + message);
                etMessage.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        messagingManager.startAdvertising();
        messagingManager.startDiscovery();
    }

    @Override
    protected void onStop() {
        super.onStop();
        messagingManager.stop();
    }

    private void initializeViews() {
        rvMessages = findViewById(R.id.rv_messages);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
    }

    private void setupRecyclerView() {
        messageAdapter = new MessageAdapter(messages);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(messageAdapter);
    }

    private void addMessage(String message) {
        runOnUiThread(() -> {
            messages.add(message);
            messageAdapter.notifyItemInserted(messages.size() - 1);
            rvMessages.scrollToPosition(messages.size() - 1);
        });
    }

    @Override
    public void onMessageReceived(String sender, String message) {
        addMessage(sender + ": " + message);
    }

    @Override
    public void onEndpointConnected(String endpointName) {
        runOnUiThread(() -> {
            Snackbar.make(findViewById(android.R.id.content), endpointName + " has connected!", Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onEndpointDisconnected(String endpointName) {
        runOnUiThread(() -> {
            Snackbar.make(findViewById(android.R.id.content), endpointName + " has disconnected.", Snackbar.LENGTH_SHORT).show();
        });
    }
}
