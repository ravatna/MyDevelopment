package com.tyche.mobile.susco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private TextView txvGiftEmpty;
    private TextView txvDiscountEmpty;

    private static final String MY_PREFS = "susco_tyche";
    private SwipeRefreshLayout swipeContainer;
    private String m_formToken,m_cookieToken;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
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
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        lnrGiftBox = (LinearLayout)rootView.findViewById(R.id.lnrGift);
        lnrVoucher = (LinearLayout)rootView.findViewById(R.id.lnrVoucher);
        txvMyName = (TextView) rootView.findViewById(R.id.txvMyName);
        txvMyNumber = (TextView) rootView.findViewById(R.id.txvMyNumber);
        txvMyScore = (TextView)rootView.findViewById(R.id.txvMyScore);
        txvDiscountEmpty = (TextView)rootView.findViewById(R.id.txvDiscountEmpty);
        txvGiftEmpty = (TextView)rootView.findViewById(R.id.txvGiftEmpty);

        try {
            txvMyName.setText(App.getInstance().customerMember.getString("fname").replace("\r","").replace("\n","").replace("null","") + " " + App.getInstance().customerMember.getString("lname").replace("null","").replace("\r","").replace("\n",""));
            txvMyScore.setText(App.getInstance().customerMember.getString("point_summary"));
            txvMyNumber.setText(App.getInstance().customerMember.getString("mobile"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        m_cookieToken = App.getInstance().cookieToken.toString();
        m_formToken = App.getInstance().formToken.toString();


        // check internet connection.
        if( isCanOnline()){
             doCatalogForMember();
        }

        //////////////////////////////////////////////////////

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);

                // check internet connection.
               // if( isCanOnline()){
                doScoreMember();
                doCatalogForMember();
               // }

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        /////////////////////////////////////////////////

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
    public  boolean isCanOnline() {
        ConnectivityManager
                cm = (ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    } // .End isCanOnline

    private void doScoreMember() {
        new ScoreForMember().execute();
    }

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
            }
            // Create Show ProgressBar
            strJson = "{\"mobile\":\"" + _mcode  + "\",\"formToken\":\"" + m_formToken  + "\",\"cookieToken\":\"" + m_cookieToken  + "\"}";
            Log.i("GGGGGG",strJson);
            postUrl  = App.getInstance().m_server + "/GetCatalogForMember/catalog_for_member_mobile";
            pd = new ProgressDialog(getActivity());
            pd.setMessage("กำลังดำเนินการ...");
            pd.setCancelable(false);
          //  pd.show();

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

        String post(String url, String json) throws IOException,OutOfMemoryError {
            RequestBody body = RequestBody.create(JSON, json);
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("formToken", m_formToken)
                        .addHeader("cookieToken", m_cookieToken)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch ( Exception e){
                return "{}";
            }
        }
    }

    private void parseResultGetCatalogForMember(String result) {
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);

        if(result == null)
            return ;

        ////////////////////////////////
        Log.i("JSON Score ", result);
        try {
            JSONObject jsonObj = new JSONObject(result);


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

    ////////////////////////////////////////

    private class ScoreForMember extends AsyncTask<Void, Void, String> {
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
            strJson = "{'membercode':'" + _mcode  + "','formToken':'" + m_formToken  + "','cookieToken':'" + m_cookieToken  + "'}";
            postUrl  = App.getInstance().m_server + "/RefreshPoint/Member";
            pd = new ProgressDialog(getActivity());
            pd.setMessage("กำลังดำเนินการ...");
            pd.setCancelable(false);
            //  pd.show();

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

            parseResultScoreForMember(result);

        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException,OutOfMemoryError {
            RequestBody body = RequestBody.create(JSON, json);
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("formToken", m_formToken)
                        .addHeader("cookieToken", m_cookieToken)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch ( Exception e){
                return "{}";
            }
        }
    }

    private void parseResultScoreForMember(String result) {
        // Now we call setRefreshing(false) to signal refresh has finished


        if(result == null)
            return ;

        ////////////////////////////////
        Log.i("JSON Score ", result);
        try {
            JSONObject jsonObj = new JSONObject(result);
            //App.getInstance().loginObject = jsonObj;

            // keep transaction on json array object.
            // Log.i("XXX",jsonObj.getString("point_member"));
            txvMyScore.setText(jsonObj.getString("point_member"));


            overrideFonts(getActivity(),lnrGiftBox );
            overrideFonts(getActivity(),lnrVoucher );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void init2() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout blogView = (LinearLayout) inflater.inflate(R.layout._blog_gift,null);


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
        lnrVoucher.removeAllViews();
        txvDiscountEmpty.setVisibility(View.VISIBLE);

        for(int i = 0; i <   objObjects.size() ; i ++){
            txvDiscountEmpty.setVisibility(View.INVISIBLE);
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
        lnrGiftBox.removeAllViews();
        txvGiftEmpty.setVisibility(View.VISIBLE);

        for(int i = 0; i <   objObjects.size() ; i ++){
            txvGiftEmpty.setVisibility(View.INVISIBLE);
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Toast.makeText(getActivity(),"ScoreFrament",Toast.LENGTH_LONG).show();
    }





}