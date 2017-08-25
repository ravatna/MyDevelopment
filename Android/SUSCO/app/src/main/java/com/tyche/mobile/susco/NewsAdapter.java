package com.tyche.mobile.susco;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by rewat on 26/5/2560.
 */

public class NewsAdapter extends BaseAdapter {
    private Context ctx;
    private static LayoutInflater inflater=null;
    public ImageView imageLoader;


    public NewsAdapter(Context _ctx ) {
        ctx = _ctx;
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return App.getInstance().selectNews.length();
    }

    public JSONObject getItem(int position) {
        try {
            return App.getInstance().selectNews.getJSONObject(position);
        } catch (JSONException e) {
            return new JSONObject();
        }
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout._item_news, null);

        ImageView thumb_image=(ImageView)vi.findViewById(R.id.imgPic); // thumb image
        TextView txvTitle = (TextView)vi.findViewById(R.id.txtTitle);

        // Setting all values in listview
        JSONObject newsObj = new JSONObject();
        try {
            newsObj  = App.getInstance().selectNews.getJSONObject(position);
            txvTitle.setText(newsObj.getString("news_head"));
            String imageString = newsObj.getString("pic1_id");

            //decode base64 string to image
            byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            thumb_image.setImageBitmap(decodedImage);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vi;
    }



    private class GetImageBase64 extends AsyncTask<Void, Void, String> {
        ImageView imgView = null;
        String strJson,postUrl;
        ProgressDialog pd;
        String m_formToken = "";
        String m_cookieToken = "";
        String _mcode = "";
        String imagecode = "";
        String Width = "";
        String Height = "";
        String checkWidth = "0";
        String CustomWidthHeight = "0"; // 0 get by set width height, 1 get by checkWidth;


        @Override
        protected void onPreExecute() {
            try {
                _mcode = App.getInstance().customerMember.getString("member_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Create Show ProgressBar
            strJson = "{'member_code':'" + _mcode
                    + "','imagecode':'" + imagecode
                    + "','Width':'" + Width
                    + "','Height':'" + Height
                    + "','checkWidth':'" + checkWidth // for fix width ,1 for fix height
                    + "','CustomWidthHeight':'" + CustomWidthHeight // for fix width ,1 for fix height
                    + "','formToken':'" + m_formToken
                    + "','cookieToken':'" + m_cookieToken  + "'}";
            postUrl  = App.getInstance().m_server + "/GetPicture/getimagebase64";


        }

        protected String doInBackground(Void... urls)   {

            String result = null;
            try {

                /////////////////////////////
                RequestBody body = RequestBody.create(JSON, strJson);
                Request request = new Request.Builder()
                        .url(postUrl)
                        .addHeader("formToken",m_formToken)
                        .addHeader("cookieToken",m_cookieToken)
                        .post(body)
                        .build();


                Response response = client.newCall(request).execute();
                result = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(String result)  {



            parseResultGetImage64(result);

        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

    }

    private void parseResultGetImage64(String result) {
        if(result == null)
            return ;

        ////////////////////////////////
        try {
            JSONObject jsonObj = new JSONObject(result);

            if(jsonObj.getBoolean("success")){

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
