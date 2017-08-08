//
//  FirstViewController.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/20/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class ScoreViewController: UIViewController {
    @IBOutlet weak var vweGIft: UIView!
    @IBOutlet weak var scrMain: UIScrollView!
    
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var lblScore: UILabel!
    @IBOutlet weak var lblName: UILabel!
    
    @IBOutlet weak var lblDiscountNotReady: UILabel!
    @IBOutlet weak var lblGiftNotReady: UILabel!
    
    
    var isFirst = true
    var yy:Int = 260
    var yy2:Int = 0
    
    func updateInfo() {
        let customer:[AnyObject]
        do{
            customer = SharedInfo.getInstance.json!["customer_detail"] as! [AnyObject]
            
           
            var fname = customer[0]["fname"] as! String
            var lname = customer[0]["lname"] as! String
            var code = customer[0]["member_code"] as! String
            var score = customer[0]["point_summary"] as! String
            var phone = customer[0]["mobile"] as! String
            
            lblName.text = fname + " " + lname
            lblPhone.text = phone
            lblScore.text = score
            
            
            
            
        }catch {
            
        }
        
        
        
        
        
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
        
        
        
        
        //updateScrollViewForHeight()
    }

    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        
        
        if SharedInfo.getInstance.currentDevice == "45" {
            yy =   220
        }
        else if SharedInfo.getInstance.currentDevice == "67"
        {
            yy =  255
        }
            
        else if SharedInfo.getInstance.currentDevice == "67+"
        {
            yy =  300
        }
        
        
        
        self.updateInfo() // update user data
        
        doLoadGift() // load content from servder
        
        
        
    }
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    var bit: Int = 0 // 0 = left, 1 = right
    
    func updateCatalogDiscount(boxWidth:Int,boxHeight:Int) {
        let catalogs:[AnyObject]
        do{

            catalogs = SharedInfo.getInstance.jsonGift!["catalog"] as! [AnyObject]
            var view_tag = 1000
            var btn_tag = 0
            var xx:Int = 10
            var loop = 0
            
            for item in catalogs {
                
                
                
                // ให้สร้างรายการเฉพาะส่วนลด = 0
                if item["check"] as! String == "1" {
                    continue
                }
                
                
                lblDiscountNotReady.isHidden = true
                
                loop += 1
                //print(item)
                
                
                if SharedInfo.getInstance.currentDevice == "45" {
                    xx = 10
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 165
                    }
                }
                else if SharedInfo.getInstance.currentDevice == "67"
                {
                    xx = 10
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 195
                    }
                }
                    
                else if SharedInfo.getInstance.currentDevice == "67+"
                {
                    xx = 10
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 210
                    }
                }

                
                // view group
                let itemView:UIView = UIView (frame: CGRect(x:xx,y:yy, width:boxWidth,height:boxHeight))
                itemView.tag = view_tag
                itemView.autoresizingMask = [.flexibleTopMargin]
                itemView.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 1.0)
                itemView.layer.shadowColor = UIColor.black.cgColor
                itemView.layer.shadowOpacity = 1
                itemView.layer.shadowOffset = CGSize.zero
                itemView.layer.shadowRadius = 1
                
                let lblTitle:UILabel = UILabel(frame:CGRect(x:8,y:(boxHeight-35),width:(boxWidth-8),height:21))
                
                lblTitle.text = item["redeem_item_desc"] as! String
                
                let buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:boxWidth, height: (boxHeight-60) ))
                buttonConnect.tag = btn_tag
                
                let iconString:String = item["picture"] as! String
                
                let decodedData:Data = Data(base64Encoded: iconString)!
                buttonConnect.setImage(UIImage(data:decodedData), for: UIControlState.normal)

                itemView.addSubview(buttonConnect)
                itemView.addSubview(lblTitle)
                
                self.scrMain.addSubview(itemView)
                
                bit = 0
                if ( btn_tag + 1) % 2 == 0 {
                    bit = 1
                    
                    if SharedInfo.getInstance.currentDevice == "45" {
                        yy +=   175
                    }
                    else if SharedInfo.getInstance.currentDevice == "67"
                    {
                        yy +=  225
                    }
                        
                    else if SharedInfo.getInstance.currentDevice == "67+"
                    {
                        yy +=  240
                    }

                }
                
                yy2 = yy
                
                view_tag = view_tag + 1
                btn_tag = btn_tag + 1
                
                itemView.invalidateIntrinsicContentSize()
                
            }

        }catch {
            
        }
        
        scrMain.invalidateIntrinsicContentSize()
        updateScrollViewForHeight()
        
    }
    
    
    func updateCatalogGift(boxWidth:Int,boxHeight:Int) {
        let catalogs:[AnyObject]
        do{
            
            catalogs = SharedInfo.getInstance.jsonGift!["catalog"] as! [AnyObject]
            var view_tag = 2000
            var btn_tag = 0
            var xx:Int = 10
            
            yy2 += 0
            
            // if not end on two column by discount group
            if(bit == 0){
                yy2 += 240
            }
            
            vweGIft.isHidden = false
            
 
            var f:CGRect = vweGIft.frame;
            f.origin.x = 0; // new x
            f.origin.y = CGFloat(yy2) // new y
            vweGIft.frame = f
           
            
            
            yy2 += 35
            
//             f = lblGiftNotReady.frame;
//            f.origin.x = 0; // new x
//            f.origin.y = CGFloat(yy2) // new y
//            lblGiftNotReady.frame = f
//            
//            yy2 += 45
            
            
            for item in catalogs {
                
                // ให้สร้างรายการเฉพาะของแลก = 1
                if item["check"] as! String == "0" {
                    continue
                }
                
                
                lblGiftNotReady.isHidden = true
                
                if SharedInfo.getInstance.currentDevice == "45" {
                    xx = 10
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 165
                    }
                }
                else if SharedInfo.getInstance.currentDevice == "67"
                {
                    xx = 10
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 185
                    }
                }
                    
                else if SharedInfo.getInstance.currentDevice == "67+"
                {
                    xx = 10
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 210
                    }
                }
                
                // view group
                let itemView:UIView = UIView (frame: CGRect(x:xx,y:yy2, width:boxWidth,height:boxHeight))
                itemView.tag = view_tag
                itemView.autoresizingMask = [.flexibleTopMargin]
                
                itemView.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 1.0)
                itemView.layer.shadowColor = UIColor.black.cgColor
                itemView.layer.shadowOpacity = 1
                itemView.layer.shadowOffset = CGSize.zero
                itemView.layer.shadowRadius = 1
                
                
                let lblTitle:UILabel = UILabel(frame:CGRect(x:8,y:(boxHeight-35),width:(boxWidth-8),height:21))
                
                lblTitle.text = item["redeem_item_desc"] as! String
                
                let buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:boxWidth, height:boxHeight))
                buttonConnect.tag = btn_tag
                
                let iconString:String = item["picture"] as! String
                
                let decodedData:Data = Data(base64Encoded: iconString)!
                buttonConnect.setImage(UIImage(data:decodedData), for: UIControlState.normal)
                
                itemView.addSubview(buttonConnect)
                itemView.addSubview(lblTitle)
                
                self.scrMain.addSubview(itemView)
                
                if (btn_tag + 1) % 2 == 0 {
                    
                    
                    if SharedInfo.getInstance.currentDevice == "45" {
                        yy +=   175
                    }
                    else if SharedInfo.getInstance.currentDevice == "67"
                    {
                        yy +=  225
                    }
                        
                    else if SharedInfo.getInstance.currentDevice == "67+"
                    {
                        yy +=  240
                    }
                    
                }
                
                view_tag = view_tag + 1
                btn_tag = btn_tag + 1
                
                itemView.invalidateIntrinsicContentSize()
                
            }
            
            
            
        }catch {
            
        }
        
        scrMain.invalidateIntrinsicContentSize()
        updateScrollViewForHeight()
        
    }// end func
    
    
    
    //mark: -- update scroll view about height
    func updateScrollViewForHeight(){
        let lastView : UIView! = scrMain.subviews.last
        // print(lastView.tag)
        let height:Float = Float(lastView.frame.size.height)
        
        let pos:Float = Float(lastView.frame.origin.y)
        let sizeOfContent = height + pos + 100
        
        
        scrMain.contentSize.height = CGFloat(sizeOfContent)
    }
    
 
    
    // mark: -- make connection to server
    func doLoadGift(){
        
        let customer:[AnyObject]
        
        customer = SharedInfo.getInstance.json!["customer_detail"] as! [AnyObject]
        
        let phone = customer[0]["mobile"] as! String
        let formToken:String = SharedInfo.getInstance.json!["formToken"] as! String
        let cookieToken:String = SharedInfo.getInstance.json!["cookieToken"] as! String
        
        
        // create post request
        
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/GetCatalogForMember/catalog_for_member_mobile")!
        
        let jsonDict = [
            "mobile": phone
            ,"formToken": formToken
            ,"cookieToken": cookieToken ]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: jsonDict, options: [])
        
        var request = URLRequest(url: url)
        request.httpMethod = "post"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsonData
        
        
        
        let task = URLSession.shared.dataTask(with: request) {
            (data, response, error) in
            
            // dismiss alert view
            //  self.loadingDialog.dismiss(animated: true, completion: nil)
            
            
            if let error = error {
                print("error:", error)
                return
            }
            
            do {
                guard let data = data else { return }
                guard let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: AnyObject] else { return }
                
                
                if json["msg"] as! String != "ลูกค้ายังมีคะแนนไม่สะสม" {
                    // assign result from
                    SharedInfo.getInstance.jsonGift = json;
                    DispatchQueue.main.async(){
                        
                        self.lblGiftNotReady.isHidden = false;
                        self.lblDiscountNotReady.isHidden = false;
                        
                        var w:Int = 145
                        var h:Int = 165
                        
                        if SharedInfo.getInstance.currentDevice == "45" {
                            w = 145
                            h = 165
                        }
                        else if SharedInfo.getInstance.currentDevice == "67"
                        {
                            w = 170
                            h = 200
                        }
                            
                        else if SharedInfo.getInstance.currentDevice == "67+"
                        {
                            w = 195
                            h = 220
                        }

                        
                        
                        self.updateCatalogDiscount(boxWidth: w,boxHeight: h) // update discount block
                        self.updateCatalogGift(boxWidth: w,boxHeight: h) // update gift block
                    }
                    
                    //print(json)
                }
                else
                {
                    self.vweGIft.isHidden = true;
                    self.lblGiftNotReady.isHidden = true;
                    self.lblDiscountNotReady.isHidden = true;
                }
                
             
                
            } catch {
                print("error:", error)
            }
            
                    }
        
        task.resume()
        
    }
}
