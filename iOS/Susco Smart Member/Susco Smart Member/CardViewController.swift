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
    @IBOutlet weak var vweTitleCard: UIView!
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

        lblCode.text = SharedInfo.getInstance.member_code
        lblName.text = SharedInfo.getInstance.fname + " " + SharedInfo.getInstance.lname
        
        let index = SharedInfo.getInstance.createdate.index(SharedInfo.getInstance.createdate.endIndex, offsetBy: -7)
        lblDate.text = SharedInfo.getInstance.createdate.substring(from: index)  // Hello
        
        
        // generate QR Code by Member Code
        if let img = createQRFromString(SharedInfo.getInstance.member_code) {
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
