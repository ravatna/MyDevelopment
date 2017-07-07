package com.tyche.mobile.angkortour;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Vinit on 24/5/2560.
 */

 public class UserInfoFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static int SELECT_PHOTO = -1;
    private TextView txvMyName;
    private TextView txvPointNo;
    private byte[] imageBytes;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String _password="",_email="",_cid_card="",_imagebase64="";

    private String m_formToken,m_cookieToken;
    private static final String MY_PREFS = "angkor_tyche";
    private String _mobile = "";
    private Button btnGuide;

    public UserInfoFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static UserInfoFragment newInstance(int sectionNumber) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_user, container, false);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        m_cookieToken = App.getInstance().cookieToken.toString();
        m_formToken = App.getInstance().formToken.toString();

        txvMyName = (TextView) rootView.findViewById(R.id.txvMyName);
        txvPointNo = (TextView) rootView.findViewById(R.id.txvPointNo);
        btnGuide = (Button)rootView.findViewById(R.id.btnGuide);
        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        try {

            txvMyName.setText(App.getInstance().customerMember.getString("fname") + " " + App.getInstance().customerMember.getString("lname"));
             txvPointNo.setText("10,000 Point(s)");

        } catch (JSONException e) {
            e.printStackTrace();
        }catch(Exception e){}



        TextView txvLogout = (TextView)rootView.findViewById(R.id.txvLogout);
        txvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //@todo: add confirm dialog.
                new AlertDialog.Builder(getActivity())
                        .setTitle("ลงชื่อออก")
                        .setMessage("ต้องการลงชื่อออกจากระบบใช่หรือไม่?")
                        .setPositiveButton("ใช่",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        editor.putString("login_json","");
                                        editor.commit();

                                        getActivity().finish();
                                        getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));
                                    }
                                })
                        .setNegativeButton(
                                "ไม่",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                }).show();


            }
        });



        overrideFonts(getActivity(),rootView );
        return rootView;
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





    /////////////////////////////////////////////////////////
    private void doUpdateInfo() {
        new  UpdateInfo().execute();
    }

    private class UpdateInfo extends AsyncTask<Void, Void, String> {
        String strJson,postUrl;
        ProgressDialog pd;
        String _mcode = "";
        @Override
        protected void onPreExecute() {

            try {
                _mcode = App.getInstance().customerMember.getString("member_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Create Show ProgressBar
            strJson = "{'member_code':'" + _mcode  + "'"

                    + ",'password':'" + _password + "'"
                    + ",'email':'" + _email + "'"
                    + ",'cid_card':'" + _cid_card + "'"
                    + ",'imagebase64':'" + _imagebase64 + "'"

                    + ",'formToken':'" + m_formToken + "'"
                    + ",'cookieToken':'" + m_cookieToken
                    + "'}";
            postUrl  = App.getInstance().m_server + "/UpdateDetailCustomer/UpdateDetail";
            pd = new ProgressDialog(getActivity());
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

            parseResultUpdateInfo(result);

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

    private void parseResultUpdateInfo(String result) {
        if(result == null)
            return ;

        ////////////////////////////////
        _cid_card = "";
        _mobile = "";
        _email = "";
        _imagebase64 = "";
        _password = "";

        try {
            JSONObject jsonObj = new JSONObject(result);
            if(jsonObj.getString("success").equals("true")) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("ปรับปรุงข้อมูล")
                        .setMessage("ปรับปรุงข้อมูลเรียบร้อยแล้ว\n กรุณาออกจากระบบและเข้าสู่ระบบใหม่อีกครั้ง")
                        .setPositiveButton("ปิด",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .show();
            }else{
                new AlertDialog.Builder(getActivity())
                        .setTitle("ปรับปรุงข้อมูล")
                        .setMessage("ไม่สามารถแก้ไขข้อมูลขณะนี้ โปรดทำรายการใหม่ภายหลัง")
                        .setPositiveButton("ปิด",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();

            new AlertDialog.Builder(getActivity())
                    .setTitle("ปรับปรุงข้อมูล")
                    .setMessage("ไม่สามารถแก้ไขข้อมูลขณะนี้ โปรดทำรายการใหม่ภายหลัง")
                    .setPositiveButton("ปิด",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            })
                    .show();
        }
    }

}
