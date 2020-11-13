import 'dart:async';

import 'package:flutter/services.dart';

class SecurityPlugin {
  static const MethodChannel _channel =
      const MethodChannel('security_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
