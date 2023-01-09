import 'package:flutter_test/flutter_test.dart';
import 'package:otus_plugin_bluetooth/otus_plugin_bluetooth.dart';
import 'package:otus_plugin_bluetooth/otus_plugin_bluetooth_platform_interface.dart';
import 'package:otus_plugin_bluetooth/otus_plugin_bluetooth_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockOtusPluginBluetoothPlatform
    with MockPlatformInterfaceMixin
    implements OtusPluginBluetoothPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final OtusPluginBluetoothPlatform initialPlatform = OtusPluginBluetoothPlatform.instance;

  test('$MethodChannelOtusPluginBluetooth is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelOtusPluginBluetooth>());
  });

  test('getPlatformVersion', () async {
    OtusPluginBluetooth otusPluginBluetoothPlugin = OtusPluginBluetooth();
    MockOtusPluginBluetoothPlatform fakePlatform = MockOtusPluginBluetoothPlatform();
    OtusPluginBluetoothPlatform.instance = fakePlatform;

    expect(await otusPluginBluetoothPlugin.getPlatformVersion(), '42');
  });
}
