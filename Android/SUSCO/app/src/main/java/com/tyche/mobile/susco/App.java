package com.tyche.mobile.susco;

import android.graphics.Bitmap;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    Bitmap imgProfile = null;
    Bitmap imgTempProfile = null;
    Boolean showProgressDialog = true;

    String m_server = "http://suscoapidev-iCRM.atlasicloud.com/V2";// "http://192.168.88.197/SUSCOAPI/";  //
    public boolean needUpdateImageProfile = false;
    //String m_server =  "http://192.168.88.174/SUSCOAPIV2/";  //

    static public App getInstance(){
        if(object == null){
            object = new App();
        }

        return object;
    }


    public static String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public  boolean validThaiIDCard(long v) {
        long id = v;
        if (Long.toString(v).equals("1111111111111")) return false;
        String c = Long.toString(v).substring(Long.toString(v).length()-1);

        long base = 100000000000l; //สร้างตัวแปร เพื่อสำหรับให้หารเพื่อเอาหลักที่ต้องการ
        int basenow; //สร้างตัวแปรเพื่อเก็บค่าประจำหลัก
        int sum = 0; //สร้างตัวแปรเริ่มตัวผลบวกให้เท่ากับ 0
        for(int i = 13; i > 1; i--) { //วนรอบตั้งแต่ 13 ลงมาจนถึง 2
            basenow = (int)Math.floor(id/base); //หาค่าประจำตำแหน่งนั้น ๆ
            id = id - basenow*base; //ลดค่า id ลงทีละหลัก
            System.out.println(basenow + "x" + i + " = " + (basenow*i)); //แสดงค่าเมื่อคูณแล้วของแต่ละหลัก
            sum += basenow*i; //บวกค่า sum ไปเรื่อย ๆ ทีละหลัก
            base = base/10; //ตัดค่าที่ใช้สำหรับการหาเลขแต่ละหลัก
        }
        //System.out.println("Sum is " + sum); //แสดงค่า sum
        int checkbit = (11 - (sum%11))%10; //คำนวณค่า checkbit
        int cc = Integer.parseInt(c);
        //System.out.println("Check bit is " + checkbit); //แสดงค่า checkbit ที่ได้
        return (cc == checkbit);
    }



    /**
     * Validate hex with regular expression
     *
     * @param s
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validEmail( String s) {
         Pattern pattern;
         Matcher matcher;
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);

        matcher = pattern.matcher(s);
        return matcher.matches();

    }

    /**
     * Validate hex with regular expression
     *
     * @param s
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validMobilePhone( String s) {
        Pattern pattern;
        Matcher matcher;
        String PATTERN = "^(0)[0-9]{9}";
        pattern = Pattern.compile(PATTERN);

        matcher = pattern.matcher(s);
        return matcher.matches();
    }

}
