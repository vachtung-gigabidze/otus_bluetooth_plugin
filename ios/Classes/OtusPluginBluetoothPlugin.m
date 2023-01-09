#import "OtusPluginBluetoothPlugin.h"
#if __has_include(<otus_plugin_bluetooth/otus_plugin_bluetooth-Swift.h>)
#import <otus_plugin_bluetooth/otus_plugin_bluetooth-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "otus_plugin_bluetooth-Swift.h"
#endif

@implementation OtusPluginBluetoothPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftOtusPluginBluetoothPlugin registerWithRegistrar:registrar];
}
@end
