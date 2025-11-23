package com.mobiverse.launcher.gestures;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GestureListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private final GestureManager gestureManager;
    private final GestureCallback callback;

    public GestureListener(Context context, GestureCallback callback) {
        this.gestureManager = GestureManager.getInstance(context);
        this.callback = callback;
        this.gestureDetector = new GestureDetector(context, new SimpleGestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class SimpleGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            handleGesture(GestureManager.GestureType.DOUBLE_TAP);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        handleGesture(GestureManager.GestureType.SWIPE_RIGHT);
                    } else {
                        handleGesture(GestureManager.GestureType.SWIPE_LEFT);
                    }
                    return true;
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        handleGesture(GestureManager.GestureType.SWIPE_DOWN);
                    } else {
                        handleGesture(GestureManager.GestureType.SWIPE_UP);
                    }
                    return true;
                }
            }
            return false;
        }
    }

    private void handleGesture(GestureManager.GestureType gesture) {
        GestureManager.GestureAction action = gestureManager.getActionForGesture(gesture);
        String customApp = gestureManager.getCustomAppForGesture(gesture);
        callback.onGestureTriggered(action, customApp);
    }

    public interface GestureCallback {
        void onGestureTriggered(GestureManager.GestureAction action, String customAppPackage);
    }
}
