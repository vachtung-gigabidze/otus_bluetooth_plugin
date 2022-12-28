import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'otus_bluetooth_plugin_platform_interface.dart';

/// An implementation of [OtusBluetoothPluginPlatform] that uses method channels.
class MethodChannelOtusBluetoothPlugin extends OtusBluetoothPluginPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('otus_bluetooth_plugin');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
