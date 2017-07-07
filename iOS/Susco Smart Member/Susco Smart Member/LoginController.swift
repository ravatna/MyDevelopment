//
//  LoginController.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/21/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class LoginController:  UIViewController,UITextViewDelegate {
    
    @IBOutlet weak var swtLoginRegister: UISegmentedControl!
    
    
    // -- group login
    @IBOutlet weak var vweLogin: UIView!
    @IBOutlet weak var txtU: UITextField!
    @IBOutlet weak var txtP: UITextField!
    @IBOutlet weak var btnLogin: UIButton!
    
    
    
    // -- group register
    @IBOutlet weak var btnRegister: UIButton!
    @IBOutlet weak var txtPhone: UITextField!
    @IBOutlet weak var txtName: UITextField!
    @IBOutlet weak var txtSurname: UITextField!
    @IBOutlet weak var txtEmail: UITextField!
    @IBOutlet weak var txtPass: UITextField!
    @IBOutlet weak var txtRePass: UITextField!
    @IBOutlet weak var vweRegister: UIView!
    
    
    var loadingDialog:UIAlertController!
    
    
    
    func validRegisterField() -> Bool{
        var b:Bool = true
        if txtPhone.text == "" {
            let alert = BuildAlertDialog("แจ้งเตือน", "โปรดระบุหมายเลขโทรศัพท์", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: nil))
            
            b = false
            self.present(alert, animated:true, completion:nil)
            return b
        }
        
        if txtName.text == "" {
            let alert = BuildAlertDialog("แจ้งเตือน", "โปรดระบุชื่อ", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: nil))
            
            b = false
            self.present(alert, animated:true, completion:nil)
            return b
        }
        
        if txtSurname.text == "" {
            let alert = BuildAlertDialog("แจ้งเตือน", "โปรดระบุสกุล", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: nil))
            
            b = false
            self.present(alert, animated:true, completion:nil)
            return b
        }
        
        if txtEmail.text == "" {
            let alert = BuildAlertDialog("แจ้งเตือน", "โปรดระบุอีเมล์", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: nil))
            
            b = false
            self.present(alert, animated:true, completion:nil)
            return b
        }
        
        if isValidEmail(testStr: txtEmail.text!) != true {
            let alert = BuildAlertDialog("แจ้งเตือน", "โปรดระบุอีเมล์", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: nil))
            
            b = false
            self.present(alert, animated:true, completion:nil)
            return b
        }
        
        
        if txtPass.text == "" {
            let alert = BuildAlertDialog("แจ้งเตือน","โปรดระบุรหัสผ่าน", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: nil))
            
            b = false
            self.present(alert, animated:true, completion:nil)
            return b
        }
        
        if txtPass.text != txtRePass.text {
            let alert = BuildAlertDialog("แจ้งเตือน","ยืนยันรหัสผ่านไม่ถูกต้อง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: nil))
            
            b = false
            self.present(alert, animated:true, completion:nil)
            return b
        }
        
        
        
        return b
    }

    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        txtU.resignFirstResponder()
        txtP.resignFirstResponder()
        txtPhone.resignFirstResponder()
        txtName.resignFirstResponder()
        txtSurname.resignFirstResponder()
        txtPass.resignFirstResponder()
        txtRePass.resignFirstResponder()
        txtEmail.resignFirstResponder()
        
    }
    
    // -- on Press button Register
    @IBAction func doRegister(_ sender: Any) {
        // @todo: need to validate require field
        
       
        
        
        
        
        if validRegisterField() {
            
            present(self.loadingDialog, animated: true, completion: nil)

            
            doSubmitRegister()
        }

    }
    
    
    
    
    // -- on press button Login
    @IBAction func doLogin(_ sender: Any) {
        
       
        present(self.loadingDialog, animated: true, completion: nil)
        
        if validLoginField() {
            doSubmitLogin()
        }

    }
    
    // -- make connection to server
    func doSubmitLogin(){
    
    
        // create post request
        
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/Security/login_customer_susco")!
        let jsonDict = [ "mobile_customer": txtU.text as! String , "pass_customer": txtP.text as! String ]
        let jsonData = try! JSONSerialization.data(withJSONObject: jsonDict, options: [])
        
        var request = URLRequest(url: url)
        request.httpMethod = "post"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsonData
        
        let task = URLSession.shared.dataTask(with: request) {
            (data, response, error) in
            
            DispatchQueue.main.async(execute: {
                // dismiss alert view
                self.loadingDialog.dismiss(animated: true, completion: {
                    
                    if let error = error {
                        print("error:", error)
                        return
                    }
                    
                    do {
                        guard let data = data else { return }
                        guard let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: AnyObject] else { return }
                        
                        
                        if json["msg"] as! String == "Success" {
                            // assign result from
                            SharedInfo.getInstance.json = json;
                            
                            
                            let defaults = UserDefaults.standard
                            
                            defaults.set(json, forKey: "jsonLogin")
                            
                            // prepare to set home view controller
                            let viewController = self.storyboard?.instantiateViewController(withIdentifier: "home_tabview")
                            self.present(viewController!, animated:true,completion:nil);
                            //self.dismiss(animated: true, completion: nil)
                            
                        }else{
                            let alert = self.BuildAlertDialog("แจ้งเตือน", "ไม่พบผู้ใช้งานที่ระบุ", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: { Void in
                                self.dismiss(animated: true, completion: nil)
                            }))
                            
                            self.present(alert, animated:true, completion:nil)
                            
                        }
                        
                        
                        
                        
                        
                    } catch {
                        print("error:", error)
                    }
                } // end let task
                    
                )

            
            }) // end DispatchQueue
            
                    }
        
        
        task.resume()
    
    } // end func
    
    
    func isValidEmail(testStr:String) -> Bool {
        // print("validate calendar: \(testStr)")
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: testStr)
    }
    
    
    // -- make connection to server for register new member
    func doSubmitRegister(){
        
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/RegisterCustomerSusco/RegisterMember")!
        let jsonDict = [ "mobile": txtPhone.text
            , "fname": txtName.text
            , "lname": txtSurname.text
            , "password": txtPass.text
            , "email": txtEmail.text ]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: jsonDict, options: [])
        
        var request = URLRequest(url: url)
        request.httpMethod = "post"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsonData
        
        let task = URLSession.shared.dataTask(with: request) {
            (data, response, error) in
            
            // dismiss alert view
            self.loadingDialog.dismiss(animated: true, completion: { Void in
                
                if let error = error {
                    print("error:", error)
                    return
                }
                
                do {
                    guard let data = data else { return }
                    guard let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: AnyObject] else { return }
                    
                    if json["msg"] as! String == "success" {
                        let alert = self.BuildAlertDialog("ลงทะเบียนใหม่", "ลงทะเบียนเรียบร้อยแล้ว โปรดลงชื่อเข้าใช้งานเพื่อเข้าสู่ระบบ", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler:{ action in
                            
                            self.swtLoginRegister.selectedSegmentIndex = 0
                            self.updateSwitchView()
                            
                        }))
                        
                        self.present(alert, animated:true, completion:nil)
                    }else{
                        let alert = self.BuildAlertDialog("ลงทะเบียนใหม่", "ลงทะเบียนไม่สำเร็จ โปรดทำรายการใหม่ภายหลัง\nเนื่องจาก \(json["msg"] as! String)", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: nil))
                        
                        
                        self.present(alert, animated:true, completion:nil)
                    }
                    
                    
                } catch {
                    print("error:", error)
                }

                
                
            })
            
            
                    }
        
        task.resume()
        
    }// end func
    
    
    func validLoginField() -> Bool{
        var b:Bool = true
        if txtU.text == "" {
            let alert = BuildAlertDialog("แจ้งเตือน", "โปรดระบุชื่อผู้ใช้งาน", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: nil))
            
            b = false
            self.present(alert, animated:true, completion:nil)
            
        }
        
        
        if txtP.text == "" {
            let alert = BuildAlertDialog("แจ้งเตือน","โปรดระบุรหัสผ่าน", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: nil))
            
            b = false
            self.present(alert, animated:true, completion:nil)
            
        }
        
        return b
    }
    
    
        
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

    
    
    
    
    // -- on press switch bar and detect state to switch view login and regiter
    
    @IBAction func doSwitchView(_ sender: Any) {
        
        updateSwitchView()
        
        
    }
    
    func updateSwitchView(){
        if swtLoginRegister.selectedSegmentIndex == 0 {
            vweLogin.isHidden = false
            vweRegister.isHidden = true
        }else{
            vweLogin.isHidden = true
            vweRegister.isHidden = false
        }
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        txtU.text = "0831356653"
        txtP.text = "6653"
        
//        let defaults = UserDefaults.standard
//        do{
//            SharedInfo.getInstance.json = defaults.object(forKey: "jsonLogin") as! [String : AnyObject]
//            
//            if SharedInfo.getInstance.json != nil {
//                // prepare to set home view controller
//                let viewController = self.storyboard?.instantiateViewController(withIdentifier: "login_view")
//                self.present(viewController!, animated:true,completion:nil);
//                
//            }
//        }catch {
//            
//        }
        
        
        loadingDialog = UIAlertController(title: nil, message: "โปรดรอสักครู่...", preferredStyle: .alert)
        
        let loadingIndicator = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50))
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
        loadingIndicator.startAnimating();
        
        self.loadingDialog.view.addSubview(loadingIndicator)
        
    }
    
    
    
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}

