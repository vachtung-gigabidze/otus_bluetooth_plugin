
import 'otus_plugin_bluetooth_platform_interface.dart';

class OtusPluginBluetooth {
  Future<String?> getPlatformVersion() {
    return OtusPluginBluetoothPlatform.instance.getPlatformVersion();
  }
}
