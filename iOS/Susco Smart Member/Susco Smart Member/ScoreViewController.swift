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
    
    var isFirst = true
    var yy:Int = 210
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
        
        updateScrollViewForHeight()
    }

    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        self.updateInfo() // update user data
        
        doLoadGift() // load content from servder
        
        
        
    }
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    
    func updateCatalogDiscount() {
        let catalogs:[AnyObject]
        do{

            catalogs = SharedInfo.getInstance.jsonGift!["catalog"] as! [AnyObject]
            var view_tag = 1000
            var btn_tag = 0
            
            
            for item in catalogs {
                
                // ให้สร้างรายการเฉพาะส่วนลด = 0
                if item["check"] as! String == "0" {
                    continue
                }
                
                print(item)
                var xx:Int = 10
                if (btn_tag + 1) % 2 == 0 {
                    xx = 165
                }
                
                // view group
                let itemView:UIView = UIView (frame: CGRect(x:xx,y:yy, width:145,height:170))
                itemView.tag = view_tag
                itemView.autoresizingMask = [.flexibleTopMargin]
                itemView.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 1.0)
                itemView.layer.shadowColor = UIColor.black.cgColor
                itemView.layer.shadowOpacity = 1
                itemView.layer.shadowOffset = CGSize.zero
                itemView.layer.shadowRadius = 1
                
                let lblTitle:UILabel = UILabel(frame:CGRect(x:8,y:115,width:140,height:28))
                
                lblTitle.text = item["redeem_item_desc"] as! String
                
                let buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:145, height:110))
                buttonConnect.tag = btn_tag
                
                let iconString:String = item["picture"] as! String
                
                let decodedData:Data = Data(base64Encoded: iconString)!
                buttonConnect.setImage(UIImage(data:decodedData), for: UIControlState.normal)

                itemView.addSubview(buttonConnect)
                itemView.addSubview(lblTitle)
                
                self.scrMain.addSubview(itemView)
                
                
                if (btn_tag + 1) % 2 == 0 {
                    yy = yy + 175
                    yy2 = yy
                }
                
                view_tag = view_tag + 1
                btn_tag = btn_tag + 1
                
                itemView.invalidateIntrinsicContentSize()
                
            }

        }catch {
            
        }
        
        scrMain.invalidateIntrinsicContentSize()
        updateScrollViewForHeight()
        
    }
    
    
    func updateCatalogGift() {
        let catalogs:[AnyObject]
        do{
            
            catalogs = SharedInfo.getInstance.jsonGift!["catalog"] as! [AnyObject]
            var view_tag = 2000
            var btn_tag = 0
            
            yy2 += 0
            
            vweGIft.isHidden = false
            
//            vweGIft.frame(CGRect(x:0,y:yy2, width:vweGIft.frame.size.width,height:vweGIft.frame.size.height))
            
            var f:CGRect = vweGIft.frame;
            f.origin.x = 0; // new x
            f.origin.y = CGFloat(yy2) // new y
            vweGIft.frame = f
           
            
            yy2 += 35
            
            for item in catalogs {
                
                // ให้สร้างรายการเฉพาะของแลก = 1
                if item["check"] as! String == "1" {
                    continue
                }
                
                print(item)
                var xx:Int = 10
                if (btn_tag + 1) % 2 == 0 {
                    xx = 165
                }
                
                // view group
                let itemView:UIView = UIView (frame: CGRect(x:xx,y:yy2, width:145,height:170))
                itemView.tag = view_tag
                itemView.autoresizingMask = [.flexibleTopMargin]
                itemView.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 1.0)
                itemView.layer.shadowColor = UIColor.black.cgColor
                itemView.layer.shadowOpacity = 1
                itemView.layer.shadowOffset = CGSize.zero
                itemView.layer.shadowRadius = 1
                
                
                let lblTitle:UILabel = UILabel(frame:CGRect(x:8,y:115,width:140,height:28))
                
                lblTitle.text = item["redeem_item_desc"] as! String
                
                let buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:145, height:110))
                buttonConnect.tag = btn_tag
                
                let iconString:String = item["picture"] as! String
                
                let decodedData:Data = Data(base64Encoded: iconString)!
                buttonConnect.setImage(UIImage(data:decodedData), for: UIControlState.normal)
                
                
                
                itemView.addSubview(buttonConnect)
                itemView.addSubview(lblTitle)
                
                self.scrMain.addSubview(itemView)
                
                
                if (btn_tag + 1) % 2 == 0 {
                    yy = yy + 175
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
    
    /**
 strJson = "{'mobile':'" + _mcode  + "','formToken':'" + m_formToken  + "','cookieToken':'" + m_cookieToken  + "'}";
     
 postUrl  = App.getInstance().m_server + "/GetCatalogForMember/catalog_for_member_mobile";
 */
    
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
                        self.updateCatalogDiscount() // update discount block
                        self.updateCatalogGift() // update gift block
                    }
                    
                    //print(json)
                }
                
             
                
            } catch {
                print("error:", error)
            }
            
                    }
        
        task.resume()
        
    }
}
