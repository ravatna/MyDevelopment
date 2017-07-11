//
//  FirstViewController.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/20/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class HomeViewController: UIViewController,UIScrollViewDelegate {
    @IBOutlet weak var scrMain: UIScrollView!
    @IBOutlet weak var lblPhone: UILabel!
    @IBOutlet weak var lblScore: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var pageControl: UIPageControl!
    @IBOutlet weak var scrImg: UIScrollView!
    
    var isFirst = true
    var yy:Int = 405
    
    var currentPage:Int = 0
    var loadingDialog:UIAlertController!
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        
        // validLoginState()
        
        if SharedInfo.getInstance.currentDevice == "45" {
            yy =   365
        }
        else if SharedInfo.getInstance.currentDevice == "67"
        {
            yy =  405
        }
            
        else if SharedInfo.getInstance.currentDevice == "67+"
        {
            yy =  405
        }

        
        
        initView()
       
        Timer.scheduledTimer(timeInterval: 5, target: self, selector: #selector(HomeViewController.moveToNextPageTwoSecond), userInfo: nil, repeats: true)
        
        
        
        
        
        
    }
    
    func moveToNextPageTwoSecond (){
        
        
        if currentPage >= (pageControl.numberOfPages - 1) {
            currentPage = 0
        }else{
        currentPage += 1
            }
        scrollToPage(currentPage)
    }
    
    func initView() {
        // กำหนดค่าการทำงานให้กับ pageControl
       
        loadingDialog = UIAlertController(title: nil, message: "โปรดรอสักครู่...", preferredStyle: .alert)
        
        let loadingIndicator = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50))
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
        loadingIndicator.startAnimating();
        
        self.loadingDialog.view.addSubview(loadingIndicator)
        
        
            self.doGetListBanner()
            self.doLoadTransaction()
        
        
    }
    
    
    //
    func updateNewsSection(boxWidth:Int,boxHeight:Int) {
        let customer:[AnyObject]
        let selectNews:[AnyObject]
        
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
           
            
            selectNews = SharedInfo.getInstance.json!["select_news"] as! [AnyObject]
            var view_tag = 1000
            var btn_tag = 0
            
            for item in selectNews {
                
                // view group
                let itemView:UIView = UIView (frame: CGRect(x:8,y:yy, width:boxWidth,height:boxHeight))
                itemView.tag = view_tag
                itemView.autoresizingMask = [.flexibleTopMargin]
                itemView.backgroundColor = UIColor(red: 1, green: 1, blue: 1, alpha: 1.0)
                itemView.layer.shadowColor = UIColor.black.cgColor
                itemView.layer.shadowOpacity = 1
                itemView.layer.shadowOffset = CGSize.zero
                itemView.layer.shadowRadius = 1
                
                
                let lblNewsHead:UILabel = UILabel(frame:CGRect(x:8,y:(boxHeight-26) ,width:(boxWidth-20) ,height:21))
                lblNewsHead.text = item["news_head"] as! String
                
                let buttonConnect = UIButton(frame: CGRect(x:0, y: 0, width:boxWidth, height: (boxHeight-30) ) )
                buttonConnect.tag = btn_tag
                
                let iconString:String = item["pic1_id"] as! String
                
                let decodedData:Data = Data(base64Encoded: iconString)!
                buttonConnect.setImage(UIImage(data:decodedData), for: UIControlState.normal)
                
                buttonConnect.addTarget(self, action: #selector(doOpenNewsDetail), for:.touchUpInside)
                
                
                itemView.addSubview(buttonConnect)
                itemView.addSubview(lblNewsHead)
                
                self.scrMain.addSubview(itemView)

                if SharedInfo.getInstance.currentDevice == "45" {
                    yy +=   255
                }
                else if SharedInfo.getInstance.currentDevice == "67"
                {
                    yy +=  265
                }
                    
                else if SharedInfo.getInstance.currentDevice == "67+"
                {
                    yy +=  290
                }

                view_tag = view_tag + 1
                btn_tag = btn_tag + 1
                
                
            }

        }catch {
            
        }

    
    }
    
 
    // when press any news will go to news detail.
    func doOpenNewsDetail(sender:UIButton!)
    {
        // prepare to set home view controller
        let viewController:NewsDetailViewController! = self.storyboard?.instantiateViewController(withIdentifier: "news_detail_view") as! NewsDetailViewController
        viewController.newsId = sender.tag
        self.present(viewController!, animated:true,completion:nil);
        

        
    }  // end func
    
    func doGotoURL (sender:UIButton!)
    {
        var banners = SharedInfo.getInstance.jsonBanner!["banner"] as! [AnyObject]
        var banner = banners[sender.tag]
        
        do{
            if let requestUrl = URL( string:banner["banner_url"] as! String) {
                UIApplication.shared.openURL(requestUrl)
                
            }
        } catch {
            // @todo: debug error
            print(error)
        }
    }
    
    
    
    override func viewDidAppear(_ animated: Bool) {
       
            // call update screen by first time only
            if isFirst == true{
                isFirst = false
                
                
                var w:Int = 304
                var h:Int = 235
                
                if SharedInfo.getInstance.currentDevice == "67"
                {
                    w = 360
                    h = 250
                }
                else if SharedInfo.getInstance.currentDevice == "67+"
                {
                    w = 398
                    h = 280
                    yy = 452
                }
                
                self.updateNewsSection(boxWidth:w,boxHeight:h)

                updateScrollViewForHeight()
                
            }

    }
    
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    // -- update scroll view about height
    func updateScrollViewForHeight(){
        let lastView : UIView! = scrMain.subviews.last
       // print(lastView.tag)
        let height:Float = Float(lastView.frame.size.height)

        let pos:Float = Float(lastView.frame.origin.y)
        let sizeOfContent = height + pos + 10
    
        
        scrMain.contentSize.height = CGFloat(sizeOfContent)
    }

    
    
    func scrollToPage(_ page: Int) {
        UIView.animate(withDuration: 0.3) {
            self.scrImg.contentOffset.x = self.scrImg.frame.width * CGFloat(page)
        }
    }
    
    /**
     * scrollview scroll page
     */
    func changePage(sender: AnyObject) -> () {
        let x = CGFloat(pageControl.currentPage) * scrImg.frame.size.width
        scrImg.setContentOffset(CGPoint(x:x, y:0), animated: true)
    }
    
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        
        let pageNumber = round(scrollView.contentOffset.x / scrollView.frame.size.width)
        pageControl.currentPage = Int(pageNumber)
    }


    // mark: -- make connection to server
    func doGetListBanner(){
        
       
        let customer:[AnyObject]
        
        customer = SharedInfo.getInstance.json!["customer_detail"] as! [AnyObject]
        
        let membercode = customer[0]["member_code"] as! String
        let formToken:String = SharedInfo.getInstance.json!["formToken"] as! String
        let cookieToken:String = SharedInfo.getInstance.json!["cookieToken"] as! String
      
     
        // create post request
        
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/Banner/GetBanner")!
        let jsonDict = [ "membercode": membercode
            ,"formToken": formToken
            ,"cookieToken": cookieToken ]
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
                
                
                // assign result from
                SharedInfo.getInstance.jsonBanner = json;
                
                var w:Int = 320
                var h:Int = 180
                
                if SharedInfo.getInstance.currentDevice == "67"
                
                {
                    w = 375
                    h = 190
                }
                
                else if SharedInfo.getInstance.currentDevice == "67+"
                
                {
                    w = 414
                    h = 215
                }
                
                DispatchQueue.main.async(execute: {
                self.updateBanner(boxWidth:w,boxHeight:h)
                })
                
            } catch {
                print("error:", error)
            }
        }
        
        task.resume()
        
    } // end func
    
    
    /////////////////////////////////////////////////////////////////////////////////
    
    
    // 320x180 = 45s, 67,399x190 = 67Plus
    func updateBanner(boxWidth:Int, boxHeight:Int) -> Void {
        
        // กำหนดค่าการทำงานให้กับ pageControl
        pageControl.addTarget(self, action: #selector(self.changePage(sender:)), for: UIControlEvents.valueChanged)
        
        let banners:[AnyObject]
        
        var iconString:String
        var decodedData:Data
        var itemCounter:Int = 0
        
        banners = SharedInfo.getInstance.jsonBanner!["banner"] as! [AnyObject]
        var btn_tag:Int = 0
        for banner in banners {
            
            // ตรวจสอบข้อมูลว่าเป็นค่าที่ว่างหรือไม่
            iconString = banner["image"] as! String
            if iconString != "" {
                
                decodedData = Data(base64Encoded: iconString)!
                // กำหนดความกว่างของ วัตถุ และตำแหน่งที่จะแสดง

                let buttonConnect = UIButton(frame: CGRect(x:boxWidth*itemCounter, y:0 ,width:boxWidth ,height:boxHeight))
                buttonConnect.tag = btn_tag

                
                
                buttonConnect.setImage(UIImage(data:decodedData), for: UIControlState.normal)
                buttonConnect.imageView?.contentMode = UIViewContentMode.scaleToFill
                
                
                buttonConnect.addTarget(self, action: #selector(doGotoURL), for:.touchUpInside)
                
              itemCounter += 1
                self.scrImg.addSubview(buttonConnect)
                btn_tag += 1
            } // end if

            // หลังจากที่ได้ข้อมูลมาแล้วว่ามีภาพที่ต้องแสดงกี่ภาพ ก็จะมาจัดการส่วนพื้นที่ให้แสดงภาพ ต่อว่าจะเปิดหรือปิด พื้นที่ส่วนแสดง
            
        }

        self.pageControl.numberOfPages = itemCounter
        self.scrImg.contentSize = CGSize(width:(self.scrImg.frame.width * CGFloat(itemCounter)), height:self.scrImg.frame.height)

    }
    
    
    ///////////////////////////////////////////////////////////////////////////////
    
    
    // mark: -- make connection to server
    func doLoadTransaction(){
        
        let customer:[AnyObject]
        
        customer = SharedInfo.getInstance.json!["customer_detail"] as! [AnyObject]
        
        let membercode = customer[0]["member_code"] as! String
        let formToken:String = SharedInfo.getInstance.json!["formToken"] as! String
        let cookieToken:String = SharedInfo.getInstance.json!["cookieToken"] as! String
        
        
        // create post request
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/ListTransactionCustomer/GetTransactionByMember")!
        let jsonDict = [ "membercode": membercode
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
                
                
                // assign result from
                SharedInfo.getInstance.jsonTransaction = json;
 
            } catch {
                print("error:", error)
            }
        }
        
        task.resume()
        
    } // end func

}

