//
//  AppDelegate.swift
//  FCM Notification Tester
//
//  Created by Vinicius Soares on 09/07/21.
//

import UIKit
import UserNotifications

import Firebase

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate {
  var window: UIWindow?

  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication
                     .LaunchOptionsKey: Any]?) -> Bool {
    // Starts the Firebase library
    FirebaseApp.configure()
    
    /**
        Create the NotificationCenter so the app asks the user for notification permissions and sets
        the resources used by them, like alerts, badges and sound. The way this setting is done
        depends on the version of iOS, so it is checked to use the appropriate code.
     */
    if #available(iOS 10.0, *) {
      UNUserNotificationCenter.current().delegate = self

      let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
      UNUserNotificationCenter.current().requestAuthorization(
        options: authOptions,
        completionHandler: { _, _ in }
      )
    } else {
      let settings: UIUserNotificationSettings =
        UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
      application.registerUserNotificationSettings(settings)
    }

    /**
        Finally, register the app, during its initialization, to receive remote notifications so FCM generated notifications
        can be handled and showed in the app and in the notification center.
     */
    application.registerForRemoteNotifications()

    return true
  }
}
