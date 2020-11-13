#include "AppDelegate.h"
#include "GeneratedPluginRegistrant.h"
#import "MainViewController.h"
#import <UIKit/UIKit.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    /** 设置主控制器继承FlutterViewController*/
    MainViewController * VC = [[MainViewController alloc]init];
    UINavigationController * NVC = [[UINavigationController alloc]initWithRootViewController:VC];
    [self.window setRootViewController:NVC];
    /** flutter插件通道代理*/
  [GeneratedPluginRegistrant registerWithRegistry:self];
  // Override point for customization after application launch.
  return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

@end
