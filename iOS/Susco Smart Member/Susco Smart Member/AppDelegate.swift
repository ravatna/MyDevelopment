//
//  AppDelegate.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/20/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
   


    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        
        let font:UIFont = UIFont (name: "Kanit-Regular", size :14)!
           // UITabBarItem.appearance().setTitleTextAttributes(<#T##attributes: [String : Any]?##[String : Any]?#>, for: <#T##UIControlState#>)
         UITabBarItem.appearance().setTitleTextAttributes([NSFontAttributeName: font], for: .normal)
        //UISegmentedControlSegment.setTitleTextAttributes([NSFontAttributeName: font])
        // dynamic use story board
        let storyBoard:UIStoryboard = self.grabStoryBoard()
        self.window?.rootViewController = storyBoard.instantiateInitialViewController()
        self.window?.makeKeyAndVisible()
        /// end dynamic use story board
        
       
        
        
        return true
    }

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }


    func grabStoryBoard() -> UIStoryboard {
        
        let screenHeight = UIScreen.main.bounds.height
        let screenWidth = UIScreen.main.bounds.width
        let storyBoard:UIStoryboard!
        
        switch screenHeight {
//        case 480:
//            storyBoard = UIStoryboard.init(name: "Main", bundle: nil)
//            break
//        case 568:
//            storyBoard = UIStoryboard.init(name: "Main", bundle: nil)
//            break
        case 667:
            storyBoard = UIStoryboard.init(name: "Main67", bundle: nil)
            SharedInfo.getInstance.currentDevice = "67"
            break
        case 736:
            storyBoard = UIStoryboard.init(name: "Main67Plus", bundle: nil)
            SharedInfo.getInstance.currentDevice = "67+"
            break
        default:
            storyBoard = UIStoryboard.init(name: "Main", bundle: nil)
            SharedInfo.getInstance.currentDevice = "45"
            break
        }
        return storyBoard
    }
}

