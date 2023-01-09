import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'otus_plugin_bluetooth_platform_interface.dart';

/// An implementation of [OtusPluginBluetoothPlatform] that uses method channels.
class MethodChannelOtusPluginBluetooth extends OtusPluginBluetoothPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('otus_plugin_bluetooth');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
