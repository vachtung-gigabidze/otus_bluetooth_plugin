import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:otus_bluetooth_plugin/otus_bluetooth_plugin_method_channel.dart';

void main() {
  MethodChannelOtusBluetoothPlugin platform = MethodChannelOtusBluetoothPlugin();
  const MethodChannel channel = MethodChannel('otus_bluetooth_plugin');

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
