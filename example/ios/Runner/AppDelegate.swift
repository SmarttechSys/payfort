import UIKit
import Flutter
@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
  ) -> Bool {
  
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }


 override func applicationDidEnterBackground(_ application: UIApplication) {
        var bgTask: UIBackgroundTaskIdentifier = .invalid
        bgTask = application.beginBackgroundTask(expirationHandler: {
            application.endBackgroundTask(bgTask)
            bgTask = .invalid
        })
    }
    
    func getId() -> String { UIDevice.current.identifierForVendor?.uuidString ?? UUID().uuidString }
}
