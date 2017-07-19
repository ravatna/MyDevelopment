//
//  HistoryTransactionViewController.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/26/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class HistoryTransactionViewController:
UIViewController, UITableViewDelegate, UITableViewDataSource {

    ///////////////////
    
    // Data model: These strings will be the data for the table view cells
    let animals: [String] = ["เบอร์โทรศัพท์", "เลขประจำตัวประชาชน :", "รหัสผ่าน", "อีเมล์", "member_card","member_transaction"]
    
    // cell reuse id (cells that scroll out of view can be reused)
    let cellReuseIdentifier = "get_point_cell"
    
    
    @IBOutlet weak var swtView: UISegmentedControl!
    @IBOutlet weak var tblPointIn: UITableView!
    @IBOutlet weak var tblPointOut: UITableView!
    @IBOutlet weak var btnBack: UIButton!
    
    
    
    ///////////////////
    
    @IBAction func doBack(_ sender: Any) {
        
        self .dismiss(animated: true, completion: nil)
        
    }
    
    
    @IBAction func doSwitch(_ sender: Any) {
        
        if swtView.selectedSegmentIndex == 0 {
            tblPointIn.isHidden = false
            tblPointOut.isHidden = true
        }else{
            tblPointIn.isHidden = true
            tblPointOut.isHidden = false
        }

    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        
        updateInfo()
        
        
    }
    
    
    func updateInfo(){
    
        
        
        // App.getInstance().transactionDialies = jsonObj.getJSONArray("transaction_daily");
        // App.getInstance().redeemTransactions = jsonObj.getJSONArray("get_redeem_request");
        
    
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
        if tableView.tag == 1 {
            var transactionDialy:[AnyObject]
            
            transactionDialy = SharedInfo.getInstance.jsonTransaction!["transaction_daily"] as! [AnyObject]
            
            return transactionDialy.count
        }
        
        if tableView.tag == 2 {
            var getRedeemRequest:[AnyObject]
            
            getRedeemRequest = SharedInfo.getInstance.jsonTransaction!["get_redeem_request"] as! [AnyObject]
            
            return getRedeemRequest.count
        }
        
        return 0
    }
    
    // create a cell for each table view row
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        // create a new cell if needed or reuse an old one
        let cell:PointInTableViewCell = self.tblPointIn.dequeueReusableCell(withIdentifier: cellReuseIdentifier) as! PointInTableViewCell
        
        
        if tableView.tag == 1 {
        
        var transactionDialy:[AnyObject]
        
        transactionDialy = SharedInfo.getInstance.jsonTransaction!["transaction_daily"] as! [AnyObject]
        
        
        
        // set the text from the data model
        
        cell.lblDate!.text = transactionDialy[indexPath.row]["service_date"] as! String
        
        
        cell.lblBranch!.text = transactionDialy[indexPath.row]["branch_code"] as! String
        cell.lblScore!.text = transactionDialy[indexPath.row]["point_earn"] as! String
        }
        
        if tableView.tag == 2 {
             var getRedeemRequest:[AnyObject]
            
            getRedeemRequest = SharedInfo.getInstance.jsonTransaction!["get_redeem_request"] as! [AnyObject]
            
            
//            ((TextView)giftView.findViewById(R.id.txvCol1)).setText(jsonObj.getString("service_date"));
//            ((TextView)giftView.findViewById(R.id.txvCol2)).setText(jsonObj.getString("branch_code") + " " + jsonObj.getString("item_qty") + " ชิ้น " + " " + jsonObj.getString("item_point") + " คะแนน" );
//            ((TextView)giftView.findViewById(R.id.txvCol3)).setText( jsonObj.getString("member_point"));
//            
            
            // set the text from the data model
            
            cell.lblDate!.text = getRedeemRequest[indexPath.row]["service_date"] as! String
           // print(indexPath.row)
            var s:String = getRedeemRequest[indexPath.row]["branch_code"] as! String + " "
            
            var ss:Int =   getRedeemRequest[indexPath.row]["item_qty"] as! Int
            //print("\(ss)")
            var x = "\(ss) ชิ้น "
            
            var sss = getRedeemRequest[indexPath.row]["item_point"] as! String + " คะแนน"
            
            let a = s  + x + sss
            
            cell.lblBranch!.text = a
            
            cell.lblScore!.text = getRedeemRequest[indexPath.row]["member_point"] as! String
        }
        
        
        return cell
    }
    
    // method to run when table view cell is tapped
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("You tapped cell number \(indexPath.row).")
    }


}