package com.tyche.mobile.susco;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
}
