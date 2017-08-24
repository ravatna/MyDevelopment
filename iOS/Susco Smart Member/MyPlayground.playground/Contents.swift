 import UIKit


 var testStr = "0000000000"
 
 let PHONE_REGEX = "^(08)d{8}"
 let phoneTest = NSPredicate(format: "SELF MATCHES %@", PHONE_REGEX)
 let result =  phoneTest.evaluate(with: testStr)
print (result)