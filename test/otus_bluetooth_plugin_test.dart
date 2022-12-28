import 'package:flutter_test/flutter_test.dart';
import 'package:otus_bluetooth_plugin/otus_bluetooth_plugin.dart';
import 'package:otus_bluetooth_plugin/otus_bluetooth_plugin_platform_interface.dart';
import 'package:otus_bluetooth_plugin/otus_bluetooth_plugin_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockOtusBluetoothPluginPlatform
    with MockPlatformInterfaceMixin
    implements OtusBluetoothPluginPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final OtusBluetoothPluginPlatform initialPlatform = OtusBluetoothPluginPlatform.instance;

  test('$MethodChannelOtusBluetoothPlugin is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelOtusBluetoothPlugin>());
  });

  test('getPlatformVersion', () async {
    OtusBluetoothPlugin otusBluetoothPlugin = OtusBluetoothPlugin();
    MockOtusBluetoothPluginPlatform fakePlatform = MockOtusBluetoothPluginPlatform();
    OtusBluetoothPluginPlatform.instance = fakePlatform;

    expect(await otusBluetoothPlugin.getPlatformVersion(), '42');
  });
}
