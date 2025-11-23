package com.mobiverse.launcher.gestures;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mobiverse.launcher.R;

import java.util.ArrayList;
import java.util.List;

public class GestureSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_settings);

        RecyclerView recyclerView = findViewById(R.id.rv_gesture_settings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GestureManager gestureManager = GestureManager.getInstance(this);
        List<GestureManager.GestureType> gestureTypes = getAvailableGestures();

        GestureSettingsAdapter adapter = new GestureSettingsAdapter(this, gestureTypes, gestureManager);
        recyclerView.setAdapter(adapter);
    }

    private List<GestureManager.GestureType> getAvailableGestures() {
        List<GestureManager.GestureType> gestures = new ArrayList<>();
        gestures.add(GestureManager.GestureType.SWIPE_UP);
        gestures.add(GestureManager.GestureType.SWIPE_DOWN);
        gestures.add(GestureManager.GestureType.DOUBLE_TAP);
        gestures.add(GestureManager.GestureType.HOME_BUTTON_TAP);
        gestures.add(GestureManager.GestureType.SWIPE_LEFT);
        gestures.add(GestureManager.GestureType.SWIPE_RIGHT);
        return gestures;
    }
}