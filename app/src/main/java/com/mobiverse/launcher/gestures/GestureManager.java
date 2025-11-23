package com.mobiverse.launcher.gestures;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class GestureManager {

    public enum GestureType {
        SWIPE_UP,
        SWIPE_DOWN,
        DOUBLE_TAP,
        SWIPE_LEFT,
        SWIPE_RIGHT
    }

    public enum GestureAction {
        OPEN_APP_DRAWER,
        SHOW_NOTIFICATIONS,
        LOCK_SCREEN,
        OPEN_CAMERA,
        RUN_CUSTOM_APP,
        NONE
    }

    private static GestureManager instance;
    private Context context;
    private SharedPreferences prefs;
    private Gson gson;

    private Map<GestureType, GestureAction> gestureActionMap;
    private Map<GestureType, String> customAppMap; // For RUN_CUSTOM_APP

    private GestureManager(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = context.getSharedPreferences("launcher_gestures", Context.MODE_PRIVATE);
        this.gson = new Gson();
        loadGestures();
    }

    public static GestureManager getInstance(Context context) {
        if (instance == null) {
            instance = new GestureManager(context);
        }
        return instance;
    }

    public void setGestureAction(GestureType gesture, GestureAction action, String customAppPackage) {
        gestureActionMap.put(gesture, action);
        if (action == GestureAction.RUN_CUSTOM_APP) {
            customAppMap.put(gesture, customAppPackage);
        } else {
            customAppMap.remove(gesture);
        }
        saveGestures();
    }

    public GestureAction getActionForGesture(GestureType gesture) {
        return gestureActionMap.getOrDefault(gesture, GestureAction.NONE);
    }

    public String getCustomAppForGesture(GestureType gesture) {
        return customAppMap.get(gesture);
    }

    private void loadGestures() {
        String actionMapJson = prefs.getString("gesture_action_map", "{}");
        String customAppMapJson = prefs.getString("custom_app_map", "{}");

        gestureActionMap = gson.fromJson(actionMapJson, new TypeToken<HashMap<GestureType, GestureAction>>(){}.getType());
        customAppMap = gson.fromJson(customAppMapJson, new TypeToken<HashMap<GestureType, String>>(){}.getType());

        if (gestureActionMap == null) {
            gestureActionMap = new HashMap<>();
            // Set default gestures
            gestureActionMap.put(GestureType.SWIPE_UP, GestureAction.OPEN_APP_DRAWER);
            gestureActionMap.put(GestureType.SWIPE_DOWN, GestureAction.SHOW_NOTIFICATIONS);
            gestureActionMap.put(GestureType.DOUBLE_TAP, GestureAction.LOCK_SCREEN);
        }
        if (customAppMap == null) {
            customAppMap = new HashMap<>();
        }
    }

    private void saveGestures() {
        String actionMapJson = gson.toJson(gestureActionMap);
        String customAppMapJson = gson.toJson(customAppMap);
        prefs.edit()
            .putString("gesture_action_map", actionMapJson)
            .putString("custom_app_map", customAppMapJson)
            .apply();
    }
}
