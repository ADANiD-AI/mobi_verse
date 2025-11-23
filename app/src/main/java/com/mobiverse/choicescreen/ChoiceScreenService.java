package com.zai.choicescreen;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class ChoiceScreenService extends Service {
    private WindowManager windowManager;
    private View overlayView;
    private WindowManager.LayoutParams params;
    private Button btnChoice1, btnChoice2, btnChoice3, btnClose;
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        createOverlayView();
    }
    
    private void createOverlayView() {
        // Inflate the overlay layout
        LayoutInflater inflater = LayoutInflater.from(this);
        overlayView = inflater.inflate(R.layout.overlay_choice_screen, null);
        
        // Initialize buttons
        initializeButtons();
        
        // Set up window parameters
        setupWindowParams();
        
        // Add the overlay to window manager
        windowManager.addView(overlayView, params);
        
        // Make the overlay draggable
        makeOverlayDraggable();
    }
    
    private void initializeButtons() {
        btnChoice1 = overlayView.findViewById(R.id.btnChoice1);
        btnChoice2 = overlayView.findViewById(R.id.btnChoice2);
        btnChoice3 = overlayView.findViewById(R.id.btnChoice3);
        btnClose = overlayView.findViewById(R.id.btnClose);
        
        // Set click listeners
        btnChoice1.setOnClickListener(v -> handleChoice("Option 1 Selected"));
        btnChoice2.setOnClickListener(v -> handleChoice("Option 2 Selected"));
        btnChoice3.setOnClickListener(v -> handleChoice("Option 3 Selected"));
        btnClose.setOnClickListener(v -> stopSelf());
    }
    
    private void setupWindowParams() {
        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }
        
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );
        
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;
    }
    
    private void makeOverlayDraggable() {
        overlayView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;
            private boolean isDragging = false;
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        isDragging = false;
                        return true;
                        
                    case MotionEvent.ACTION_MOVE:
                        float deltaX = event.getRawX() - initialTouchX;
                        float deltaY = event.getRawY() - initialTouchY;
                        
                        // Check if user is actually dragging
                        if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) {
                            isDragging = true;
                            params.x = initialX + (int) deltaX;
                            params.y = initialY + (int) deltaY;
                            windowManager.updateViewLayout(overlayView, params);
                        }
                        return true;
                        
                    case MotionEvent.ACTION_UP:
                        if (!isDragging) {
                            // If not dragging, let the click event pass through
                            return false;
                        }
                        return true;
                }
                return false;
            }
        });
    }
    
    private void handleChoice(String choice) {
        Toast.makeText(this, choice, Toast.LENGTH_SHORT).show();
        
        // Here you can add your custom logic for each choice
        // For example: send broadcast, save to preferences, call API, etc.
        
        // Example: Send broadcast with the choice
        Intent broadcast = new Intent("com.zai.choicescreen.CHOICE_MADE");
        broadcast.putExtra("choice", choice);
        sendBroadcast(broadcast);
        
        // Optionally hide the overlay after selection
        hideOverlay();
        
        // Show overlay again after 3 seconds (for demo purposes)
        overlayView.postDelayed(this::showOverlay, 3000);
    }
    
    private void hideOverlay() {
        if (overlayView != null) {
            overlayView.setVisibility(View.GONE);
        }
    }
    
    private void showOverlay() {
        if (overlayView != null) {
            overlayView.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // Service will be restarted if killed
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayView != null && windowManager != null) {
            windowManager.removeView(overlayView);
        }
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}