package com.emrereyhanlioglu.key_listener_plugin;


import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import androidx.annotation.NonNull;
import com.emrereyhanlioglu.key_listener_plugin.service.AccessibilityKeyDetector;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


/** KeyListenerPlugin */
public class KeyListenerPlugin implements FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  //  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "key_listener_plugin");
    channel.setMethodCallHandler(this);
     context = flutterPluginBinding.getApplicationContext();

    EventChannel eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "keyStream");
    eventChannel.setStreamHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
     if(call.method.equals("checkAvailabilityPermission")){
      setupPermission(context);
    }
    else {
      result.notImplemented();
    }
  }

  public static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
    AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
    List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

    for (AccessibilityServiceInfo enabledService : enabledServices) {
      ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
      if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
        return true;
    }

    return false;
  }



  private void setupPermission(Context context){

    if(isAccessibilityServiceEnabled(context, AccessibilityKeyDetector.class)){
      /** if not construct intent to request permission */
      Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      /** request permission via start activity for result */
      context.startActivity(intent);
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }


  /// Stream methods
  @Override
  public void onListen(Object arguments, EventChannel.EventSink events) {
    System.out.println("ON LISTEN CALLED");
    AccessibilityKeyDetector.mEventSink = events;
  }

  @Override
  public void onCancel(Object arguments) {
    AccessibilityKeyDetector.mEventSink = null;
  }

}
