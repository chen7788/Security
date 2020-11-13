import 'dart:async';
import 'package:flutter/services.dart';

typedef OnData = void Function(dynamic event);

class ReceivePlugin{
  static const _channel = const EventChannel('com.jzhu.counter/plugin');
  StreamSubscription _subscription;
  OnData onData;

  static ReceivePlugin _instance = ReceivePlugin._internal();


  static ReceivePlugin get instance => _instance;

  ReceivePlugin._internal() {
    if (_subscription == null) {
      _subscription = _channel.receiveBroadcastStream().listen(_onData);
    }
  }

  _onData(dynamic event) {
    print('-----' +event);
    onData(event);
  }

}