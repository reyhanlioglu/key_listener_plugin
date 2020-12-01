package com.emrereyhanlioglu.key_listener_plugin;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import io.flutter.app.FlutterActivity;


public class MainActivity extends FlutterActivity {


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        System.out.println("ON KEY DOWN CALLED");

//this prevents the key from performing the base function. Replace with super.onKeyDown to let it perform it's original function, after being consumed by your app.
        return true;

    }




}
