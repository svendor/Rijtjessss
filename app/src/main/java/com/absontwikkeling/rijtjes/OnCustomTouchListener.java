package com.absontwikkeling.rijtjes;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnCustomTouchListener implements View.OnTouchListener {

    private GestureDetector gestureDetector;

    public OnCustomTouchListener(Context ctx) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onSingleTap();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleTouch();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
            boolean result = false;
            try {
                float deltaY = e2.getY() - e1.getY();
                float deltaX = e2.getX() - e1.getX();
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > SWIPE_THRESHOLD && Math.abs(vx) > SWIPE_VELOCITY_THRESHOLD) {
                        if (deltaX > 0) {
                            onSwipeRight();
                            result = true;
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSingleTap() {
    }

    public void onDoubleTouch() {
    }

}
