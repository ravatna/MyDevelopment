//
//  NewsDetailViewController.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/23/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit



class NewsDetailViewController: UIViewController,UIScrollViewDelegate,UIWebViewDelegate {

    
    @IBOutlet weak var wbvDetail: UIWebView!
    
    @IBOutlet weak var btnBack: UIButton!
    
    @IBOutlet weak var lblTitle: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var btnLeft: UIButton!
    
    @IBOutlet weak var btnRight: UIButton!
    
    
    @IBOutlet weak var pageControl: UIPageControl!
    
    @IBOutlet weak var scrImg: UIScrollView!
    @IBOutlet weak var scrMain: UIScrollView!
    @IBOutlet weak var slideView: UIView!
    var newsId:Int!
    var currentPage:Int = 0
    
    let screenHeight = UIScreen.main.bounds.height
    let screenWidth = UIScreen.main.bounds.width
    
    @IBAction func doBack(_ sender: Any) {
        
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func doMoveRight(_ sender: Any) {
        moveToNextPageTwoSecond()
    }
    
    @IBAction func doMoveLeft(_ sender: Any) {
        moveToBackPage()
    }
    
   
    
    func moveToNextPageTwoSecond (){
        if currentPage >= (pageControl.numberOfPages - 1) {
            currentPage = 0
        }else{
            currentPage += 1
        }
        scrollToPage(currentPage)
        pageControl.currentPage = currentPage
        
    }// .End moveToNextPageTwoSecond
    
    
    func moveToBackPage (){
        if currentPage <= 0  {
            currentPage = (pageControl.numberOfPages - 1)
        }else{
            currentPage -= 1
        }
        scrollToPage(currentPage)
        pageControl.currentPage = currentPage
    }// .End moveToBackPage
    
    
    ////////////////////////////////////////////////
    func GetImageBase64_News(_ button:UIButton,_ imageCode:String, _ i:Int){
        
        
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
                        
                        
                        
                        
                        if(i == 1){
                            SharedInfo.getInstance.imgBase64_1 = iconString
                        }else if(i == 2){
                            SharedInfo.getInstance.imgBase64_2 = iconString
                        }else if(i == 3){
                            SharedInfo.getInstance.imgBase64_3 = iconString
                        }
                        
                        
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
    
    
    ////////////////////////////////////////////////
    func GetImageBase64_View(_ imageCode:String){
        
        
        
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
                        
                        
                        
                        // ตรวจสอบข้อมูลว่าเป็นค่าที่ว่างหรือไม่
                        SharedInfo.getInstance.imgBase64 = json["imagebase64"] as! String
                        
                        
                    }
                })
            } catch {
                print("error:", error)
            }
        }
        
        task.resume()
        
    } // end func

    
    override func viewDidLoad() {
        wbvDetail.delegate = self
        super.viewDidLoad()
        //[wbvDetail setFont(UIFont fontWithName:@"Kanit-Regular" size:16)
//        wbvDetail.setValue(<#T##value: Any?##Any?#>, forKey: <#T##String#>)
        // Do any additional setup after loading the view.
        
        var w:Int = 320
        var h:Int = 180
        
         if SharedInfo.getInstance.currentDevice == "67"
        {
           w = 375
            h = 210
        }
            
        else if SharedInfo.getInstance.currentDevice == "67+"
        {
            w = 414
            h = 250
        }

        
        
        updateInfo(boxWidth: w,boxHeight: h)
        Timer.scheduledTimer(timeInterval: 5, target: self, selector: #selector(NewsDetailViewController.moveToNextPageTwoSecond), userInfo: nil, repeats: true)
    }
    
    
    // -- update scroll
    func scrollToPage(_ page: Int) {
        // set time to play anime
        UIView.animate(withDuration: 0.3) {
            self.scrImg.contentOffset.x = self.scrImg.frame.width * CGFloat(page)
        }
    }
    
    func webView(_ webView: UIWebView, shouldStartLoadWith request: URLRequest, navigationType: UIWebViewNavigationType) -> Bool {

        if navigationType == UIWebViewNavigationType.linkClicked {
            UIApplication.shared.openURL(request.url!)
            return false
        }
        
        return true
        
    }
    
    func webViewDidFinishLoad(_ webView: UIWebView) {
        webView.stringByEvaluatingJavaScript(from: "document.getElementsByTagName('body')[0].style.fontFamily =\"Kanit-Regular\"")
    }
    
    /**
     * ปรับปรุงข้อมูล ให้แสดงผลบนหน้าจอ
     */
    func updateInfo(boxWidth:Int,boxHeight:Int) {
        
        let selectNews:[AnyObject]
        
        var itemCounter:Int = 0
        var iconString:String
        var decodedData:Data
        var button_tag:Int = 0
        
        selectNews = SharedInfo.getInstance.selectNews!["select_news"] as! [AnyObject]
        let item:AnyObject = selectNews[self.newsId]
        
        // กำหนดค่าการทำงานให้กับ pageControl
        pageControl.addTarget(self, action: #selector(self.changePage(sender:)), for: UIControlEvents.valueChanged)

        // ตรวจสอบข้อมูลว่าเป็นค่าที่ว่างหรือไม่
        iconString = item["pic1_id"] as! String
        if iconString != "" {
            
            decodedData = Data(base64Encoded: iconString)!
            // กำหนดความกว่างของ วัตถุ และตำแหน่งที่จะแสดง
            
       
            var img:UIImage! = UIImage(data:decodedData)
            
            var pic:UIButton = UIButton(frame: CGRect(x:boxWidth*itemCounter, y:0 ,width:boxWidth ,height:boxHeight))
            pic.setBackgroundImage(img, for: UIControlState.normal)
            pic.imageView?.contentMode = UIViewContentMode.scaleToFill
            itemCounter += 1
            pic.tag = 1
            pic.addTarget(self, action: #selector(self.doOpenZoom(sender:)), for: UIControlEvents.touchUpInside)
            self.scrImg.addSubview(pic)
            
            GetImageBase64_News(pic,item["code_image1"] as! String,1)
        } // end if
        
        iconString = item["pic2_id"] as! String
        if iconString != "" {
            
            decodedData = Data(base64Encoded: iconString)!
            // กำหนดความกว่างของ วัตถุ และตำแหน่งที่จะแสดง
            
            
            var img:UIImage! = UIImage(data:decodedData)
            
            var pic:UIButton = UIButton(frame: CGRect(x:boxWidth*itemCounter, y:0 ,width:boxWidth ,height:boxHeight))
            pic.setBackgroundImage(img, for: UIControlState.normal)
            pic.imageView?.contentMode = UIViewContentMode.scaleToFill
            pic.addTarget(self, action: #selector(self.doOpenZoom(sender:)), for: UIControlEvents.touchUpInside)
            itemCounter +=   1
            pic.tag = 2
            self.scrImg.addSubview(pic)
            GetImageBase64_News(pic,item["code_image2"] as! String,2)
        } // end if
        
        iconString = item["pic3_id"] as! String
        if iconString != "" {
            
            decodedData = Data(base64Encoded: iconString)!
            // กำหนดความกว่างของ วัตถุ และตำแหน่งที่จะแสดง
            
            
            var img:UIImage! = UIImage(data:decodedData)
            
            var pic:UIButton = UIButton(frame: CGRect(x:boxWidth*itemCounter, y:0 ,width:boxWidth ,height:boxHeight))
           pic.tag = 3
            pic.imageView?.contentMode = UIViewContentMode.scaleToFill
            pic.setBackgroundImage(img, for: UIControlState.normal)
            pic.addTarget(self, action: #selector(self.doOpenZoom(sender:)), for: UIControlEvents.touchUpInside)
            
            itemCounter +=  1
            self.scrImg.addSubview(pic)
            GetImageBase64_News(pic,item["code_image3"] as! String,3)
        } // end if
        
        
        
        // หลังจากที่ได้ข้อมูลมาแล้วว่ามีภาพที่ต้องแสดงกี่ภาพ ก็จะมาจัดการส่วนพื้นที่ให้แสดงภาพ ต่อว่าจะเปิดหรือปิด พื้นที่ส่วนแสดง
        
        
        // check item picture if 0 hide scroll view
        if itemCounter == 0 {
            scrImg.isHidden = true
         
            pageControl.numberOfPages = itemCounter
        }
            
            // if not equals 0 then show scroll view and show exactly item on scroll view
        else{
            scrImg.isHidden = false
            pageControl.numberOfPages = itemCounter
        } // end if
        
        
        lblTitle.text = item["news_head"] as! String
        lblDate.text = item["news_date"] as! String
        wbvDetail.loadHTMLString(item["news_text"] as! String, baseURL: nil);
        
        self.scrImg.contentSize = CGSize(width:(self.scrImg.frame.width * CGFloat(itemCounter)), height:self.scrImg.frame.height)
      
        updateScrollMainHeight()
    }
    
    func  updateScrollMainHeight(){
        var hhhh : CGFloat = 300
        
        if SharedInfo.getInstance.currentDevice == "45" {
            hhhh =   400
        }
        else if SharedInfo.getInstance.currentDevice == "67"
        {
            hhhh =  300
        }
            
        else if SharedInfo.getInstance.currentDevice == "67+"
        {
            hhhh =  150
        }
        
        
        
        wbvDetail.scrollView.isScrollEnabled = false
        
        var yHeight :CGRect = wbvDetail.frame
        yHeight.size.height = ((wbvDetail.scrollView.contentSize.height + hhhh) + wbvDetail.frame.origin.y)
        wbvDetail.frame = yHeight
        scrMain.contentSize.height = wbvDetail.scrollView.contentSize.height+wbvDetail.frame.origin.y
        
        
    }


    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
    func doOpenZoom(sender: AnyObject) -> () {
        
        let selectNews:[AnyObject]
        
        
        
        selectNews = SharedInfo.getInstance.selectNews!["select_news"] as! [AnyObject]
        let item:AnyObject = selectNews[self.newsId]
        
        if sender.tag == 1 {
        
            SharedInfo.getInstance.imgBase64 =  SharedInfo.getInstance.imgBase64_1
        }else if sender.tag == 2 {
            SharedInfo.getInstance.imgBase64 =  SharedInfo.getInstance.imgBase64_2
        }else if sender.tag == 3 {
            
            SharedInfo.getInstance.imgBase64 =  SharedInfo.getInstance.imgBase64_3
        }

        
        let viewController:ZoomPicViewController! = self.storyboard?.instantiateViewController(withIdentifier: "zoom_view") as! ZoomPicViewController
        
        self.present(viewController!, animated:true,completion:nil);
    }
    
    
    
    func changePage(sender: AnyObject) -> () {
        let x = CGFloat(pageControl.currentPage) * scrImg.frame.size.width
        scrImg.setContentOffset(CGPoint(x:x, y:0), animated: true)
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        
        let pageNumber = round(scrollView.contentOffset.x / scrollView.frame.size.width)
        pageControl.currentPage = Int(pageNumber)
    }
    
    
    
    

}

