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
    let months: [String] = [
        "มกราคม"
        , "กุมภาพันธ์"
        , "มีนาคม"
        , "เมษายน"
        , "พฤษภาคม"
        ,"มิถุนายน"
        ,"กรกฎาคม"
        ,"สิงหาคม"
        ,"กันยายน"
        ,"ตุลาคม"
        ,"พฤศจิกายน"
        ,"ธันวาคม"
    ]
    
    
    
    // cell reuse id (cells that scroll out of view can be reused)
    let cellReuseIdentifier = "get_point_cell"
    
    
    @IBOutlet weak var swtView: UISegmentedControl!
    @IBOutlet weak var tblPointIn: UITableView!
    @IBOutlet weak var tblPointOut: UITableView!
    @IBOutlet weak var btnBack: UIButton!
    
    @IBOutlet weak var lblMonthLy: UILabel!
    
    var refreshControl:UIRefreshControl!
    
    @IBOutlet weak var scrMain: UIScrollView!
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
    
    func handleRefresh(_ refreshControl: UIRefreshControl) {
        // Do some reloading of data and update the table view's data source
        // Fetch more objects from a web service, for example...
        
        // Simply adding an object to the data source for this example
        
        doLoadTransaction()
        
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        refreshControl = UIRefreshControl()
        
        refreshControl.backgroundColor = UIColor.white
        refreshControl.tintColor = UIColor.darkGray
        refreshControl?.addTarget(self, action: #selector(handleRefresh), for: UIControlEvents.valueChanged)
        
        
        scrMain.addSubview(refreshControl)
        
        scrMain.contentSize.height = UIScreen.main.bounds.height
        
        
        let currentDate = Date()
        
        
        let monthFormatter = DateFormatter()
        let yearFormatter = DateFormatter()
        
        monthFormatter.dateFormat = "MM"
        yearFormatter.dateFormat = "yyyy"
        let m = monthFormatter.string(from:currentDate)
        let y = yearFormatter.string(from:currentDate)
        
        
        let iMonth: Int = Int(m)!
        let iYear: Int = Int(y)!
        
        let mm = months[iMonth-1]
        let yy = String(describing: (iYear+543))
        
        lblMonthLy.text = "ข้อมูลประจำเดือน " + mm + " " + yy

    }
    
    
    
    // mark: -- make connection to server
    func doLoadTransaction(){
        
        let customer:[AnyObject]
        
        customer = SharedInfo.getInstance.jsonCustomer!
        
        let membercode = customer[0]["member_code"] as! String
        let formToken:String = SharedInfo.getInstance.formToken
        let cookieToken:String = SharedInfo.getInstance.cookieToken
        
        // create post request
        let url = URL(string: SharedInfo.getInstance.serviceUrl + "/ListTransactionCustomer/GetTransactionByMember")!
        let jsonDict = [ "membercode": membercode
            ,"formToken": formToken
            ,"cookieToken": cookieToken ]
        let jsonData = try! JSONSerialization.data(withJSONObject: jsonDict, options: [])
        
        var request = URLRequest(url: url)
        request.httpMethod = "post"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = jsonData
        
        
        let task = URLSession.shared.dataTask(with: request) {
            (data, response, error) in
            
            // dismiss alert view
            //  self.loadingDialog.dismiss(animated: true, completion: nil)
            
            self.refreshControl.endRefreshing()
            
            if let error = error {
                print("error:", error)
                return
            }
            
            do {
                guard let data = data else { return }
                guard let json = try JSONSerialization.jsonObject(with: data, options: []) as? [String: AnyObject] else { return }
                
                
                // assign result from
                SharedInfo.getInstance.jsonTransaction = json;
                
                self.tblPointIn.reloadData()
                self.tblPointOut.reloadData()
                
                
                
                
                let currentDate = Date()
                
                
                let monthFormatter = DateFormatter()
                let yearFormatter = DateFormatter()
                
                monthFormatter.dateFormat = "MM"
                yearFormatter.dateFormat = "yyyy"
                let m = monthFormatter.string(from:currentDate)
                let y = yearFormatter.string(from:currentDate)
                
                
                let iMonth: Int = Int(m)!
                let iYear: Int = Int(y)!
                
                
                
                let mm = self.months[iMonth-1]
                let yy = String(describing: (iYear+543))
                
                self.lblMonthLy.text = "ข้อมูลประจำเดือน " + mm + " " + yy
                
            } catch {
                print("error:", error)
            }
        }
        
        task.resume()
        
    } // end func

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
