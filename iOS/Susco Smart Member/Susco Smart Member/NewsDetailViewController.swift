//
//  NewsDetailViewController.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/23/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class NewsDetailViewController: UIViewController,UIScrollViewDelegate {

    
    @IBOutlet weak var wbvDetail: UIWebView!
    
    @IBOutlet weak var btnBack: UIButton!
    
    @IBOutlet weak var lblTitle: UILabel!
    
    
    @IBOutlet weak var pageControl: UIPageControl!
    
    @IBOutlet weak var scrImg: UIScrollView!
    
    @IBOutlet weak var slideView: UIView!
    var newsId:Int!
    
    
    
    @IBAction func doBack(_ sender: Any) {
        
        self.dismiss(animated: true, completion: nil)
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        updateInfo()
    }
    
    
    
    /**
     * ปรับปรุงข้อมูล ให้แสดงผลบนหน้าจอ
     */
    func updateInfo() {
        
        let selectNews:[AnyObject]
        
        var itemCounter:Int = 0
        var iconString:String
        var decodedData:Data
        
        let boxHeight : Int = 180
        let boxWidth : Int = 320
        
        selectNews = SharedInfo.getInstance.json!["select_news"] as! [AnyObject]
        let item:AnyObject = selectNews[self.newsId]
        
        // กำหนดค่าการทำงานให้กับ pageControl
        pageControl.addTarget(self, action: #selector(self.changePage(sender:)), for: UIControlEvents.valueChanged)

        
        
        // ตรวจสอบข้อมูลว่าเป็นค่าที่ว่างหรือไม่
        iconString = item["pic1_id"] as! String
        if iconString != "" {
            
            decodedData = Data(base64Encoded: iconString)!
            // กำหนดความกว่างของ วัตถุ และตำแหน่งที่จะแสดง
            
       
            var img:UIImage! = UIImage(data:decodedData)
            
            var pic:UIButton = UIButton(frame: CGRect(x:320*itemCounter, y:0 ,width:320 ,height:180))
            pic.setBackgroundImage(img, for: UIControlState.normal)
            
            itemCounter = itemCounter + 1
            self.scrImg.addSubview(pic)
        } // end if
        
        iconString = item["pic2_id"] as! String
        if iconString != "" {
            
            decodedData = Data(base64Encoded: iconString)!
            // กำหนดความกว่างของ วัตถุ และตำแหน่งที่จะแสดง
            
            
            var img:UIImage! = UIImage(data:decodedData)
            
            var pic:UIButton = UIButton(frame: CGRect(x:320*itemCounter, y:0 ,width:320 ,height:180))
            pic.setBackgroundImage(img, for: UIControlState.normal)
            
            itemCounter = itemCounter + 1
            self.scrImg.addSubview(pic)
        } // end if
        
        iconString = item["pic3_id"] as! String
        if iconString != "" {
            
            decodedData = Data(base64Encoded: iconString)!
            // กำหนดความกว่างของ วัตถุ และตำแหน่งที่จะแสดง
            
            
            var img:UIImage! = UIImage(data:decodedData)
            
            var pic:UIButton = UIButton(frame: CGRect(x:320*itemCounter, y:0 ,width:320 ,height:180))
            pic.setBackgroundImage(img, for: UIControlState.normal)
            
            itemCounter = itemCounter + 1
            self.scrImg.addSubview(pic)
        } // end if
        
        // หลังจากที่ได้ข้อมูลมาแล้วว่ามีภาพที่ต้องแสดงกี่ภาพ ก็จะมาจัดการส่วนพื้นที่ให้แสดงภาพ ต่อว่าจะเปิดหรือปิด พื้นที่ส่วนแสดง
        
        
        // check item picture if 0 hide scroll view
        if itemCounter == 0 {
            scrImg.isHidden = true
        }
            
            // if not equals 0 then show scroll view and show exactly item on scroll view
        else{
            scrImg.isHidden = false
        } // end if
        
        
        lblTitle.text = item["news_head"] as! String
        wbvDetail.loadHTMLString(item["news_text"] as! String, baseURL: nil);
        
        self.scrImg.contentSize = CGSize(width:(self.scrImg.frame.width * CGFloat(1+itemCounter)), height:self.scrImg.frame.height)
        
        //scrImg.delegate = self
        //self.pageControl.currentPage = 0
        

        
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
    
    func changePage(sender: AnyObject) -> () {
        let x = CGFloat(pageControl.currentPage) * scrImg.frame.size.width
        scrImg.setContentOffset(CGPoint(x:x, y:0), animated: true)
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        
        let pageNumber = round(scrollView.contentOffset.x / scrollView.frame.size.width)
        pageControl.currentPage = Int(pageNumber)
    }

}
