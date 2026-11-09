package com.msmcollector;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.content.SharedPreferences;

public class ResourceAccessibilityService
        extends AccessibilityService {

    private long lastClickTime = 0;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        SharedPreferences preferences =
            getSharedPreferences("collector", MODE_PRIVATE);

        if (!preferences.getBoolean("auto_collect", false)) {
            return;
        }

        if (event.getEventType() !=
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED &&
            event.getEventType() !=
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }

        long now = System.currentTimeMillis();

        if (now - lastClickTime < 1500) {
            return;
        }

        AccessibilityNodeInfo root = getRootInActiveWindow();

        if (root == null) {
            return;
        }

        if (findAndClick(root)) {
            lastClickTime = now;
        }
    }

    private boolean findAndClick(AccessibilityNodeInfo node) {

        if (node == null) {
            return false;
        }

        CharSequence text = node.getText();
        CharSequence description = node.getContentDescription();

        if (isCollectionLabel(text) ||
            isCollectionLabel(description)) {

            AccessibilityNodeInfo target = node;

            while (target != null && !target.isClickable()) {
                AccessibilityNodeInfo parent = target.getParent();

                if (target != node) {
                    target.recycle();
                }

                target = parent;
            }

            if (target != null) {
                boolean clicked = target.performAction(
                    AccessibilityNodeInfo.ACTION_CLICK
                );
                target.recycle();

                if (clicked) {
                    return true;
                }
            }
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);

            if (child != null) {
                boolean result = findAndClick(child);
                child.recycle();

                if (result) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isCollectionLabel(CharSequence value) {

        if (value == null) {
            return false;
        }

        String label = value.toString().trim();

        return label.equalsIgnoreCase("Collect All")
            || label.equalsIgnoreCase("Collect Food")
            || label.equalsIgnoreCase("Collect Diamonds");
    }

    @Override
    public void onInterrupt() {
    }
}
