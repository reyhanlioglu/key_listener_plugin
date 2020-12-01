package com.emrereyhanlioglu.key_listener_plugin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import io.flutter.embedding.android.FlutterActivity;


public class MainActivity extends FlutterActivity {

    private final String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAccessibilityPermission(getContext());

    }

    public boolean checkAccessibilityPermission(Context context) {
        int accessEnabled=0;
        try {
            accessEnabled = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        e.printStackTrace();
    }
        if (accessEnabled==0) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            /** request permission via start activity for result */
            startActivity(intent);
            return false;
        } else {
            return true;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG,"Key pressed");

//this prevents the key from performing the base function. Replace with super.onKeyDown to let it perform it's original function, after being consumed by your app.
        return true;

    }
}
