package com.tyche.mobile.imember.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tyche.mobile.imember.App;
import com.tyche.mobile.imember.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Vinit on 24/5/2560.
 */

 public class ScoreFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private LinearLayout lnrGiftBox;
    private LinearLayout lnrVoucher;

    public ScoreFragment() {
    }

    private TextView txvMyName;
    private TextView txvMyNumber;
    private TextView txvMyScore;


    private String m_formToken,m_cookieToken;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ScoreFragment newInstance(int sectionNumber) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_score, container, false);

        lnrGiftBox = (LinearLayout)rootView.findViewById(R.id.lnrGift);
        lnrVoucher = (LinearLayout)rootView.findViewById(R.id.lnrVoucher);
        txvMyName = (TextView) rootView.findViewById(R.id.txvMyName);
        txvMyNumber = (TextView) rootView.findViewById(R.id.txvMyNumber);
        txvMyScore = (TextView)rootView.findViewById(R.id.txvMyScore);

        try {
            txvMyName.setText(App.getInstance().customerMember.getString("fname") + " " + App.getInstance().customerMember.getString("lname"));
            txvMyScore.setText(App.getInstance().customerMember.getString("point_summary"));
            txvMyNumber.setText(App.getInstance().customerMember.getString("mobile"));
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){}

        m_cookieToken = App.getInstance().cookieToken.toString();
        m_formToken = App.getInstance().formToken.toString();
        // doCatalogForMember();

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

    private void doCatalogForMember() {
        new CatalogForMember().execute();
    }

    private class CatalogForMember extends AsyncTask<Void, Void, String> {
        String strJson,postUrl;
        ProgressDialog pd;
        String _mcode = "";
        @Override
        protected void onPreExecute() {
            try {
                _mcode = App.getInstance().customerMember.getString("mobile");
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e){}
            // Create Show ProgressBar
            strJson = "{'mobile':'" + _mcode  + "','formToken':'" + m_formToken  + "','cookieToken':'" + m_cookieToken  + "'}";
            postUrl  = App.getInstance().m_server + "/GetCatalogForMember/catalog_for_member_mobile";
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

            return result;
        }

        protected void onPostExecute(String result)  {

            if(pd.isShowing()){
                pd.dismiss();
                pd = null;
            }

            parseResultGetCatalogForMember(result);

        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("formToken",m_formToken)
                    .addHeader("cookieToken",m_cookieToken)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();

        }

    }

    private void parseResultGetCatalogForMember(String result) {
        if(result == null)
            return ;

        ////////////////////////////////
        try {
            JSONObject jsonObj = new JSONObject(result);
            App.getInstance().loginObject = jsonObj;

            // keep transaction on json array object.
            App.getInstance().giftCatalogs = jsonObj.getJSONArray("catalog");
            init();
            init2();

            overrideFonts(getActivity(),lnrGiftBox );
            overrideFonts(getActivity(),lnrVoucher );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init2() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout blogView = (LinearLayout) inflater.inflate(R.layout._blog_gift,null);
        // lnrVoucher.removeAllViews();

        //////////////////////////////////////////
        // count item
        ArrayList<JSONObject> objObjects = new ArrayList<>();
        for(int i = 0; i <  App.getInstance().giftCatalogs.length(); i ++){

            try {
                JSONObject j = App.getInstance().giftCatalogs.getJSONObject(i);
                if(j.getString("check").equals("1")){
                    continue;
                }else{
                    objObjects.add(j);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //////////////////////////////////////////

        for(int i = 0; i <   objObjects.size() ; i ++){
            if( i%2 == 0){
                lnrVoucher.addView(blogView);
                blogView = (LinearLayout) inflater.inflate(R.layout._blog_gift,null);
            }

            View giftView = inflater.inflate(R.layout._item_gift,null);
            try {
                JSONObject j = objObjects.get(i);
                ((TextView)giftView.findViewById(R.id.txvDesc)).setText(j.getString("redeem_item_desc"));

                //decode base64 string to image
                String imageString = j.getString("picture");
                byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                ((ImageView)giftView.findViewById(R.id.imgPic)).setImageBitmap(decodedImage);
                blogView.addView(giftView);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(i+1==objObjects.size()){
                lnrVoucher.addView(blogView);
                blogView = (LinearLayout) inflater.inflate(R.layout._blog_gift,null);
            }

        }
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout blogView = (LinearLayout) inflater.inflate(R.layout._blog_gift,null);
        // lnrVoucher.removeAllViews();

        //////////////////////////////////////////
        // count item
        ArrayList<JSONObject> objObjects = new ArrayList<>();
        for(int i = 0; i <  App.getInstance().giftCatalogs.length(); i ++){

            try {
                JSONObject j = App.getInstance().giftCatalogs.getJSONObject(i);
                if(j.getString("check").equals("0")){
                    continue;
                }else{
                    objObjects.add(j);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        ////////////////////////////////////////////
        //lnrGiftBox.removeAllViews();
        for(int i = 0; i <   objObjects.size() ; i ++){

            if( i%2 == 0){

                lnrGiftBox.addView(blogView);
                blogView = (LinearLayout) inflater.inflate(R.layout._blog_gift,null);

            }


            try {
                JSONObject j = objObjects.get(i);

                View giftView = inflater.inflate(R.layout._item_gift,null);
                ((TextView)giftView.findViewById(R.id.txvDesc)).setText(j.getString("redeem_item_desc"));


                //decode base64 string to image
                String imageString = j.getString("picture");
                byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                ((ImageView)giftView.findViewById(R.id.imgPic)).setImageBitmap(decodedImage);
                blogView.addView(giftView);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(i+1==objObjects.size()){
                lnrGiftBox.addView(blogView);
                blogView = (LinearLayout) inflater.inflate(R.layout._blog_gift,null);
            }

        }
    }
}