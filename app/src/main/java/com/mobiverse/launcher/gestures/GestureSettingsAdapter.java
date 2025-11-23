package com.mobiverse.launcher.gestures;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mobiverse.launcher.R;

import java.util.List;

public class GestureSettingsAdapter extends RecyclerView.Adapter<GestureSettingsAdapter.GestureViewHolder> {

    private Context context;
    private List<GestureManager.GestureType> gestureTypes;
    private GestureManager gestureManager;

    public GestureSettingsAdapter(Context context, List<GestureManager.GestureType> gestureTypes, GestureManager gestureManager) {
        this.context = context;
        this.gestureTypes = gestureTypes;
        this.gestureManager = gestureManager;
    }

    @NonNull
    @Override
    public GestureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gesture_setting, parent, false);
        return new GestureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GestureViewHolder holder, int position) {
        GestureManager.GestureType gestureType = gestureTypes.get(position);
        holder.gestureName.setText(getGestureName(gestureType));
        
        GestureManager.GestureAction currentAction = gestureManager.getActionForGesture(gestureType);
        holder.gestureAction.setText(getActionName(currentAction));

        holder.itemView.setOnClickListener(v -> showActionPicker(gestureType));
    }

    @Override
    public int getItemCount() {
        return gestureTypes.size();
    }

    private void showActionPicker(GestureManager.GestureType gestureType) {
        GestureManager.GestureAction[] actions = GestureManager.GestureAction.values();
        String[] actionNames = new String[actions.length];
        for (int i = 0; i < actions.length; i++) {
            actionNames[i] = getActionName(actions[i]);
        }

        new AlertDialog.Builder(context)
            .setTitle("Select Action for " + getGestureName(gestureType))
            .setItems(actionNames, (dialog, which) -> {
                GestureManager.GestureAction selectedAction = actions[which];
                gestureManager.setGestureAction(gestureType, selectedAction, null);
                notifyDataSetChanged(); // Update UI
            })
            .show();
    }

    class GestureViewHolder extends RecyclerView.ViewHolder {
        TextView gestureName;
        TextView gestureAction;

        public GestureViewHolder(@NonNull View itemView) {
            super(itemView);
            gestureName = itemView.findViewById(R.id.tv_gesture_name);
            gestureAction = itemView.findViewById(R.id.tv_gesture_action);
        }
    }

    private String getGestureName(GestureManager.GestureType type) {
        switch (type) {
            case SWIPE_UP: return "Swipe Up";
            case SWIPE_DOWN: return "Swipe Down";
            case DOUBLE_TAP: return "Double Tap";
            case HOME_BUTTON_TAP: return "Home Button Tap";
            case SWIPE_LEFT: return "Swipe Left";
            case SWIPE_RIGHT: return "Swipe Right";
            default: return "Unknown";
        }
    }

    private String getActionName(GestureManager.GestureAction action) {
        switch (action) {
            case OPEN_APP_DRAWER: return "Open App Drawer";
            case SHOW_NOTIFICATIONS: return "Show Notifications";
            case QUICK_SEARCH: return "Quick Search";
            case VOICE_ASSISTANT: return "Voice Assistant";
            case LAUNCH_APP: return "Launch App";
            case SHOW_RECENTS: return "Show Recents";
            case LOCK_SCREEN: return "Lock Screen";
            case NONE: return "None";
            default: return "Unknown";
        }
    }
}