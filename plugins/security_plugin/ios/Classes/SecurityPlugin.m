#import "SecurityPlugin.h"
#if __has_include(<security_plugin/security_plugin-Swift.h>)
#import <security_plugin/security_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "security_plugin-Swift.h"
#endif

@implementation SecurityPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSecurityPlugin registerWithRegistrar:registrar];
}
@end
