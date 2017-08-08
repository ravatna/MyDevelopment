//
//  OneTextViewController.swift
//  Susco Smart Member
//
//  Created by Tyche on 7/4/17.
//  Copyright © 2017 TYCHE. All rights reserved.
//

import UIKit

class OneTextViewController: UIViewController {

    @IBOutlet weak var btnCancel: UIButton!
    @IBOutlet weak var btnInput: UIButton!
    
    @IBOutlet weak var txtInput: UITextField!
    @IBOutlet weak var txtInput2: UITextField!
    @IBOutlet weak var txtInput3: UITextField!
    
    @IBOutlet weak var lblCaption: UILabel!
    
    @IBOutlet weak var vweContent: UIView!
    
    var loadingDialog:UIAlertController!

    
    var strPassword:String = ""
    var strEmail:String = ""
    var strCidCard:String = ""
    var strImgBase64:String = ""
    var strMobile:String = ""
    
    var caseProcess:Int!
    
    
    @IBAction func doCancel(_ sender: Any) {
        // self.view.removeFromSuperview()
        self.removeAnimate()
    }
    
    
    // clear all variable data
    func resetInputValue()
    {
        strEmail = ""
        strMobile = ""
        strCidCard = ""
        strPassword = ""
        strImgBase64 = ""
        
    }
    @IBAction func doSave(_ sender: Any) {
        
        // prepare data to variable
        saveProcess(self.caseProcess)
        
        // call submit data to server
        doSubmitInfo()
        
    }
    
    
    func isValidEmail(testStr:String) -> Bool {
        // print("validate calendar: \(testStr)")
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: testStr)
    }
    
    
    func saveProcess(_ process:Int) -> Bool
    {
        
        var b:Bool = false
        
        resetInputValue()
        
        switch process {
        case 1:
            if txtInput.text!.characters.count != 13 {
                
                let alert = self.BuildAlertDialog("แจ้งเตือน", "หมายเลขประจำตัวประชาชนไม่ถูกต้อง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler:nil))
                
                self.present(alert, animated:true, completion:nil)
                
                return b
            }
            
            strCidCard = txtInput.text!
            
            break
        case 2:
            
            
            // -- @todo: add check old password here.
            let defaults = UserDefaults.standard
            
            let oldPass = defaults.string(forKey: "pw")
            
//            if txtInput3.text! != oldPass {
//                let alert = self.BuildAlertDialog("แจ้งเตือน", "รหัสผ่านเดิมไม่ถูกต้อง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler:nil))
//                
//                self.present(alert, animated:true, completion:nil)
//                
//                return b;
//            }
            
            if txtInput.text! == "" || txtInput2.text! == "" {
                
                //@todo: alert message.
                let alert = self.BuildAlertDialog("แจ้งเตือน", "รหัสผ่านไหม่ไม่ถูกต้อง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler:nil))
                
                self.present(alert, animated:true, completion:nil)
                
                return b
            }
            
            if txtInput.text! != txtInput2.text!  {
                
                //@todo: alert message.
                let alert = self.BuildAlertDialog("แจ้งเตือน", "รหัสผ่านไหม่ไม่ถูกต้อง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler:nil))
                
                self.present(alert, animated:true, completion:nil)
                
                return b
            }
            
            strPassword = txtInput.text!
            break
            
        case 3:
            if isValidEmail(testStr: txtInput.text!) != true {
                
                let alert = self.BuildAlertDialog("แจ้งเตือน", "อีเมล์ไม่ถูกต้อง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler:nil))
                
                self.present(alert, animated:true, completion:nil)
                
                
                return b
            }
            strEmail = txtInput.text!
            break
            
        case 0:
            if txtInput.text! == "" {
                return b
            }
            strMobile = txtInput.text!
            break
            
        default:
            break
        }
        
        print(txtInput.text!)
    
        return true
    }
    
    
    func updateLabel(_ process:Int) -> Void
    {
        
        txtInput2.isHidden = true
        txtInput3.isHidden = true
        
        txtInput.isSecureTextEntry = false
        txtInput2.isSecureTextEntry = false
        
        var f:CGRect = vweContent.frame
        
        f.size.width = vweContent.frame.size.width  // new width
        f.size.height = vweContent.frame.size.height - 40 // new height
        
        vweContent.frame = f
        
        switch process {
        case 1:
           lblCaption.text = "แก้ไขเลขประจำตัวประชาชน"
            
            break
        case 2:
            lblCaption.text = "แก้ไขรหัสผ่าน"
            txtInput2.isHidden = false
            txtInput3.isHidden = false
            txtInput.isSecureTextEntry = true
            txtInput2.isSecureTextEntry = true
            txtInput3.isSecureTextEntry = true
            
            
            
            break
        case 3:
            lblCaption.text = "แก้ไขอีเมล์"
            break
        case 0:
            lblCaption.text = "แก้ไขเบอร์ติดต่อ"
            break
        default:
            break
        }
        
        
    }
    
    
    func showAnimate()
    {
        self.view.transform = CGAffineTransform(scaleX: 1.3, y: 1.3)
        self.view.alpha = 0.0;
        
        UIView.animate(withDuration: 0.25, animations: {
            self.view.alpha = 1.0
            self.view.transform = CGAffineTransform(scaleX: 1.0, y: 1.0)
        });
        
    }
    
    func removeAnimate()
    {
        UIView.animate(withDuration: 0.25, animations: {
            self.view.transform = CGAffineTransform(scaleX: 1.3, y: 1.3)
            self.view.alpha = 0.0;
        }, completion:{(finished : Bool)  in
            if (finished)
            {
                self.view.removeFromSuperview()
            }
        });
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        
        self.updateLabel(self.caseProcess)
        
        self.view.backgroundColor = UIColor.black.withAlphaComponent(0.8)
        
        self.showAnimate()
        
        self.vweContent.layer.cornerRadius = 5
        self.vweContent.layer.masksToBounds = true
        
        loadingDialog = UIAlertController(title: nil, message: "โปรดรอสักครู่...", preferredStyle: .alert)
        
        let loadingIndicator = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50))
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
        loadingIndicator.startAnimating();
        
        self.loadingDialog.view.addSubview(loadingIndicator)
        
        // Do any additional setup after loading the view.
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

    
    func doSubmitInfo(){

        present(self.loadingDialog, animated: true, completion: nil)
        
        let customer:[AnyObject]
        
        customer = SharedInfo.getInstance.json!["customer_detail"] as! [AnyObject]
        let code = customer[0]["member_code"] as! String
        let formToken:String = SharedInfo.getInstance.json!["formToken"] as! String
        let cookieToken:String = SharedInfo.getInstance.json!["cookieToken"] as! String
        
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/UpdateDetailCustomer/UpdateDetail")!
        let jsonDict = [ "member_code": code
            , "password": strPassword
            , "email": strEmail
            , "cid_card": strCidCard
            , "imagebase64": strImgBase64
        , "formToken": formToken
        , "cookieToken": cookieToken ]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: jsonDict, options: [])
        
        var request = URLRequest(url: url)
        request.httpMethod = "post"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsonData
        
        let task = URLSession.shared.dataTask(with: request) {
            (data, response, error) in
            
            
            DispatchQueue.main.async(execute: {
            // dismiss alert view
            self.loadingDialog.dismiss(animated: true, completion: { Void in
                
                if let error = error {
                    print("error:", error)
                    return
                }
                
                do {
                    guard let data = data else { return }
                    guard let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: AnyObject] else { return }
                    
                    if json["success"] as! Bool == true {
                        let alert = self.BuildAlertDialog("ปรับปรุงข้อมูล", "ปรับปรุงข้อมูลเรียบร้อยแล้ว\n กรุณาออกจากระบบและเข้าสู่ระบบใหม่อีกครั้ง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler:{ action in
                        self.removeAnimate()
                        }))
                        
                        self.present(alert, animated:true, completion:nil)
                    }else{
                        let alert = self.BuildAlertDialog("ปรับปรุงข้อมูล", "ไม่สามารถแก้ไขข้อมูลขณะนี้ โปรดทำรายการใหม่ภายหลัง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: {action in
                        self.removeAnimate()
                        }))
                        
                        
                        self.present(alert, animated:true, completion:nil)
                    }
                    
                    
                } catch {
                    print("error:", error)
                }
                
                
                
                
            })
            
            }) // end DispatchQueue

            
        }
        
        task.resume()
        
    }// end func

    
    func BuildAlertDialog(_ t:String, _ m:String, btnAction:UIAlertAction) -> UIAlertController {
        // create the alert
        let alert = UIAlertController(title: t, message: m, preferredStyle: UIAlertControllerStyle.alert)
        
        // add an action (button)
        alert.addAction(btnAction)
        
        
        
        return alert
        
    }
    
    func BuildAlertDialog(_ t:String, m:String,  btnAction:UIAlertAction,btnAction2:UIAlertAction) -> UIAlertController {
        
        // create the alert
        let alert = UIAlertController(title: t, message: m, preferredStyle: UIAlertControllerStyle.alert)
        
        // add an action (button)
        alert.addAction(btnAction)
        
        // add an action (button)
        alert.addAction(btnAction2)
        
        return alert
    }

}
