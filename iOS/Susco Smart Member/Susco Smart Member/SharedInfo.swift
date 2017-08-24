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
    var jsonCustomer:[AnyObject]?
    var selectNews:[String:AnyObject]?
    var jsonTransaction:[String: AnyObject]?
    var jsonGift:[String: AnyObject]?
    var jsonBanner:[String: AnyObject]?
    var jsonPriceOil:[String:AnyObject]?
    var currentDevice : String = "45" // 67, 67+
    var imgBase64:String = ""
    var imgMemberData:AnyObject?
    
    var formToken:String = ""
    var cookieToken:String = ""
    let strContact: String = "โปรดติดต่อ SUSCO Call Center 02-428-0029 ต่อ 234,235 เพื่อขอแก้ไขข้อมูล"
    var showProgressDialog = true
    
    // let url = URL(string: "http://suscoapidev-iCRM.atlasicloud.com/V2/Security/login_customer_susco");
    //let url = URL(string: "http://192.168.88.196/SUSCOAPIV2/Security/login_customer_susco");

    var serviceUrl:String = "http://suscoapidev-iCRM.atlasicloud.com/V2/";
    
    static var getInstance = SharedInfo()
    
    func clear() -> Void {
        json = nil
        jsonCustomer = nil
        formToken = ""
        cookieToken = ""
        jsonGift = nil
        jsonBanner = nil
        jsonPriceOil = nil
        jsonTransaction = nil
        selectNews = nil
    }
    
    
    func validThaiIDCard(_ v:String) -> Bool {
        if v.characters.count == 13 {
            return true
        }else{
            return false
        }
        }
    
//    func validThaiIDCard(_ v:Double) -> Bool {
//        var id:Double = v
//        var c:String = String(describing: v).substring(to: 12)
//        
//        var base:Int64 = 1000000000001
//        var basenow:Int
//        var sum:Int = 0
//        
//        
//        var i:int = 13
//        while i > 1 {
    
//            basenow = floor(x:id/base)
//            id = id - basenow * base
//            print(basenow)
//            print("x")
//            print(i)
//            print("=")
//            print((basenow*i))
//            sum += basenow * i
//            base = base /10
//            
//            
//            
//            
//            
//            i-=1
//        }
//        
//    
//        var checkbit:Int = (11- (sum%11)) % 10
//        var cc:Int = dki
        
        
//        return false
//    }
}
