//
//  UserViewController.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/21/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class UserViewController: UIViewController, UITableViewDelegate, UITableViewDataSource  {

    @IBOutlet weak var imgMember: UIImageView!
    @IBOutlet weak var tblUserMenu: UITableView!
    
    // Data model: These strings will be the data for the table view cells
    let animals: [String] = ["เบอร์โทรศัพท์", "เลขประจำตัวประชาชน :", "รหัสผ่าน", "อีเมล์", "member_card","member_transaction","logout"]
    
    // cell reuse id (cells that scroll out of view can be reused)
    let cellReuseIdentifier = "cell"
    
    
    
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        
        // Register the table view cell class and its reuse id
        //self.tblUserMenu.register(UserTableViewCell.self, forCellReuseIdentifier: cellReuseIdentifier)
        
        // This view controller itself will provide the delegate methods and row data for the table view.
    
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
        
        
        
        let customer:[AnyObject]
       
            customer = SharedInfo.getInstance.json!["customer_detail"] as! [AnyObject]
            
            print(customer[0])
            var fname = customer[0]["fname"] as! String
            var lname = customer[0]["lname"] as! String
            var code = customer[0]["member_code"] as! String
            var score = customer[0]["point_summary"] as! String
            var phone = customer[0]["mobile"] as! String
            var email = customer[0]["email"] as! String
            var cid_card = customer[0]["cid_card"] as! String
            
            
         

        
        // set the text from the data model
        cell.imgIcon.isHidden = true
        cell.lblCaption!.text = self.animals[indexPath.row]
        cell.lblWording!.text = self.animals[indexPath.row]
        
        if indexPath.row == 0 {
            cell.lblWording.text = phone
        
        }
        if indexPath.row == 1 {
            cell.lblWording.text = cid_card
            
        }
        if indexPath.row == 2 {
            cell.lblWording.text = "******"
            
        }
        
        if indexPath.row == 3 {
            cell.lblWording.text = email
            
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
            //cell.imgIcon!.isHidden = false
            //cell.lblCaption!.isHidden = true
            //cell.imgIcon!.image = UIImage(named: "document_64")
            cell.lblCaption!.text = ""
            cell.imgIcon2!.isHidden = false
            cell.lblWording!.text = "ลงชื่อออก"
        }
        
        return cell
    }
    
    // method to run when table view cell is tapped
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("You tapped cell number \(indexPath.row).")
        
        
        // edit email
        if indexPath.row == 0 || indexPath.row == 1 || indexPath.row == 2 || indexPath.row == 3 {
            
            let customer:[AnyObject]
            
            customer = SharedInfo.getInstance.json!["customer_detail"] as! [AnyObject]
            
            var phone = customer[0]["mobile"] as! String
            var email = customer[0]["email"] as! String
            var cid_card = customer[0]["cid_card"] as! String
            

            if  indexPath.row == 0 {
                
                if phone == ""
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
                    let alert = UIAlertController(title: "แก้ไขข้อมูล", message: "โปรดติดต่อสำนักงาน SUSCO เพื่อขอแก้ไขข้อมูล", preferredStyle: UIAlertControllerStyle.alert)
                    let clearAction = UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default) {
                        (alert: UIAlertAction!) -> Void in
                        //self.dismiss(animated: true, completion: nil)
 
                    } // end clearAction
                    
                    alert.addAction(clearAction)
                    present(alert, animated: true, completion:nil)

                } // end else
                
                
            }else if indexPath.row == 1 {
                
                if cid_card == ""
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
                    let alert = UIAlertController(title: "แก้ไขข้อมูล", message: "โปรดติดต่อสำนักงาน SUSCO เพื่อขอแก้ไขข้อมูล", preferredStyle: UIAlertControllerStyle.alert)
                    let clearAction = UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default) { (alert: UIAlertAction!) -> Void in
                        //self.dismiss(animated: true, completion: nil)
                        
                    } // end clearAction
                    
                    alert.addAction(clearAction)
                    present(alert, animated: true, completion:nil)
                    
                } // end else

            }
            
            else if indexPath.row == 2
            {
                // prepare to set home view controller
                let viewController = self.storyboard?.instantiateViewController(withIdentifier: "popup_input_view") as! OneTextViewController
                
                viewController.caseProcess = indexPath.row
                
                self.addChildViewController(viewController)
                viewController.view.frame = self.view.frame
                self.view.addSubview(viewController.view)
                viewController.didMove(toParentViewController: self)
            
            }
            
            else if indexPath.row == 3
            {
                if email == ""
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
                    let alert = UIAlertController(title: "แก้ไขข้อมูล", message: "โปรดติดต่อสำนักงาน SUSCO เพื่อขอแก้ไขข้อมูล", preferredStyle: UIAlertControllerStyle.alert)
                    let clearAction = UIAlertAction(title: "ปิด", style: UIAlertActionStyle.default) { (alert: UIAlertAction!) -> Void in
                        //self.dismiss(animated: true, completion: nil)
                        
                    } // end clearAction
                    
                    alert.addAction(clearAction)
                    present(alert, animated: true, completion:nil)
                    
                } // end else

            } //end if
            
        } // end if

        
        if indexPath.row == 4 {
            // prepare to set home view controller
            let viewController = self.storyboard?.instantiateViewController(withIdentifier: "card_view")
            self.present(viewController!, animated:true,completion:nil);
            
        }
        
        if indexPath.row == 5 {
            // prepare to set home view controller
            let viewController = self.storyboard?.instantiateViewController(withIdentifier: "history_view")
            self.present(viewController!, animated:true,completion:nil);
            
        }
        
        
        // if press logout
        if indexPath.row == 6 {
            let alert = UIAlertController(title: "ลงชื่อออก", message: "ต้องการออกจากระบบ Susco Smart Member ใช่หรือไม่ ?", preferredStyle: UIAlertControllerStyle.alert)
            let clearAction = UIAlertAction(title: "ใช่", style: .destructive) { (alert: UIAlertAction!) -> Void in
                SharedInfo.getInstance.clear()
                
                // prepare to set home view controller
                //let viewController = self.storyboard?.instantiateViewController(withIdentifier: "login_view")
                //self.present(viewController!, animated:true,completion:nil);
               self.dismiss(animated: true, completion: nil)

                
            }
            
            let cancelAction = UIAlertAction(title: "ไม่ใช่", style: .default)
            {
                (alert: UIAlertAction!) -> Void in
                //print("You pressed Cancel")
            }
            
            alert.addAction(clearAction)
            alert.addAction(cancelAction)
            
            present(alert, animated: true, completion:nil)
            
            
        }
        
        
        tableView.deselectRow(at: indexPath, animated: false)

        
    }
    
    
   
    
    

    
}
