package com.tyche.mobile.susco;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vinit on 24/5/2560.
 */

public class App {

    static private App object;

    JSONObject loginObject;
    JSONObject customerMember;
    JSONArray transactionDialies;
    JSONArray redeemTransactions;
    JSONArray giftCatalogs;
    JSONArray selectNews;
    JSONObject objNews;
    JSONObject objBanner;
    String cookieToken,formToken;

    String m_server = "http://suscoapidev-iCRM.atlasicloud.com/V2";// "http://192.168.88.197/SUSCOAPI/";  //
//    String m_server =  "http://192.168.88.196/SUSCOAPIV2/";  //

    static public App getInstance(){
        if(object == null){
            object = new App();
        }

        return object;
    }
}
