//
//  LoginController.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/21/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

extension NSRange {
    func range(for str: String) -> Range<String.Index>? {
        guard location != NSNotFound else { return nil }
        
        guard let fromUTFIndex = str.utf16.index(str.utf16.startIndex, offsetBy: location, limitedBy: str.utf16.endIndex) else { return nil }
        guard let toUTFIndex = str.utf16.index(fromUTFIndex, offsetBy: length, limitedBy: str.utf16.endIndex) else { return nil }
        guard let fromIndex = String.Index(fromUTFIndex, within: str) else { return nil }
        guard let toIndex = String.Index(toUTFIndex, within: str) else { return nil }
        
        return fromIndex ..< toIndex
    }
}

class LoginController:  UIViewController,UITextViewDelegate,UITextFieldDelegate {
    
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
    
    // -- test value is number only for 10 digits
    func phoneNumberOnly(value: String) -> Bool {
        let PHONE_REGEX = "^08\\d{8}"
        let phoneTest = NSPredicate(format: "SELF MATCHES %@", PHONE_REGEX)
        let result =  phoneTest.evaluate(with: value)
        return result
    }
    
    func writeJSON_2_File(_ text_json:String) -> Bool {
    
        let file = "login.json" //this is the file. we will write to and read from it
        
        
        
        if let dir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
            
            let path = dir.appendingPathComponent(file)
            
            //writing
            do {
                try text_json.write(to: path, atomically: false, encoding: String.Encoding.utf8)
                return true
            }
            catch {/* error handling here */
                return false
            }
           
        }
    
        return false
    }// .End write JSON to file
    
    func readJSON_2_File() -> String {
        
        let file = "login.json" //this is the file. we will write to and read from it
        
        if let dir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
            
            let path = dir.appendingPathComponent(file)

             //reading
             do {
                let text2 = try String(contentsOf: path, encoding: String.Encoding.utf8)
                return text2
             }
             catch {
                /* error handling here */
                return "{}"
             }
 
        }
        
        return "{}"
        
    } // .End read JSON from file
    
    
    
    
    
    
    
 
    // -- validRegisterField()
    
    func validRegisterField() -> Bool{
        var b:Bool = true
        if phoneNumberOnly(value: txtPhone.text!) != true {
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
            // present loading
            
            present(self.loadingDialog, animated: true, completion: nil)
            
            // submit content to server
            doSubmitRegister()
        }
    }// .End doRegister()

    
    // -- on press button Login
    @IBAction func doLogin(_ sender: Any) {
        
        
        if validLoginField() == true {
            
            doSubmitLogin()
        }

    }// .End doLogin()
    
    
    // -- set
    
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        
        // detect for mobile number need 10 lenght only
        if(textField.tag==1000){
            let currentText = textField.text ?? ""
            guard let stringRange = range.range(for: currentText) else { return false }
            let updatedText = currentText.replacingCharacters(in: stringRange, with: string)
            return updatedText.characters.count <= 10
        }
        
        return true
        
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {   //delegate method
        
        
        
        if textField.tag == 1000 {
            txtName.becomeFirstResponder()
        }else if textField.tag == 1001 {
            txtSurname.becomeFirstResponder()
        }else if textField.tag == 1002 {
            txtEmail.becomeFirstResponder()
        }else if textField.tag == 1003 {
            txtPass.becomeFirstResponder()
        }else if textField.tag == 1004 {
            txtRePass.becomeFirstResponder()
        }
        textField.resignFirstResponder()
        
        return true
    }
    
    
    
    
    // -- make connection to server
    func doSubmitLogin(){
    
        //// clear state textField keyboard
        txtU.resignFirstResponder()
        txtP.resignFirstResponder()
        txtPhone.resignFirstResponder()
        txtName.resignFirstResponder()
        txtSurname.resignFirstResponder()
        txtPass.resignFirstResponder()
        txtRePass.resignFirstResponder()
        txtEmail.resignFirstResponder()
        ///////////////////////////////////
        
        present(self.loadingDialog, animated: true, completion: nil)

        
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
                            
                            let customer = SharedInfo.getInstance.json!["customer_detail"] as! [AnyObject]
                            let formToken:String = SharedInfo.getInstance.json!["formToken"] as! String
                            let cookieToken:String = SharedInfo.getInstance.json!["cookieToken"] as! String
                            
                            SharedInfo.getInstance.jsonCustomer = customer
                            
                            
                            if let s =  customer[0]["fname"] as? String {
                                SharedInfo.getInstance.fname = s
                            } else {
                                SharedInfo.getInstance.fname = ""
                            }
                            
                            if let s =  customer[0]["lname"] as? String {
                                SharedInfo.getInstance.lname = s
                            } else {
                                SharedInfo.getInstance.lname = ""
                            }
                            
                            
                            SharedInfo.getInstance.member_code = customer[0]["member_code"] as! String
                            SharedInfo.getInstance.mobile = customer[0]["mobile"] as! String
                            
                            
                            if let s =  customer[0]["code_image"] as? String {
                                SharedInfo.getInstance.code_image = s
                            } else {
                                SharedInfo.getInstance.code_image = ""
                            }
                            
                            if let s =  customer[0]["email"] as? String {
                                SharedInfo.getInstance.email = s
                            } else {
                                SharedInfo.getInstance.email = ""
                            }
                            
                            
                            SharedInfo.getInstance.createdate = customer[0]["createdate"] as! String
                            
                            if let s =  customer[0]["cid_card"] as? String {
                                SharedInfo.getInstance.cid_card = s
                            } else {
                                SharedInfo.getInstance.cid_card = ""
                            }
                            
                            SharedInfo.getInstance.point_summary = customer[0]["point_summary"] as! String
                            
                            if customer[0]["cid_card_pic"] as! String != "" {
                                SharedInfo.getInstance.had_pic_profile = true
                            }else{
                                SharedInfo.getInstance.had_pic_profile = false
                            }
                            
                            
                            SharedInfo.getInstance.formToken = formToken
                            SharedInfo.getInstance.cookieToken = cookieToken
                            
                            let defaults = UserDefaults.standard

                            //@todo: change from save default
                            // remember user to default section //
                            defaults.setValue(SharedInfo.getInstance.fname, forKey: "fname")
                            defaults.setValue(SharedInfo.getInstance.lname, forKey: "lname")
                            defaults.setValue(SharedInfo.getInstance.member_code, forKey: "member_code")
                            defaults.setValue(SharedInfo.getInstance.mobile, forKey: "mobile")
                            defaults.setValue(SharedInfo.getInstance.code_image, forKey: "code_image")
                            defaults.setValue(SharedInfo.getInstance.email, forKey: "email")
                            defaults.setValue(SharedInfo.getInstance.cid_card, forKey: "cid_card")
                            defaults.setValue(SharedInfo.getInstance.createdate, forKey: "createdate")
                            defaults.setValue(SharedInfo.getInstance.point_summary, forKey: "point_summary")
                            defaults.setValue(SharedInfo.getInstance.had_pic_profile, forKey: "had_pic_profile")
                            
                            defaults.setValue(SharedInfo.getInstance.formToken, forKey: "formToken")
                            defaults.setValue(SharedInfo.getInstance.cookieToken, forKey: "cookieToken")
                            defaults.setValue(self.txtP.text, forKey: "pw")
 
                            
                            // next display main view
                            
                            // prepare to set home view controller
                            let viewController = self.storyboard?.instantiateViewController(withIdentifier: "home_tabview")
                            self.present(viewController!, animated:true,completion:nil);
                            
                            
                        }else{
                            
                            let alert = self.BuildAlertDialog("ลงชื่อเข้าใช้งาน", "การลงชื่อเข้าใช้งานไม่สำเร็จ กรุณาตรวจสอบ ชื่อผู้ใช้งาน และรหัสผ่านอีกครั้ง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: { Void in
                                self.dismiss(animated: true, completion: nil)
                                
                               
                                    self.txtU.becomeFirstResponder()
                                    
                                
                            }))
                            
                            self.present(alert, animated:true, completion:nil)
                            
                        }
                        
                        self.txtU.text = ""
                        self.txtP.text = ""
                        self.txtName.text = ""
                        self.txtPass.text = ""
                        self.txtRePass.text = ""
                        self.txtEmail.text = ""
                        self.txtPhone.text  = ""
                        self.txtSurname.text = ""
                        
                        
                    } catch {
                        print("error:", error)
                        let alert = self.BuildAlertDialog("ลงชื่อเข้าใช้งาน", "การลงชื่อเข้าใช้งานไม่สำเร็จ กรุณาตรวจสอบ ชื่อผู้ใช้งาน และรหัสผ่านอีกครั้ง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: { Void in
                            self.dismiss(animated: true, completion: nil)
                        }))
                        
                        self.present(alert, animated:true, completion:nil)
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
        
        //// clear keyboard state
        txtU.resignFirstResponder()
        txtP.resignFirstResponder()
        txtPhone.resignFirstResponder()
        txtName.resignFirstResponder()
        txtSurname.resignFirstResponder()
        txtPass.resignFirstResponder()
        txtRePass.resignFirstResponder()
        txtEmail.resignFirstResponder()
        /////////////////////////////////
        
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
                    
                    self.txtU.text = ""
                    self.txtP.text = ""
                    self.txtName.text = ""
                    self.txtPass.text = ""
                    self.txtRePass.text = ""
                    self.txtEmail.text = ""
                    self.txtPhone.text  = ""
                    self.txtSurname.text = ""
                    
                    if json["msg"] as! String == "success" {
                        let alert = self.BuildAlertDialog("ลงทะเบียนใหม่", "ลงทะเบียนเรียบร้อยแล้ว โปรดลงชื่อเข้าใช้งานเพื่อเข้าสู่ระบบ", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler:{ action in
                            
                            self.swtLoginRegister.selectedSegmentIndex = 0
                            self.updateSwitchView()
                            
                        }))
                        
                        self.present(alert, animated:true, completion:nil)
                    }else{
                        let alert = self.BuildAlertDialog("ลงทะเบียนใหม่", "ลงทะเบียนไม่สำเร็จ โปรดทำรายการใหม่ภายหลัง\nเนื่องจาก \(json["msg"] as! String)", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: { action in
                        
                            //self.txtU.becomeFirstResponder()
                        
                        }))
                        
                        
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
            
        } // .End if txtU.text == ""
        
        
        if txtP.text == "" {
            let alert = BuildAlertDialog("แจ้งเตือน","โปรดระบุรหัสผ่าน", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: nil))
            
            b = false
            self.present(alert, animated:true, completion:nil)
            
        }
        
        return b
    } // .End validLoginField()
    
    
        
    func BuildAlertDialog(_ t:String, _ m:String, btnAction:UIAlertAction) -> UIAlertController {
        // create the alert
        let alert = UIAlertController(title: t, message: m, preferredStyle: UIAlertControllerStyle.alert)
        
        // add an action (button)
        alert.addAction(btnAction)
        
        return alert
        
    } // .End BuildAlertDialog()
    
    
    func BuildAlertDialog(_ t:String, m:String,  btnAction:UIAlertAction, btnAction2:UIAlertAction) -> UIAlertController {
        
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
        
        self.txtU.text = ""
        self.txtP.text = ""
        self.txtName.text = ""
        self.txtPass.text = ""
        self.txtRePass.text = ""
        self.txtEmail.text = ""
        self.txtPhone.text  = ""
        self.txtSurname.text = ""
        
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
        
        //txtU.text = "0831356653"
        //txtP.text = "6653"
        
        //txtU.text = "0815505881"
        //txtP.text = "5881"
        
        txtPhone.delegate = self
        
        let font:UIFont = UIFont (name: "Kanit-Regular", size :16)!
         swtLoginRegister.setTitleTextAttributes([NSFontAttributeName: font], for: .normal)
        
        loadingDialog = UIAlertController(title: nil, message: "โปรดรอสักครู่...", preferredStyle: .alert)
        
        let loadingIndicator = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50))
        loadingIndicator.hidesWhenStopped = true
        loadingIndicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyle.gray
        loadingIndicator.startAnimating();
        
        self.loadingDialog.view.addSubview(loadingIndicator)
        
        
        // check login status
        
//        self.txtU.delegate = self
//        self.txtP.delegate = self
        self.txtName.delegate = self
        self.txtPass.delegate = self
        self.txtRePass.delegate = self
        self.txtEmail.delegate = self
        self.txtPhone.delegate = self
        self.txtSurname.delegate = self
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
        let defaults = UserDefaults.standard

        do{
            
            let f = defaults.string(forKey: "formToken")
            let c = defaults.string(forKey: "cookieToken")
            
            if f != nil && c != nil {
                
                SharedInfo.getInstance.fname = defaults.string(forKey: "fname")!
                SharedInfo.getInstance.lname = defaults.string(forKey: "lname")!
                SharedInfo.getInstance.member_code = defaults.string(forKey: "member_code")!
                SharedInfo.getInstance.mobile = defaults.string(forKey: "mobile")!
                SharedInfo.getInstance.code_image = defaults.string(forKey: "code_image")!
                SharedInfo.getInstance.email = defaults.string(forKey: "email")!
                SharedInfo.getInstance.createdate = defaults.string(forKey:"createdate")!
                SharedInfo.getInstance.cid_card = defaults.string(forKey:"cid_card")!
                SharedInfo.getInstance.point_summary = defaults.string(forKey:"point_summary")!
                
                
                SharedInfo.getInstance.had_pic_profile = defaults.bool(forKey:"had_pic_profile")
                
                
                SharedInfo.getInstance.formToken = f!
                SharedInfo.getInstance.cookieToken = c!

                // prepare to set home view controller
                let viewController = self.storyboard?.instantiateViewController(withIdentifier: "home_tabview")
                self.present(viewController!, animated:true,completion:nil);
                
                
            }
        }catch {
            
        }
        
    }
  
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}

