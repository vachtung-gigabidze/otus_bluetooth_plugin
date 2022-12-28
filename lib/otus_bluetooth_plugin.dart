
import 'otus_bluetooth_plugin_platform_interface.dart';

class OtusBluetoothPlugin {
  Future<String?> getPlatformVersion() {
    return OtusBluetoothPluginPlatform.instance.getPlatformVersion();
  }
}
