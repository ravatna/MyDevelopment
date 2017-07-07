//
//  PointInTableViewCell.swift
//  Susco Smart Member
//
//  Created by Tyche on 6/26/17.
//  Copyright © 2017 TYCHE. All rights reserved.
//

import UIKit

class PointOutTableViewCell: UITableViewCell {

    
    @IBOutlet weak var lblDate: UILabel!
    @IBOutlet weak var lblScore: UILabel!
    @IBOutlet weak var lblBranch: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
