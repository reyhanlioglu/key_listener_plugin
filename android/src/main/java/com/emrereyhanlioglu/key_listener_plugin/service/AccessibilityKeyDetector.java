package com.emrereyhanlioglu.key_listener_plugin.service;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;

public class AccessibilityKeyDetector extends AccessibilityService implements   EventChannel.StreamHandler {

    private final String TAG = "AccessKeyDetector";
    public static EventChannel.EventSink mEventSink = null;

    @Override
    public boolean onKeyEvent(KeyEvent event) {
        //This allows the key pressed to function normally after it has been used by your app.
        if(mEventSink != null){
            System.out.println("Key event " +event.getKeyCode()+" is added into stream");
            mEventSink.success(event.getKeyCode());
        }

        /// TODO: Change keyCode to 286
       if(event.getKeyCode() == 24){
           Intent intent = new Intent();
           intent.setAction("android.intent.action.RUN")
                   .setPackage("com.blueoperation_mobile")
                   .setComponent(new ComponentName("com.blueoperation_mobile","io.flutter.embedding.android.FlutterActivity"))
                   .putExtra("route", "sos");
           startActivity(intent);
       }


        return super.onKeyEvent(event);
    }


    @Override
    protected void onServiceConnected() {
        Log.i(TAG,"Service connected");

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }


    @Override
    public void onInterrupt() {

    }

    /// Stream methods
    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        mEventSink = events;
    }

    @Override
    public void onCancel(Object arguments) {
        mEventSink = null;
    }


}