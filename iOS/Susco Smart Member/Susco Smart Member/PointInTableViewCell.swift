//
//  PointInTableViewCell.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/26/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class PointInTableViewCell: UITableViewCell {

    
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
