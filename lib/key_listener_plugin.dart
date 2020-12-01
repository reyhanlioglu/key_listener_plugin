import 'dart:async';

import 'package:flutter/services.dart';

class KeyListenerPlugin {
  static const MethodChannel _channel =
      const MethodChannel('key_listener_plugin');

  static Stream<int> _keyStream;

  static Future<bool> checkAvailabilityPermission() async {
    print('STARTING HERE');
    final bool hasPermission =
        await _channel.invokeMethod('checkAvailabilityPermission');
    return hasPermission;
  }

  static Stream<int> get keyStream {
    EventChannel _eventChannel = const EventChannel('keyStream');
    if (_keyStream == null) {
      _keyStream =
          _eventChannel.receiveBroadcastStream().map<int>((event) => event);
    }

    return _keyStream;
  }
}
