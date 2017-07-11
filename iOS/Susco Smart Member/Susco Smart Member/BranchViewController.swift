//
//  File.swift
//  Susco Smart Member
//
//  Created by TYCHE on 6/20/2560 BE.
//  Copyright © 2560 TYCHE. All rights reserved.
//

import UIKit

class BranchViewControlller : UIViewController, UIWebViewDelegate{

    
    @IBOutlet weak var wvwBarnch: UIWebView!
    
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        loadMap()
        wvwBarnch.delegate = self
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func loadMap()
    {
        
        /*
        //let url = URL (string: "https://www.google.com/maps/d/viewer?mid=1Y0VSBNMlw9Q2r4Wpk5NkPrUbn9s&ll=14.305836340137358%2C102.16185804805923&z=6")
        let url = URL (string: "https://www.google.co.th/search?q=susco+map&oq=susco+map&aqs=chrome..69i57.9535j0j7&sourceid=chrome&ie=UTF-8#q=SUSCO&rflfq=1&rlha=0&rllag=13825228,100502341,7317&tbm=lcl&tbs=lrf:!2m1!1e3!3sEAE,lf:1,lf_ui:3&rlfi=hd:;si:;mv:!1m3!1d135298.98065174193!2d100.55670005!3d13.78422315!2m3!1f0!2f0!3f0!3m2!1i491!2i419!4f13.1;tbs:lrf:!2m1!1e3!3sEAE,lf:1,lf_ui:3")
        
        
        let requestObj = URLRequest(url: url!)
        wvwBarnch.loadRequest(requestObj)
        */
        /////////////////////////////////////////////////////
        
        do {
            guard let filePath = Bundle.main.path(forResource: "newhtml", ofType: "html")
                else {
                    // File Error
                    print ("File reading error")
                    return
            }
            
            let contents =  try String(contentsOfFile: filePath, encoding: .utf8)
            let baseUrl = URL(fileURLWithPath: filePath)
            wvwBarnch.loadHTMLString(contents as String, baseURL: baseUrl)
        }
        catch
        {
            print ("File HTML error")
        }
        
    }
    
    func webView(_ webView: UIWebView, shouldStartLoadWith request: URLRequest, navigationType: UIWebViewNavigationType) -> Bool {
        
        
        if navigationType == UIWebViewNavigationType.linkClicked {
            UIApplication.shared.openURL(request.url!)
            return false
        }
        return true
        
        
    }
    
    /*
    - (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    if (navigationType == UIWebViewNavigationTypeLinkClicked ) {
    [[UIApplication sharedApplication] openURL:[request URL]];
    return NO;
    }
    
    return YES;
    }
 */
    
    
    
}

