package com.tyche.mobile.susco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private Button btnSwitchLogin,btnSwitchRegister;
    private LinearLayout lnrLogin;
    private LinearLayout lnrRegister;
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtRegistUsername;
    private EditText edtRegistPassword;
    private EditText edtRePassword;
    private EditText edtFname;
    private EditText edtLname;
    private EditText edtEmail;
    private EditText edtPhone;
    private Button btnLogin;
    private Button btnRegister;

    String m_formToken,m_cookieToken;
    private TextView txvVersion;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String MY_PREFS = "susco_tyche";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getApplicationContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initView();
    }

    private void initView() {

        btnSwitchLogin = (Button)findViewById(R.id.btnSwitchLogin);
        btnSwitchRegister = (Button)findViewById(R.id.btnSwitchRegister);

        lnrLogin = (LinearLayout)findViewById(R.id.lnrLogin);
        lnrRegister = (LinearLayout)findViewById(R.id.lnrRegister);

        edtUsername = (EditText)findViewById(R.id.edtUsername);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        //edtUsername.setText("0831356653");
        //edtPassword.setText("6653");


        edtRegistPassword = (EditText)findViewById(R.id.edtRegistPassword);

        edtRePassword = (EditText)findViewById(R.id.edtRePassword);
        edtFname = (EditText)findViewById(R.id.edtFname);
        edtLname = (EditText)findViewById(R.id.edtLname);
        edtPhone = (EditText)findViewById(R.id.edtPhone);
        edtEmail = (EditText)findViewById(R.id.edtEmail);


//        edtPhone.setText("0831356653");
//        edtRePassword.setText("6653");
//        edtRegistPassword.setText("6653");

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        txvVersion = (TextView)findViewById(R.id.txvVersion);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            boolean b = true;
            //@todo : validate user input data.

            if(edtPhone.getText().toString().length() != 10){
                edtPhone.setError("ระบุหมายเลขโทรศัพท์");
                b = false;
            }

            if(edtFname.getText().toString().length() < 1){
                edtFname.setError("ระบุชื่อ");
                b = false;
            }

            if(edtLname.getText().toString().length() < 1){
                edtLname.setError("ระบุนามสกุล");
                b = false;
            }

            if(edtEmail.getText().toString().length() < 4){
                edtEmail.setError("อีเมล์ไม่ถูกต้อง");
                b = false;
            }

                if(edtRegistPassword.getText().toString().length() <=0){
                    edtEmail.setError("ระบุรหัสผ่าน");
                    b = false;
                }

            if(! edtRegistPassword.getText().toString().equals(edtRePassword.getText().toString())  ){
                edtRePassword.setError("ยืนยันรหัสผ่านไม่ถูกต้อง");
                b = false;
            }

            if(b){
                doRegister();
            }


            }
        });

        btnSwitchLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterToLoginView();
            }
        });

        btnSwitchRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginToRegisterView();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = true;
                //@todo : validate user input data.

                if(edtUsername.getText().toString().length() <= 0){
                    edtUsername.setError("โปรดระบุชื่อผู้ใช้งาน");
                    b = false;
                }

                if(edtPassword.getText().toString().length() <= 0){
                    edtPassword.setError("โปรดระบุรหัสผ่าน");
                    b = false;
                }

                if(b) {
                    doLogin();
                }
            }
        });

//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(LoginActivity.this,"Registered.",Toast.LENGTH_LONG).show();
//                doRegister();
//            }
//        });

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            txvVersion.setText("v." + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        overrideFonts(this,findViewById(R.id.contentView) );

    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Kanit-Regular.ttf"));
            }
        } catch (Exception e) {
            Log.e("UpdateFontface",e.getMessage());
        }
    } // end method

    private void RegisterToLoginView() {
        lnrRegister.setVisibility(View.GONE);
        lnrLogin.setVisibility(View.VISIBLE);

        btnSwitchLogin.setTextColor(Color.WHITE);
        btnSwitchLogin.setBackgroundResource(R.drawable.button_left_radius_blue);

        btnSwitchRegister.setTextColor(Color.BLUE);
        btnSwitchRegister.setBackgroundResource(R.drawable.button_right_radius_white);

    }

    private void LoginToRegisterView() {
        lnrLogin.setVisibility(View.GONE);
        lnrRegister.setVisibility(View.VISIBLE);

        btnSwitchLogin.setTextColor(Color.BLUE);
        btnSwitchLogin.setBackgroundResource(R.drawable.button_left_radius_white);

        btnSwitchRegister.setTextColor(Color.WHITE);
        btnSwitchRegister.setBackgroundResource(R.drawable.button_right_radius_blue);
    }


    private void doLogin() {

        new Login().execute();
    }

    private class Login extends AsyncTask<Void, Void, String> {
        String strJson,postUrl;
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
            strJson = "{'mobile_customer':'" + edtUsername.getText().toString()  + "','pass_customer':'" + edtPassword.getText().toString() + "'}";
            postUrl  = App.getInstance().m_server + "/Security/login_customer_susco";
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("กำลังดำเนินการ...");
            pd.setCancelable(false);
            pd.show();

        }

        protected String doInBackground(Void... urls)   {

            String result = null;
            try {
                result = post(postUrl, strJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(String result)  {

            if(pd.isShowing()){
                pd.dismiss();
                pd = null;
            }

            parseResultLogin(result);

        }


        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

    }

    private void parseResultLogin(String result) {

        try {
            JSONObject jsonObj = new JSONObject(result);
            App.getInstance().loginObject = jsonObj;
            editor.putString("login_json",jsonObj.toString());
            editor.commit();

            if(jsonObj.getBoolean("success"))
            {


                JSONArray arr = jsonObj.getJSONArray("customer_detail");

                App.getInstance().formToken = jsonObj.getString("formToken");
                App.getInstance().cookieToken = jsonObj.getString("cookieToken");

                App.getInstance().customerMember = arr.getJSONObject(0);
                App.getInstance().selectNews = jsonObj.getJSONArray("select_news");

                // when logoin state valid next load dialy transaction
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();


            }
            else
            {

                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("ลงชื่อเข้าใช้งาน")
                        .setMessage("การลงชื่อเข้าใช้งานไม่สำเร็จ กรุณาตรวจสอบ ชื่อผู้ใช้งาน และรหัสผ่านอีกครั้ง")
                        .setPositiveButton("ปิด",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,  int which) {
                                    }
                                })
                        .show();
            }

        } catch (JSONException e) {
            e.printStackTrace();

            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("ลงชื่อเข้าใช้งาน")
                    .setMessage("การลงชื่อเข้าใช้งานไม่สำเร็จ กรุณาตรวจสอบ ผู้ใช้งาน และรหัสผ่านอีกครั้ง")
                    .setPositiveButton("ปิด",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,  int which) {
                                }
                            })
                    .show();
        }

    }

    /////////////////////////////////////////////////////////

    private void doRegister() {
        new Register().execute();
    }

    private class Register extends AsyncTask<Void, Void, String> {
        String strJson,postUrl;
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
            strJson = "{'mobile':'" + edtPhone.getText().toString()  + "'"
                    + ",'fname':'" + edtFname.getText().toString() + "'"
                    + ",'lname':'" + edtLname.getText().toString() + "'"
                    + ",'password':'" + edtRegistPassword.getText().toString() + "'"
                    + ",'email':'" + edtEmail.getText().toString() + "'"

                    + "}";
            postUrl  = App.getInstance().m_server + "/RegisterCustomerSusco/RegisterMember";
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("กำลังดำเนินการ...");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(Void... urls)   {
            String result = null;
            try {
                result = post(postUrl, strJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // blah blah

            return result;
        }

        protected void onPostExecute(String result)  {

            if(pd.isShowing()){
                pd.dismiss();
                pd = null;
            }

            Log.i("LOGGGGGG",result);
            parseResultRegister(result);

        }


        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

    }

    private void parseResultRegister(String result) {
        if(result == null)
            return ;

        ////////////////////////////////
        try {

            JSONObject jsonObj = new JSONObject(result);
            if(jsonObj.getBoolean("success")) {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("ลงทะเบียนใหม่")
                        .setMessage("ลงทะเบียนเรียบร้อยแล้ว โปรดลงชื่อเข้าใช้งานเพื่อเข้าสู่ระบบ")
                        .setPositiveButton("ปิด",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        RegisterToLoginView();
                                        edtUsername.setText("");
                                        edtPassword.setText("");
                                    }
                                })
                        .show();
            }else{
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("ลงทะเบียนใหม่")
                        .setMessage("ลงทะเบียนไม่สำเร็จ โปรดทำรายการใหม่ภายหลัง\nเนื่องจาก " + jsonObj.getString("msg"))
                        .setPositiveButton("ปิด",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        edtUsername.setText("");
                                        edtPassword.setText("");
                                    }
                                })
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();

            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("ลงทะเบียนใหม่")
                    .setMessage("ลงทะเบียนไม่สำเร็จ โปรดทำรายการใหม่ภายหลัง")
                    .setPositiveButton("ปิด",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,  int which) {

                                    edtUsername.setText("");
                                    edtPassword.setText("");
                                }
                            })
                     .show();
        }
    }
}
