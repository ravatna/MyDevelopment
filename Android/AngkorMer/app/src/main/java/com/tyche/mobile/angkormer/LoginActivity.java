package com.tyche.mobile.angkormer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    private EditText edtUsername;
    private Button btnLogin;
    private TextView txvVersion;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static final String MY_PREFS = "angkor_mer_tyche";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getApplicationContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initView();
    }

    private void initView() {

        edtUsername = (EditText)findViewById(R.id.edtUsername);
        edtUsername.setText("AK0831356653");

        btnLogin = (Button)findViewById(R.id.btnLogin);
        //btnRegister = (Button)findViewById(R.id.btnRegister);
        txvVersion = (TextView)findViewById(R.id.txvVersion);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = true;
                //@todo : validate user input data.

                if(edtUsername.getText().toString().length() <= 0){
                    edtUsername.setError("Pls, Input ticket code.");
                    b = false;
                }



                if(b) {
                    doLogin();
                }
            }
        });


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

    }

    private void LoginToRegisterView() {

    }


    private void doLogin() {
        // this block use for demo.
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
        // end block

        // @todo un comment this when connection to production.// new Login().execute();
    }

    private class Login extends AsyncTask<Void, Void, String> {
        String strJson,postUrl;
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
            strJson = "{'mobile_customer':'" + edtUsername.getText().toString()  + "','pass_customer':'" + /*edtPassword.getText().toString() + */ "'}";
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
            JSONArray arr = jsonObj.getJSONArray("customer_detail");

            App.getInstance().formToken = jsonObj.getString("formToken");
            App.getInstance().cookieToken = jsonObj.getString("cookieToken");

            App.getInstance().customerMember = arr.getJSONObject(0);
            App.getInstance().selectNews = jsonObj.getJSONArray("select_news");

            // when logoin state valid next load dialy transaction
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /////////////////////////////////////////////////////////

}
