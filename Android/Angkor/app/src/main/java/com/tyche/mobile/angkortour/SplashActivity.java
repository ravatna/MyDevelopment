package com.tyche.mobile.angkortour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String MY_PREFS = "susco_tyche";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getApplicationContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);

        if(!sharedPreferences.getString("login_json","{}").equals("{}")){
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(sharedPreferences.getString("login_json","{}"));
                App.getInstance().loginObject = jsonObj;

                JSONArray arr = jsonObj.getJSONArray("customer_detail");

                App.getInstance().formToken = jsonObj.getString("formToken");
                App.getInstance().cookieToken = jsonObj.getString("cookieToken");

                App.getInstance().customerMember = arr.getJSONObject(0);
                App.getInstance().selectNews = jsonObj.getJSONArray("select_news");

                // when logoin state valid next load dialy transaction
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) { }

                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).start();
            }
        }else{
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) { }

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).start();
        }




    } // end onCreate

} // end class