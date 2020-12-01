import 'dart:async';

import 'package:flutter/services.dart';

class KeyListenerPlugin {
  static const MethodChannel _channel =
      const MethodChannel('key_listener_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<int> getKeyStream() async {
    final int keyStream = await _channel.invokeMethod('getKeyStream');
    return keyStream;
  }

  static Future<bool> checkAvailabilityPermission() async {
    print('STARTING HERE');
    final bool hasPermission =
        await _channel.invokeMethod('checkAvailabilityPermission');
    return hasPermission;
  }

  static Future<bool> listenKeyEvents() async {
    print('STARTING HERE');
    final bool hasPermission = await _channel.invokeMethod('listenKeyEvents');
    return hasPermission;
  }
}
