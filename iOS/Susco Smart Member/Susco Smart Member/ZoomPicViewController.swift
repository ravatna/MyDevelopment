//
//  ZoomPicViewController.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/23/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class ZoomPicViewController: UIViewController,UIScrollViewDelegate {

    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var scrMain: UIScrollView!
    
    @IBOutlet weak var btnBack: UIButton!
    @IBAction func doBack(_ sender: Any) {
        self .dismiss(animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        let decodedData = Data(base64Encoded: SharedInfo.getInstance.imgBase64)!
        // กำหนดความกว่างของ วัตถุ และตำแหน่งที่จะแสดง
        
        
        
        img.image = UIImage(data:decodedData)
        
        
        scrMain.minimumZoomScale = 1.0
        scrMain.maximumZoomScale = 5.0
        
    }
    
    func viewForZooming(in scrollView: UIScrollView) -> UIView? {
        
        return self.img
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

}
