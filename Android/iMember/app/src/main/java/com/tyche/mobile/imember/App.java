package com.tyche.mobile.imember;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vinit on 24/5/2560.
 */

public class App {

    static private App object;

    public JSONObject loginObject;
    public JSONObject customerMember;
    public JSONArray transactionDialies;
    public JSONArray redeemTransactions;
    public JSONArray giftCatalogs;
    public JSONArray selectNews;
    public JSONObject objNews;
    public String cookieToken = "";
    public String formToken = "";

    public String m_server = "http://suscoapidev-iCRM.atlasicloud.com";// "http://192.168.88.197/SUSCOAPI/";  //

    static public App getInstance(){
        if(object == null){
            object = new App();
        }

        return object;
    }
}
