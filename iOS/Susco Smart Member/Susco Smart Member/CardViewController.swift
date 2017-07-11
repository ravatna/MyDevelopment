//
//  CardViewController.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/26/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class CardViewController: UIViewController {

    @IBOutlet weak var btnBack: UIButton!
    @IBOutlet weak var lblCode: UILabel!
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblName: UILabel!
    @IBOutlet weak var imgQR: UIImageView!
    
    @IBOutlet weak var vweCard: UIView!
    var qrcodeImage: CIImage!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        let angle:CGFloat = (270.0 * 3.14/180.0) as CGFloat
        let rotation = CGAffineTransform(rotationAngle: angle)
        vweCard.transform = rotation
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func doBack(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        let customer:[AnyObject]
        
        customer = SharedInfo.getInstance.json!["customer_detail"] as! [AnyObject]
        
        //print(customer[0])
        var fname = customer[0]["fname"] as! String
        var lname = customer[0]["lname"] as! String
        var code = customer[0]["member_code"] as! String
        var score = customer[0]["point_summary"] as! String
        var phone = customer[0]["mobile"] as! String
        var email = customer[0]["email"] as! String
        var cid_card = customer[0]["cid_card"] as! String
        var createdate = customer[0]["createdate"] as! String
        
        lblCode.text = code
        lblName.text = fname + " " + lname
        lblDate.text = createdate
    
        
        let data = customer[0]["member_code"] as! String
        
        if let img = createQRFromString(data) {
            //let somImage = UIImage(CIImage: img, scale: 1.0, orientation: UIImageOrientation.Down)
            imgQR.image = img
        }
        
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

    // MARK: Custom method implementation
    
    
    func createQRFromString(_ str: String) -> UIImage?
    {
        let data = str.data(using: String.Encoding.ascii)
        
        if let filter = CIFilter(name: "CIQRCodeGenerator") {
            filter.setValue(data, forKey: "inputMessage")
            let transform = CGAffineTransform(scaleX: 5, y: 5)
            
            if let output = filter.outputImage?.applying(transform) {
                return UIImage(ciImage: output)
            }
        }
        
        return nil
        
        
    }
    
    func convert(cmage:CIImage) -> UIImage
    {
        let context:CIContext = CIContext.init(options: nil)
        let cgImage:CGImage = context.createCGImage(cmage, from: cmage.extent)!
        let image:UIImage = UIImage.init(cgImage: cgImage)
        return image
    }
}
