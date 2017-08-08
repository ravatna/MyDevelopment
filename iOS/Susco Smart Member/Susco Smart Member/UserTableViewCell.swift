//
//  UserTableViewCell.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/23/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class UserTableViewCell: UITableViewCell {

    @IBOutlet weak var lblWording: UILabel!
    @IBOutlet weak var lblCaption: UILabel!
    @IBOutlet weak var imgIcon: UIImageView!
    @IBOutlet weak var imgIcon2: UIImageView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
