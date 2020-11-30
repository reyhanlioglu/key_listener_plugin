package com.emrereyhanlioglu.key_listener_plugin;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** KeyListenerPlugin */
public class KeyListenerPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;

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
    }

    else if(call.method.equals("listenKeyEvents")){
      System.out.println("STARTED");

      MainActivity mainActivity = new MainActivity();
      result.success(mainActivity.checkAccessibilityPermission());
    }
    else {
      result.notImplemented();
    }
  }

  private boolean setupPermission(Context context){
    int accessEnabled=0;
    try {
      System.out.println("TRY 1");
      accessEnabled = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
    } catch (Settings.SettingNotFoundException e) {
      System.out.println("CATCH Exception");
      e.printStackTrace();
    }
    if (accessEnabled==0) {
      /** if not construct intent to request permission */
      Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      /** request permission via start activity for result */
      System.out.println("STARTING INTENT");
      context.startActivity(intent);
      return false;
    } else {
      System.out.println("HAS ACCESS");
      return true;
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
