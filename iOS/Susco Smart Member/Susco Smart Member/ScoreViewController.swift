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
    
    var refreshControl:UIRefreshControl!
    
    
    var isFirst = true
    var yy:Int = 260
    var yy2:Int = 0
    
    var a_itemView = Array<UIView>()
    
    
    func updateInfo() {
        
            
            lblName.text = SharedInfo.getInstance.fname.replacingOccurrences(of: "\n", with: "") + " " + SharedInfo.getInstance.lname.replacingOccurrences(of: "\n", with: "")
            lblPhone.text = SharedInfo.getInstance.mobile
            lblScore.text = SharedInfo.getInstance.point_summary
            
       

    }
    

    
    
    ////////////////////////////////////////////////
    func GetImageBase64_News(_ button:UIButton,_ imageCode:String){
        
        
        
        
        // create post request
        
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/GetPicture/getimagebase64")!
        let jsonDict = [
            "member_code": SharedInfo.getInstance.member_code
            ,"imagecode": imageCode
            ,"Width": "1024"
            ,"Height": "400"
            ,"checkWidth": "0"
            ,"CustomWidthHigth": "1"
            ,"formToken": SharedInfo.getInstance.formToken
            ,"cookieToken": SharedInfo.getInstance.cookieToken
        ]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: jsonDict, options: [])
        
        var request = URLRequest(url: url)
        request.httpMethod = "post"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsonData
        
        let task = URLSession.shared.dataTask(with: request) {
            (data, response, error) in
            
            
            if let error = error {
                print("error:", error)
                return
            }
            
            do {
                guard let data = data else { return }
                guard let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: AnyObject] else { return }
                
                DispatchQueue.main.async(execute: {
                    
                    // call update screen by first time only
                    
                    if json["success"] as! Bool == true{
                        
                        var iconString:String
                        var decodedData:Data
                        
                        // ตรวจสอบข้อมูลว่าเป็นค่าที่ว่างหรือไม่
                        iconString = json["imagebase64"] as! String
                        
                        if iconString != "" {
                            decodedData = Data(base64Encoded: iconString)!
                            button.setBackgroundImage(UIImage(data:decodedData), for: UIControlState.normal)
                            button.layoutIfNeeded()
                        } // end if
                    }
                })
            } catch {
                print("error:", error)
            }
        }
        
        task.resume()
        
    } // end func
    
    
    func handleRefresh(_ refreshControl: UIRefreshControl) {
        // Do some reloading of data and update the table view's data source
        // Fetch more objects from a web service, for example...
        
        // Simply adding an object to the data source for this example
        
        if SharedInfo.getInstance.currentDevice == "45" {
            yy =   280
        }
        else if SharedInfo.getInstance.currentDevice == "67"
        {
            yy =  265
        }
            
        else if SharedInfo.getInstance.currentDevice == "67+"
        {
            yy =  320
        }
        
        ScoreForMember()
        doLoadGift() // load content from servder
        
        
        //refreshControl.endRefreshing()
    }

    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        
        
        refreshControl = UIRefreshControl()
        
        refreshControl.backgroundColor = UIColor.white
        refreshControl.tintColor = UIColor.darkGray
        refreshControl?.addTarget(self, action: #selector(handleRefresh), for: UIControlEvents.valueChanged)
        
        
        scrMain.addSubview(refreshControl)
        
        scrMain.contentSize.height = UIScreen.main.bounds.height
        //print(scrMain.contentSize.height)
        
        if SharedInfo.getInstance.currentDevice == "45" {
            yy =   280
        }
        else if SharedInfo.getInstance.currentDevice == "67"
        {
            yy =  265
        }
            
        else if SharedInfo.getInstance.currentDevice == "67+"
        {
            yy =  320
        }
        
        self.updateInfo() // update user data
        
        doLoadGift() // load content from servder
    }
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    var bit: Int = 0 // 0 = left, 1 = right
    
    
    
    func removeItemFromView(){
        for view in a_itemView {
            view.removeFromSuperview()
            
        }
        a_itemView.removeAll()
    
    } // .End view
    
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
                    xx = 25
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 165
                    }
                }
                else if SharedInfo.getInstance.currentDevice == "67"
                {
                    xx = 40
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 205
                    }
                }
                    
                else if SharedInfo.getInstance.currentDevice == "67+"
                {
                    xx = 40
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 220
                    }
                }

                
                // view group
                var itemView:UIView = UIView (frame: CGRect(x:xx,y:yy, width:boxWidth,height:boxHeight))
                var lblTitle:UILabel = UILabel(frame:CGRect(x:8, y:(boxHeight-35), width:(boxWidth-8), height:21))
                var buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:boxWidth, height: (boxHeight-60) ))
                
                if SharedInfo.getInstance.currentDevice == "45" {
                    itemView = UIView (frame: CGRect(x:xx,y:yy, width:boxWidth+25,height:boxHeight))
                    
                    
                    buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:boxWidth + 25, height: (boxHeight-40) ))
                    lblTitle.font  = UIFont (name: "Kanit-Regular", size :12)
                     lblTitle = UILabel(frame:CGRect(x:8, y:(boxHeight-35), width:(boxWidth+17), height:21))
                }
                else if SharedInfo.getInstance.currentDevice == "67"
                {
                   buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:boxWidth, height: (boxHeight-70) ))
                    lblTitle.font  = UIFont (name: "Kanit-Regular", size :14)
                }
                    
                else if SharedInfo.getInstance.currentDevice == "67+"
                {
                    buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:boxWidth, height: (boxHeight-80) ))
                    lblTitle.font  = UIFont (name: "Kanit-Regular", size :14)
                }

                
                itemView.tag = view_tag
                itemView.autoresizingMask = [.flexibleTopMargin]
                itemView.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 1.0)
                itemView.layer.shadowColor = UIColor.black.cgColor
                itemView.layer.shadowOpacity = 1
                itemView.layer.shadowOffset = CGSize.zero
                itemView.layer.shadowRadius = 1
                
                lblTitle.text = item["redeem_item_desc"] as! String
                
                buttonConnect.tag = btn_tag
                
                let iconString:String = item["picture"] as! String
                
                let decodedData:Data = Data(base64Encoded: iconString)!
                buttonConnect.setImage(UIImage(data:decodedData), for: UIControlState.normal)

                itemView.addSubview(buttonConnect)
                itemView.addSubview(lblTitle)
                
                
                a_itemView.append(itemView)
                
                
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
        
//        scrMain.invalidateIntrinsicContentSize()
//        updateScrollViewForHeight()
        
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
           
            
            
            yy2 += 40
            
 
            
            var iCheck = 1
            var iHaveItem = 0
            for item in catalogs {
                
                // ให้สร้างรายการเฉพาะของแลก = 1
                if item["check"] as! String == "0" {
                    iHaveItem += 1
                    
                    continue
                    
                }
                
                
                lblGiftNotReady.isHidden = true
                
                if SharedInfo.getInstance.currentDevice == "45" {
                    xx = 25
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 165
                    }
                }
                else if SharedInfo.getInstance.currentDevice == "67"
                {
                    xx = 40
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 205
                    }
                }
                    
                else if SharedInfo.getInstance.currentDevice == "67+"
                {
                    xx = 40
                    if (btn_tag + 1) % 2 == 0 {
                        xx = 220
                    }
                }
                
                // view group
                var itemView:UIView = UIView (frame: CGRect(x:xx,y:yy2, width:boxWidth,height:boxHeight))
                var buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:boxWidth, height:boxHeight-70))
                var lblTitle:UILabel = UILabel(frame:CGRect(x:8,y:(boxHeight-35),width:(boxWidth-8),height:21))
                

                if SharedInfo.getInstance.currentDevice == "45" {
 
                    // check current is last item or not.
                    if iCheck+1 == (catalogs.count - (iHaveItem-1)) {
                        // display last item on center screen
                         itemView = UIView (frame: CGRect(x:95 ,y:yy2, width:boxWidth+25,height:boxHeight))
                    }else{
                        // display current left and right
                         itemView = UIView (frame: CGRect(x:xx,y:yy2, width:boxWidth+25,height:boxHeight))
                    }
                    
                    buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:boxWidth + 25, height: (boxHeight-40) ))
                    lblTitle.font  = UIFont (name: "Kanit-Regular", size :12)
                    lblTitle = UILabel(frame:CGRect(x:8, y:(boxHeight-35), width:(boxWidth+17), height:21))

                    
                    
                    
                    
                }
                else if SharedInfo.getInstance.currentDevice == "67"
                {
                    
                    
                    if iCheck+1 == (catalogs.count - (iHaveItem-1)) {
                        // display last item on center screen
                        itemView = UIView (frame: CGRect(x:123 ,y:yy2, width:boxWidth,height:boxHeight))
                    }else{
                        // display current left and right
                        itemView = UIView (frame: CGRect(x:xx,y:yy2, width:boxWidth,height:boxHeight))
                    }
                    
                    buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:boxWidth, height: (boxHeight-70) ))
                    lblTitle.font  = UIFont (name: "Kanit-Regular", size :14)
                }
                    
                else if SharedInfo.getInstance.currentDevice == "67+"
                {
                    
                    if iCheck+1 == (catalogs.count - (iHaveItem-1)) {
                        // display last item on center screen
                        itemView = UIView (frame: CGRect(x:123 ,y:yy2, width:boxWidth,height:boxHeight))
                    }else{
                        // display current left and right
                        itemView = UIView (frame: CGRect(x:xx,y:yy2, width:boxWidth,height:boxHeight))
                    }
                    
                    buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:boxWidth, height: (boxHeight-80) ))
                    lblTitle.font  = UIFont (name: "Kanit-Regular", size :14)
                }
                
                
                
                ///////////////////////////////////////////////////
                itemView.tag = view_tag
                itemView.autoresizingMask = [.flexibleTopMargin]
                
                itemView.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 1.0)
                itemView.layer.shadowColor = UIColor.black.cgColor
                itemView.layer.shadowOpacity = 1
                itemView.layer.shadowOffset = CGSize.zero
                itemView.layer.shadowRadius = 1
                
                
                
                lblTitle.text = item["redeem_item_desc"] as! String
                // lblTitle.font  = UIFont (name: "Kanit-Regular", size :17)
                
                
                buttonConnect.tag = btn_tag
                
                let iconString:String = item["picture"] as! String
                
                let decodedData:Data = Data(base64Encoded: iconString)!
                buttonConnect.setImage(UIImage(data:decodedData), for: UIControlState.normal)
                
                itemView.addSubview(buttonConnect)
                itemView.addSubview(lblTitle)
                
                // add list item to array list for remove next time
                a_itemView.append(itemView)
                
                self.scrMain.addSubview(itemView)
                
                if (btn_tag + 1) % 2 == 0 {
                    
                    
                    if SharedInfo.getInstance.currentDevice == "45" {
                        yy2 +=   175
                    }
                    else if SharedInfo.getInstance.currentDevice == "67"
                    {
                        yy2 +=  225
                    }
                        
                    else if SharedInfo.getInstance.currentDevice == "67+"
                    {
                        yy2 +=  240
                    }
                    
                }
                
                view_tag = view_tag + 1
                btn_tag = btn_tag + 1
                
                itemView.invalidateIntrinsicContentSize()
                
                
                
                iCheck += 1
                
            }// .Een for

        }catch {
            
        }
        
        scrMain.invalidateIntrinsicContentSize()
        updateScrollViewForHeight()
        
        
        self.refreshControl.endRefreshing()
        
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
        
        // create post request
        
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/GetCatalogForMember/catalog_for_member_mobile")!
        
        let jsonDict = [
            "mobile": SharedInfo.getInstance.mobile
            ,"formToken": SharedInfo.getInstance.formToken
            ,"cookieToken": SharedInfo.getInstance.cookieToken ]
        
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
                
                self.refreshControl.endRefreshing()
                
                guard let data = data else { return }
                guard let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: AnyObject] else { return }
                
                
                if json["msg"] as! String != "ลูกค้ายังมีคะแนนไม่สะสม" {
                    // assign result from
                    SharedInfo.getInstance.jsonGift = json;
                    
                    DispatchQueue.main.async(){
                        
                        self.lblGiftNotReady.isHidden = true;
                        self.lblDiscountNotReady.isHidden = true;
                        
                        var w:Int = 135
                        var h:Int = 165
                        
                        if SharedInfo.getInstance.currentDevice == "45" {
                            w = 105
                            h = 165
                        }
                            
                        else if SharedInfo.getInstance.currentDevice == "67"
                        {
                            w = 130
                            h = 200
                        }

                        else if SharedInfo.getInstance.currentDevice == "67+"
                        {
                            w = 160
                            h = 220
                        }

                        
                        
                        // call remove catgory item discount and gift first
                        // before show next item set
                        if self.a_itemView.count > 0 {
                            self.removeItemFromView()
                            
                        }
                            self.updateCatalogDiscount(boxWidth: w,boxHeight: h) // update discount block
                            self.updateCatalogGift(boxWidth: w,boxHeight: h) // update gift block
                        
                        
                        
                    }
                    
                }
                else
                {
                    // case not have any item
                    self.vweGIft.isHidden = false;
                    self.lblGiftNotReady.isHidden = false;
                    self.lblDiscountNotReady.isHidden = false;
                }
                
             
                
            } catch {
                print("error:", error)
            }
            
                    }
        
        task.resume()
        
    }// .End doLoadGif
    
    
    
    
    func ScoreForMember(){
       
        // create post request
        
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/RefreshPoint/Member")!
        
        let jsonDict = [
            "membercode": SharedInfo.getInstance.member_code
            ,"formToken": SharedInfo.getInstance.formToken
            ,"cookieToken": SharedInfo.getInstance.cookieToken ]
        
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
                        
                    }
                    
                    
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
        
    } // .End GetScore()

}
