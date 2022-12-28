#import "OtusBluetoothPlugin.h"
#if __has_include(<otus_bluetooth_plugin/otus_bluetooth_plugin-Swift.h>)
#import <otus_bluetooth_plugin/otus_bluetooth_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "otus_bluetooth_plugin-Swift.h"
#endif

@implementation OtusBluetoothPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftOtusBluetoothPlugin registerWithRegistrar:registrar];
}
@end
