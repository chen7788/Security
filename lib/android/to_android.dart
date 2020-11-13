import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

class ToAndroid {
  /* 通道名称，必须与原生注册的一致*/
  static const _channel = const MethodChannel(
      'com.jzhu.jump/plugin');

  /*
   * 方法名称，必须与flutter注册的一致
   */
  static final Map<String, String> methodNames = const {
    'register': 'register',
  };

  /*
  * 调用原生的方法（带参）
  */
  static Future<dynamic> goNativeWithValue(String methodName,
      [Map<String, String> map]) async {
    if (null == map) {
      dynamic future = await _channel.invokeMethod(methodName);
      return future;
    } else {
      dynamic future = await _channel.invokeMethod(methodName, map);
      return future;
    }
  }
  static Future requestPermission() async {
    // 申请权限
    Map<PermissionGroup, PermissionStatus> permissions =
    await PermissionHandler().requestPermissions([PermissionGroup.location,PermissionGroup.storage]);
    // 申请结果
    PermissionStatus permission =
    await PermissionHandler().checkPermissionStatus(PermissionGroup.location);

    if (permission == PermissionStatus.granted) {
      //权限申请成功

    } else {
      //权限申请被拒绝

      //CargoUtils.showMessage("亲！必须开启位置权限才能进入主程序~");
      // 申请权限
      //    只有当用户同时点选了拒绝开启权限和不再提醒后才会true
      bool isSHow = await PermissionHandler()
          .shouldShowRequestPermissionRationale(PermissionGroup.location);
      if(!isSHow){
        //CargoUtils.showMessage("当前已被您禁用申请权限，请在设置中手动开启后退出重新进入程序");
        await PermissionHandler().openAppSettings();
      }else{
        PermissionStatus ps =
        await PermissionHandler().checkPermissionStatus(PermissionGroup.location);
        if(ps == PermissionStatus.granted){
          //preLogin();
        }else{
          requestPermission();
        }
      }

    }

  }
}