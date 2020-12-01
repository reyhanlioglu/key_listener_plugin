package com.emrereyhanlioglu.key_listener_plugin;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;

import com.emrereyhanlioglu.key_listener_plugin.service.AccessibilityKeyDetector;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** KeyListenerPlugin */
public class KeyListenerPlugin extends AccessibilityService implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  //  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;
  private Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "key_listener_plugin");
    channel.setMethodCallHandler(this);
     context = flutterPluginBinding.getApplicationContext();

  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }
    else if(call.method.equals("getKeyStream")){
      result.success(11);
    }
    else if(call.method.equals("checkAvailabilityPermission")){
      System.out.println("STARTED");
      boolean res = setupPermission(context);

      result.success(res);
    }else if(call.method.equals("connectToService")){
      Intent service = new Intent(context, KeyListenerService.class);
      context.startService(service);
      System.out.println("SERVICE IS STARTED");

      Intent keyDetectorService = new Intent(context, AccessibilityKeyDetector.class);
      context.startService(keyDetectorService);

      result.success(true);
    }

    else {
      result.notImplemented();
    }
  }

  private boolean setupPermission(Context context){
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
      context.startActivity(intent);
      return false;
    } else {
      return true;
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }


  /// Activity methods
  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();

  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }

  private final String TAG = "AccessKeyDetector";

  @Override
  public boolean onKeyEvent(KeyEvent event) {
    Log.d(TAG,"Key pressed via accessibility is: "+event.getKeyCode());
    //This allows the key pressed to function normally after it has been used by your app.
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
}
