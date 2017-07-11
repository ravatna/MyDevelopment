//
//  SharedInfo.swift
//  Susco Smart Member
//
//  Created by Tyche on 6/26/17.
//  Copyright © 2017 TYCHE. All rights reserved.
//

import Foundation

class SharedInfo: NSObject {

    var json:[String: AnyObject]?
    var jsonTransaction:[String: AnyObject]?
    var jsonGift:[String: AnyObject]?
    var jsonBanner:[String: AnyObject]?
    var currentDevice : String = "45" // 67, 67+
    var imgBase64:String = ""
    
    // let url = URL(string: "http://suscoapidev-iCRM.atlasicloud.com/V2/Security/login_customer_susco");
    //let url = URL(string: "http://192.168.88.196/SUSCOAPIV2/Security/login_customer_susco");

    
    var serviceUrl:String = "http://suscoapidev-iCRM.atlasicloud.com/V2/";
    
    static var getInstance = SharedInfo()
    
     func clear() -> Void {
        json = nil
        jsonGift = nil
        jsonTransaction = nil
        
        
    }

}
