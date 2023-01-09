import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:otus_plugin_bluetooth/otus_plugin_bluetooth_method_channel.dart';

void main() {
  MethodChannelOtusPluginBluetooth platform = MethodChannelOtusPluginBluetooth();
  const MethodChannel channel = MethodChannel('otus_plugin_bluetooth');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
