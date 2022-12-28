import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'otus_bluetooth_plugin_method_channel.dart';

abstract class OtusBluetoothPluginPlatform extends PlatformInterface {
  /// Constructs a OtusBluetoothPluginPlatform.
  OtusBluetoothPluginPlatform() : super(token: _token);

  static final Object _token = Object();

  static OtusBluetoothPluginPlatform _instance = MethodChannelOtusBluetoothPlugin();

  /// The default instance of [OtusBluetoothPluginPlatform] to use.
  ///
  /// Defaults to [MethodChannelOtusBluetoothPlugin].
  static OtusBluetoothPluginPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [OtusBluetoothPluginPlatform] when
  /// they register themselves.
  static set instance(OtusBluetoothPluginPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
