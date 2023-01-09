import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'otus_plugin_bluetooth_method_channel.dart';

abstract class OtusPluginBluetoothPlatform extends PlatformInterface {
  /// Constructs a OtusPluginBluetoothPlatform.
  OtusPluginBluetoothPlatform() : super(token: _token);

  static final Object _token = Object();

  static OtusPluginBluetoothPlatform _instance = MethodChannelOtusPluginBluetooth();

  /// The default instance of [OtusPluginBluetoothPlatform] to use.
  ///
  /// Defaults to [MethodChannelOtusPluginBluetooth].
  static OtusPluginBluetoothPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [OtusPluginBluetoothPlatform] when
  /// they register themselves.
  static set instance(OtusPluginBluetoothPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
