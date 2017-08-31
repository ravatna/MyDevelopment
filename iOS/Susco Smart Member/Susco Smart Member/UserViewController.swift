//
//  UserViewController.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/21/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class UserViewController: UIViewController, UITableViewDelegate, UITableViewDataSource ,UIImagePickerControllerDelegate,UINavigationControllerDelegate {

    @IBOutlet weak var imgMember: UIImageView!
    @IBOutlet weak var tblUserMenu: UITableView!
    @IBOutlet weak var txtName: UILabel!
    @IBOutlet weak var btnDialogName: UIButton!
    let picker  = UIImagePickerController()
   
    @IBOutlet weak var btnPickerDialog: UIButton!
    
    var strPassword:String = ""
    var strEmail:String = ""
    var strCidCard:String = ""
    var strImgBase64:String = ""
    var strMobile:String = ""
    var strFname : String = ""
    var strLname : String = ""
    
    var tmp_id_card:String = ""
    var tmp_name:String = ""
    var tmp_email:String = ""
    
    var imgProfile:UIImage?
   
    
    
    var loadingDialog:UIAlertController!
    
    // Data model: These strings will be the data for the table view cells
    let animals: [String] = ["เบอร์โทรศัพท์ :", "เลขประจำตัวประชาชน :", "ชือ - สกุล :", "รหัสผ่าน :", "อีเมล์ :", "member_card","member_transaction","logout"]
    
    // cell reuse id (cells that scroll out of view can be reused)
    let cellReuseIdentifier = "cell"
    
    
    @IBAction func doPickerDialog(_ sender: Any) {
        picker.allowsEditing = false
        picker.sourceType = .photoLibrary
        picker.mediaTypes = UIImagePickerController.availableMediaTypes(for: .photoLibrary)!
        picker.modalPresentationStyle = .popover
        present(picker, animated: true, completion: nil)
        //picker.popoverPresentationController?.barButtonItem = sender as! UIBarButtonItem
    }
    
    @IBAction func doDialogName(_ sender: Any) {
        
        // prepare to set home view controller
        let viewController = self.storyboard?.instantiateViewController(withIdentifier: "popup_input_view") as! OneTextViewController
        
        viewController.caseProcess = 5
        
        self.addChildViewController(viewController)
        viewController.view.frame = self.view.frame
        self.view.addSubview(viewController.view)
        viewController.didMove(toParentViewController: self)
    }
    
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        dismiss(animated: true, completion: nil)
        
        resetImageProfile()
        
       
    }
    
    func resizeImage(image: UIImage, newWidth: CGFloat) -> UIImage {
        
        let scale = newWidth / image.size.width
        let newHeight = image.size.height * scale
       
        UIGraphicsBeginImageContext(CGSize(width:newWidth,height: newHeight))
        
        image.draw(in: CGRect(x:0, y:0, width:newWidth, height:newHeight))
        
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage!
    }
    
    
    
    ////////////////////////////////////////////////
    func GetImageBase64_Profile(_ imageCode:String){
        
        // create post request
        
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/GetPicture/getimagebase64")!
        let jsonDict = [
            "member_code": SharedInfo.getInstance.member_code
            ,"imagecode": imageCode
            ,"Width": "120"
            ,"Height": "120"
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
                        
                        if iconString != "" {
                            decodedData = Data(base64Encoded: iconString)!
                            self.imgProfile = UIImage(data:decodedData)
                            self.imgMember.image = self.imgProfile
                            
                        } // end if
                    }
                })
            } catch {
                print("error:", error)
            }
        }
        
        task.resume()
        
    } // end func

    
    
    func resetImageProfile(){
     
        print(SharedInfo.getInstance.code_image)
        
        
        // check user are set new picture profile or not
        if SharedInfo.getInstance.had_pic_profile {
            //imgMember.image = imgProfile
            
            GetImageBase64_Profile(SharedInfo.getInstance.code_image)
        }
        //. End check use has image from service.
        else
        {
            imgMember.image = UIImage(named: "user_info")
        }
        

    }
    
    
    
    func doSubmitInfo(){
        
        present(self.loadingDialog, animated: true, completion: nil)
        
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/UpdateDetailCustomer/UpdateDetail")!
        let jsonDict = [ "member_code": SharedInfo.getInstance.member_code
            , "password": strPassword
            , "email": strEmail
            , "frist_name": strFname
            , "last_name": strLname
            , "cid_card": strCidCard
            , "imagebase64": strImgBase64
            , "formToken": SharedInfo.getInstance.formToken
            , "cookieToken": SharedInfo.getInstance.cookieToken ]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: jsonDict, options: [])
        
        var request = URLRequest(url: url)
        request.httpMethod = "post"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsonData
        
        let task = URLSession.shared.dataTask(with: request) {
            (data, response, error) in
            
            
            // set process without waiting.
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
                        print(json)
                        if json["success"] as! Bool == true {
                            
                            let alert = self.BuildAlertDialog("ปรับปรุงข้อมูล", "ปรับปรุงข้อมูลเรียบร้อยแล้ว\n กรุณาออกจากระบบและเข้าสู่ระบบใหม่อีกครั้ง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler:{ action in
                                
                                
                            }))
                            
                            self.present(alert, animated:true, completion:nil)
                            
                            
                            
                            if self.strCidCard != "" {
                                SharedInfo.getInstance.tmp_id_card = self.strCidCard
                            }
                            
                            if self.strEmail != "" {
                                SharedInfo.getInstance.tmp_email = self.strEmail
                            }
                            
                            if self.strFname != "" {
                                SharedInfo.getInstance.tmp_fname = self.strFname
                                SharedInfo.getInstance.tmp_lname = self.strLname
                            }
                            
                            
                            self.GetUserInfo()
                        }else{
                            let alert = self.BuildAlertDialog("ปรับปรุงข้อมูล", "ไม่สามารถแก้ไขข้อมูลขณะนี้ โปรดทำรายการใหม่ภายหลัง", btnAction: UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default, handler: {action in
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

    
    func GetUserInfo(){
        
        
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/GetInfo/Customer")!
        let jsonDict = [ "membercode": SharedInfo.getInstance.member_code
           
            , "formToken": SharedInfo.getInstance.formToken
            , "cookieToken": SharedInfo.getInstance.cookieToken ]
        
        let jsonData = try! JSONSerialization.data(withJSONObject: jsonDict, options: [])
        
        var request = URLRequest(url: url)
        request.httpMethod = "post"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsonData
        
        let task = URLSession.shared.dataTask(with: request) {
            (data, response, error) in
            
            
            // set process without waiting.
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
                        print(json)
                        if json["success"] as! Bool == true {
                            
                           
                            let customer = json["customer_detail"] as! [AnyObject]
                            
                            SharedInfo.getInstance.jsonCustomer = customer
                            
                            let defaults = UserDefaults.standard
                            //print(customer)
                            defaults.setValue(customer, forKey: "customer_detail")
                            
                            
                        }
                        
                        
                    } catch {
                        print("error:", error)
                    }
                    
                    
                    
                    
                })
                
            }) // end DispatchQueue
            
            
        }
        
        task.resume()
        
    }// end func
    
    
    
    

    
    
    func imagePickerController(_ picker: UIImagePickerController,
                               didFinishPickingMediaWithInfo info: [String : AnyObject])
    {
        let chosenImage = info[UIImagePickerControllerOriginalImage] as! UIImage //2
        //imgMember.contentMode = .scaleAspectFit //3
        
        let resizedImage = resizeImage(image: chosenImage, newWidth: 200)
        
        imgMember.image = resizedImage //4
        
        dismiss(animated:true, completion: nil) //5
        
       
        let jpegCompressionQuality: CGFloat = 0.8 // Set this to whatever suits your purpose
        if let base64String = UIImageJPEGRepresentation(resizedImage, jpegCompressionQuality)?.base64EncodedString() {
            // Upload base64String to your database
            
            
            ///////////////////////
            
            let alert = UIAlertController(title: "ยืนยัน", message: "ต้องการเปลี่ยนแปลงภาพ ?", preferredStyle: .alert)
            
            let clearAction = UIAlertAction(title: "ใช่", style: .destructive) { (alert: UIAlertAction!) -> Void in
                
                /////////////////////////////////
                
                self.strImgBase64 = base64String
                self.doSubmitInfo()
                
                /////////////////////////////////
                
            }
            
            let cancelAction = UIAlertAction(title: "ไม่ใช่", style: .default) { (alert: UIAlertAction!) -> Void in
                //print("You pressed Cancel")
                
                self.resetImageProfile()
            }
            
            alert.addAction(clearAction)
            alert.addAction(cancelAction)
            
            present(alert, animated: true, completion:nil)
        }

    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        
        // Register the table view cell class and its reuse id
        //self.tblUserMenu.register(UserTableViewCell.self, forCellReuseIdentifier: cellReuseIdentifier)
        
        // This view controller itself will provide the delegate methods and row data for the table view.
    
        picker.delegate = self
        
     
        resetImageProfile()
        
        
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
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
    
    // number of rows in table view
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.animals.count
    }
    
    // create a cell for each table view row
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        // create a new cell if needed or reuse an old one
        let cell:UserTableViewCell = self.tblUserMenu.dequeueReusableCell(withIdentifier: cellReuseIdentifier) as! UserTableViewCell
        
        
        
            let fname = SharedInfo.getInstance.fname
            let lname = SharedInfo.getInstance.lname
        
            let phone = SharedInfo.getInstance.mobile
            let email = SharedInfo.getInstance.email
            let cid_card = SharedInfo.getInstance.cid_card
        
        
            let tmp_name = fname + " " + lname
        
        
        SharedInfo.getInstance.tmp_email = email
        SharedInfo.getInstance.tmp_fname = fname
        SharedInfo.getInstance.tmp_lname = lname
        SharedInfo.getInstance.tmp_id_card = cid_card
        
        
        // set the text from the data model
        cell.imgIcon.isHidden = true
        cell.lblCaption!.text = self.animals[indexPath.row]
        cell.lblWording!.text = self.animals[indexPath.row]
        
        if indexPath.row == 0 {
            cell.lblWording.text = phone
        }
        
        if indexPath.row == 1 {
            
            if cid_card != "" {
                cell.lblWording.text = SharedInfo.getInstance.tmp_id_card
            }else{
                cell.lblWording.text = "* แตะที่นี่เพื่อแก้ไข *"
            }
        }
        
        if indexPath.row == 2 {
            
            
            if SharedInfo.getInstance.tmp_fname  != "" || SharedInfo.getInstance.tmp_lname != "" {
                cell.lblWording.text = tmp_name
            }else{
                cell.lblWording.text = "* แตะที่นี่เพื่อแก้ไข *"
            }
        }
        
        if indexPath.row == 3 {
            cell.lblWording.text = "******"
            
        }
        
        if indexPath.row == 4 {
            
            if SharedInfo.getInstance.tmp_email != "" {
                cell.lblWording.text = SharedInfo.getInstance.tmp_email
            }else{
                cell.lblWording.text = "* แตะที่นี่เพื่อแก้ไข *"
            }
            
            
        }
        
        if self.animals[indexPath.row] == "member_card" {
            cell.imgIcon!.isHidden = false
            cell.lblCaption!.isHidden = true
            cell.imgIcon!.image = UIImage(named: "member_card_64")
            
            cell.lblWording!.text = "บัตรสมาชิก"
        }
        
        if self.animals[indexPath.row] == "member_transaction" {
            cell.imgIcon!.isHidden = false
            cell.lblCaption!.isHidden = true
            cell.imgIcon!.image = UIImage(named: "document_64")
            
            cell.lblWording!.text = "ประวัติการทำรายการ"
        }
        
        cell.imgIcon2!.isHidden = true
        
        if self.animals[indexPath.row] == "logout" {

            cell.lblCaption!.text = ""
            cell.imgIcon2!.isHidden = false
            cell.lblWording!.text = "ลงชื่อออก"
        }
        
        return cell
    }
    
    // method to run when table view cell is tapped
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        //print("You tapped cell number \(indexPath.row).")
        
        
        // edit email
        if indexPath.row <= 4 {

            if  indexPath.row == 0 {
                
                if SharedInfo.getInstance.mobile == ""
                {
                    // prepare to set home view controller
                    let viewController = self.storyboard?.instantiateViewController(withIdentifier: "popup_input_view") as! OneTextViewController
                    
                    viewController.caseProcess = indexPath.row
                    
                    self.addChildViewController(viewController)
                    viewController.view.frame = self.view.frame
                    self.view.addSubview(viewController.view)
                    viewController.didMove(toParentViewController: self)
                }
                else
                {
                    // display alert not change anything
                    let alert = UIAlertController(title: "แก้ไขเบอร์ติดต่อ", message: SharedInfo.getInstance.strContact, preferredStyle: UIAlertControllerStyle.alert)
                    let clearAction = UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default) {
                        (alert: UIAlertAction!) -> Void in
                        //self.dismiss(animated: true, completion: nil)
 
                    } // end clearAction
                    
                    alert.addAction(clearAction)
                    present(alert, animated: true, completion:nil)

                } // end else
                
                
            }else if indexPath.row == 1 {
                print(SharedInfo.getInstance.tmp_id_card)
                if SharedInfo.getInstance.tmp_id_card == ""
                {
                    // prepare to set home view controller
                    let viewController = self.storyboard?.instantiateViewController(withIdentifier: "popup_input_view") as! OneTextViewController
                    
                    viewController.caseProcess = indexPath.row
                    
                    self.addChildViewController(viewController)
                    viewController.view.frame = self.view.frame
                    self.view.addSubview(viewController.view)
                    viewController.didMove(toParentViewController: self)
                    
                }
                else
                {
                    // display alert not change anything
                    let alert = UIAlertController(title: "แก้ไขเลขบัตรประจำตัวประชาชน", message: SharedInfo.getInstance.strContact, preferredStyle: UIAlertControllerStyle.alert)
                    let clearAction = UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default) { (alert: UIAlertAction!) -> Void in
                        //self.dismiss(animated: true, completion: nil)
                        
                    } // end clearAction
                    
                    alert.addAction(clearAction)
                    present(alert, animated: true, completion:nil)
                    
                } // end else

            }
            else if indexPath.row == 2 {
               // print(SharedInfo.getInstance.tmp_fname + " " + SharedInfo.getInstance.tmp_lname)
                if SharedInfo.getInstance.tmp_fname   == "" || SharedInfo.getInstance.tmp_lname == ""
                {
                    // prepare to set home view controller
                    let viewController = self.storyboard?.instantiateViewController(withIdentifier: "popup_input_view") as! OneTextViewController
                    
                    viewController.caseProcess = indexPath.row
                    
                    self.addChildViewController(viewController)
                    viewController.view.frame = self.view.frame
                    self.view.addSubview(viewController.view)
                    viewController.didMove(toParentViewController: self)
                    
                }
                else
                {
                    // display alert not change anything
                    let alert = UIAlertController(title: "แก้ไข ชื่อ - สกุล", message: SharedInfo.getInstance.strContact, preferredStyle: UIAlertControllerStyle.alert)
                    let clearAction = UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default) { (alert: UIAlertAction!) -> Void in
                        //self.dismiss(animated: true, completion: nil)
                        
                    } // end clearAction
                    
                    alert.addAction(clearAction)
                    present(alert, animated: true, completion:nil)
                    
                } // end else
                
            }
            
            else if indexPath.row == 3
            {
                // prepare to set home view controller
                let viewController = self.storyboard?.instantiateViewController(withIdentifier: "popup_input_view") as! OneTextViewController
                
                viewController.caseProcess = indexPath.row
                
                self.addChildViewController(viewController)
                viewController.view.frame = self.view.frame
                self.view.addSubview(viewController.view)
                viewController.didMove(toParentViewController: self)
            
            }
            
            else if indexPath.row == 4
            {
                if SharedInfo.getInstance.tmp_email == ""
                {
                    // prepare to set home view controller
                    let viewController = self.storyboard?.instantiateViewController(withIdentifier: "popup_input_view") as! OneTextViewController
                    
                    viewController.caseProcess = indexPath.row
                    
                    self.addChildViewController(viewController)
                    viewController.view.frame = self.view.frame
                    self.view.addSubview(viewController.view)
                    viewController.didMove(toParentViewController: self)
                    
                }
                else
                {
                    // display alert not change anything
                    let alert = UIAlertController(title: "แก้ไขอีเมล์", message: SharedInfo.getInstance.strContact, preferredStyle: UIAlertControllerStyle.alert)
                    let clearAction = UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default) { (alert: UIAlertAction!) -> Void in
                        //self.dismiss(animated: true, completion: nil)
                        
                    } // end clearAction
                    
                    alert.addAction(clearAction)
                    present(alert, animated: true, completion:nil)
                    
                } // end else

            } //end if
            
        } // end if

        
        if indexPath.row == 5 {
            // prepare to set home view controller
            let viewController = self.storyboard?.instantiateViewController(withIdentifier: "card_view")
            self.present(viewController!, animated:true,completion:nil);
            
        }
        
        if indexPath.row == 6 {
            // prepare to set home view controller
            let viewController = self.storyboard?.instantiateViewController(withIdentifier: "history_view")
            self.present(viewController!, animated:true,completion:nil);
            
        }
        
        
        // if press logout
        if indexPath.row == 7 {
            
            
            let myString  = "ลงชื่อออก"
            var myMutableString = NSMutableAttributedString()
            myMutableString = NSMutableAttributedString(string: myString as String, attributes: [NSFontAttributeName:UIFont(name: "Kanit-Regular", size: 16.0)!])
            
            // Change Message With Color and Font
            
            let message  = "ต้องการออกจากระบบ SUSCO Smart Member ใช่หรือไม่ ?"
            var messageMutableString = NSMutableAttributedString()
            messageMutableString = NSMutableAttributedString(string: message as String, attributes: [NSFontAttributeName:UIFont(name: "Kanit-Regular", size: 16.0)!])
          //  messageMutableString.addAttribute(NSForegroundColorAttributeName, value: UIColor.greenColor(), range: NSRange(location:0,length:message.characters.count))
           // alertController.setValue(messageMutableString, forKey: "attributedMessage")

             
            let alert = UIAlertController(title: "ลงชื่อออก", message: "ต้องการออกจากระบบ SUSCO Smart Member ใช่หรือไม่ ?", preferredStyle: UIAlertControllerStyle.alert)
            //alert.setValue(myMutableString, forKey: "attributedTitle")

            let clearAction = UIAlertAction(title: "ออก", style: .destructive) { (alert: UIAlertAction!) -> Void in
                SharedInfo.getInstance.clear()
                
                let defaults = UserDefaults.standard
                
                //@todo: remove  save default section
                
                defaults.removeObject( forKey: "fname")
                defaults.removeObject( forKey: "lname")
                defaults.removeObject( forKey: "member_code")
                defaults.removeObject( forKey: "mobile")
                defaults.removeObject( forKey: "code_image")
                defaults.removeObject( forKey: "email")
                defaults.removeObject( forKey: "cid_card")
                defaults.removeObject( forKey: "createdate")
                defaults.removeObject( forKey: "point_summary")
                
                defaults.removeObject( forKey: "formToken")
                defaults.removeObject( forKey: "cookieToken")
                defaults.removeObject( forKey: "pw")
                
                
                
               self.dismiss(animated: true, completion: nil)

                
            }
            
            
            let cancelAction = UIAlertAction(title: "ยกเลิก", style: .default)
            {
                (alert: UIAlertAction!) -> Void in
                            }
            
            alert.addAction(clearAction)
            alert.addAction(cancelAction)
            
            present(alert, animated: true, completion:nil)
            
            
        }
        
        
        tableView.deselectRow(at: indexPath, animated: false)

        
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

    

    
}
