package com.tyche.mobile.susco;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Vinit on 24/5/2560.
 */

public class HomeFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView txvMyName;
    private TextView txvMyNumber;
    private TextView txvMyScore;

    private TextView txtTitle1,txtTitle2,txtTitle3,txtTitle4,txtTitle5,txtTitle6,txtTitle7,txtTitle8;


    LinearLayout lnrPromotion;
    private String m_formToken,m_cookieToken;

    CirclePageIndicator indicator;
    private  ViewPager mPager;
    private  int currentPage = 0;
    private  int NUM_PAGES = 0;
    private   ArrayList<Bitmap> IMAGES= new ArrayList<>();
    private ArrayList<Bitmap> ImagesArray = new ArrayList<>();


    public HomeFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_home, container, false);


        //bannerSlider = (BannerSlider) rootView.findViewById(R.id.banner_slider1);
        //        //add banner using image url
        //       //  bannerSlider.addBanner(new RemoteBanner("Put banner image url here ..."));
        //        //add banner using resource drawable
        //        bannerSlider.addBanner(new DrawableBanner(R.drawable.susco_banner_01));
        //        bannerSlider.addBanner(new DrawableBanner(R.drawable.banner_news_500px_214px_01));
        //        bannerSlider.addBanner(new DrawableBanner(R.drawable.banner_news_500px_214px_02));

        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        indicator = (CirclePageIndicator)
                rootView.findViewById(R.id.indicator);

        lnrPromotion = (LinearLayout)rootView.findViewById(R.id.lnrPromotion);
        txvMyName = (TextView) rootView.findViewById(R.id.txvMyName);
        txvMyNumber = (TextView) rootView.findViewById(R.id.txvMyNumber);
        txvMyScore = (TextView)rootView.findViewById(R.id.txvMyScore);
        txtTitle1 = (TextView)rootView.findViewById(R.id.txtTitle1);
        txtTitle2 = (TextView)rootView.findViewById(R.id.txtTitle2);
        txtTitle3 = (TextView)rootView.findViewById(R.id.txtTitle3);
        txtTitle4 = (TextView)rootView.findViewById(R.id.txtTitle4);
        txtTitle5 = (TextView)rootView.findViewById(R.id.txtTitle5);
        txtTitle6 = (TextView)rootView.findViewById(R.id.txtTitle6);
        txtTitle7 = (TextView)rootView.findViewById(R.id.txtTitle7);
        txtTitle8 = (TextView)rootView.findViewById(R.id.txtTitle8);

        try {

            txvMyName.setText(App.getInstance().customerMember.getString("fname") + " " + App.getInstance().customerMember.getString("lname"));
            txvMyScore.setText(App.getInstance().customerMember.getString("point_summary"));
            txvMyNumber.setText(App.getInstance().customerMember.getString("mobile"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        m_cookieToken = App.getInstance().cookieToken.toString();
        m_formToken = App.getInstance().formToken.toString();
        doPriceOil();
        doBanner();
        doMemberTransection();

        init();

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

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        lnrPromotion.removeAllViews();
        for(int i = 0; i <  App.getInstance().selectNews.length(); i ++){

            try {
                final JSONObject j = App.getInstance().selectNews.getJSONObject(i);

                View giftView = inflater.inflate(R.layout._item_news,null);
                ((TextView)giftView.findViewById(R.id.txtTitle)).setText(j.getString("news_head"));


                //decode base64 string to image
                String imageString = j.getString("pic1_id");
                byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                ((ImageView)giftView.findViewById(R.id.imgPic)).setImageBitmap(decodedImage);
                ((ImageView)giftView.findViewById(R.id.imgPic)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.getInstance().objNews = j;
                        Intent intent = new Intent(getActivity(),NewsDetailActivity.class);
                        startActivity(intent);
                    }
                });
                lnrPromotion.addView(giftView);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    } // end init

    private void doMemberTransection() {

        new MemberTransection().execute();
    }

    private class MemberTransection extends AsyncTask<Void, Void, String> {
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
            postUrl  = App.getInstance().m_server + "/ListTransactionCustomer/GetTransactionByMember";
            pd = new ProgressDialog(getActivity() );
            pd.setMessage("กำลังดำเนินการ...");
            pd.setCancelable(false);
            pd.show();

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

            if(pd.isShowing()){
                pd.dismiss();
                pd = null;
            }

            parseResultMemberTransection(result);

        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

    }

    private void parseResultMemberTransection(String result) {
        if(result == null)
            return ;

        ////////////////////////////////
        try {
            JSONObject jsonObj = new JSONObject(result);
            App.getInstance().loginObject = jsonObj;

            // keep transaction on json array object.
            App.getInstance().transactionDialies = jsonObj.getJSONArray("transaction_daily");
            App.getInstance().redeemTransactions = jsonObj.getJSONArray("get_redeem_request");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////

    private void doPriceOil() {
        new PriceOil().execute();
    }

    private class PriceOil extends AsyncTask<Void, Void, String> {
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
            postUrl  = App.getInstance().m_server + "/Standardprice/Get";
            //            pd = new ProgressDialog(getActivity() );
            //            pd.setMessage("กำลังดำเนินการ...");
            //            pd.setCancelable(false);
            //            pd.show();

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

            //            if(pd.isShowing()){
            //                pd.dismiss();
            //                pd = null;
            //            }

            parseResultPriceOil(result);

        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

    }

    private void parseResultPriceOil(String result) {
        if(result == null)
            return ;

        ////////////////////////////////
        try {
            JSONObject jsonObj = new JSONObject(result);

            JSONArray  jsonArray = jsonObj.getJSONArray("current_price");

            Log.i("JSON",result);
            for(int i =0; i < jsonArray.length(); i++){
                JSONObject item = jsonArray.getJSONObject(i);


                if(item.getString("product_desc").equals("เบนซิน")){
                    txtTitle1.setText(item.getString("set_price") + " บาท/ลิตร");
                }

                if(item.getString("product_desc").equals("แก๊สโซฮอล์ 95")){
                    txtTitle2.setText(item.getString("set_price") + " บาท/ลิตร");

                }

                if(item.getString("product_desc").equals("แก๊สโซฮอล์ 91")){
                    txtTitle3.setText(item.getString("set_price") + " บาท/ลิตร");

                }

                if(item.getString("product_desc").equals("แก๊สโซฮอล์ อี 20")){

                    txtTitle4.setText(item.getString("set_price") + " บาท/ลิตร");
                }

                if(item.getString("product_desc").equals("แก๊สโซฮอล์ อี 85")){

                    txtTitle5.setText(item.getString("set_price") + " บาท/ลิตร");
                }

                if(item.getString("product_desc").equals("ดีเซลหมุนเร็ว")){

                    txtTitle6.setText(item.getString("set_price") + " บาท/ลิตร");
                }

                if(item.getString("product_desc").equals("ก๊าซ LPG")){

                    txtTitle7.setText(item.getString("set_price") + " บาท/ลิตร");
                }

                if(item.getString("product_desc").equals("ก๊าซ NGV")){

                    txtTitle8.setText(item.getString("set_price") + " บาท/กก.");
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////

    private void doBanner() {
        new Banner().execute();
    }

    private class Banner extends AsyncTask<Void, Void, String> {
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
            postUrl  = App.getInstance().m_server + "/Banner/GetBanner";
            //            pd = new ProgressDialog(getActivity() );
            //            pd.setMessage("กำลังดำเนินการ...");
            //            pd.setCancelable(false);
            //            pd.show();

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

            //            if(pd.isShowing()){
            //                pd.dismiss();
            //                pd = null;
            //            }

            parseResultBanner(result);

        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

    }



    private void parseResultBanner(String result) {
        if(result == null)
            return ;

        ////////////////////////////////
        try {
            JSONObject jsonObj = new JSONObject(result);
            App.getInstance().objBanner = jsonObj;
            JSONArray jsonArray = jsonObj.getJSONArray("banner");

            Log.i("JSON Banner",result);
            for(int i =0; i < jsonArray.length(); i++){
                final JSONObject item = jsonArray.getJSONObject(i);

                String imageString = item.getString("image");
                //decode base64 string to image
                byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                // img.setImageBitmap(decodedImage);

                ImagesArray.add(decodedImage);

            } // end for


            mPager.setAdapter(new SlidingImage_Adapter(getActivity(),ImagesArray));



            indicator.setViewPager(mPager);
            final float density = getResources().getDisplayMetrics().density;

            //Set circle indicator radius
            indicator.setRadius(5 * density);

            NUM_PAGES =IMAGES.size();

            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);

            // Pager listener over indicator
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;









                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    //    private void initSlideBanner() {
    //        for(int i=0;i<IMAGES.length;i++)
    //            ImagesArray.add(IMAGES[i]);
    //
    //
    //        mPager.setAdapter(new SlidingImage_Adapter(getActivity(),ImagesArray));
    //
    //
    //
    //        indicator.setViewPager(mPager);
    //
    //        final float density = getResources().getDisplayMetrics().density;
    //
    ////Set circle indicator radius
    //        indicator.setRadius(5 * density);
    //
    //        NUM_PAGES =IMAGES.length;
    //
    //        // Auto start of viewpager
    //        final Handler handler = new Handler();
    //        final Runnable Update = new Runnable() {
    //            public void run() {
    //                if (currentPage == NUM_PAGES) {
    //                    currentPage = 0;
    //                }
    //                mPager.setCurrentItem(currentPage++, true);
    //            }
    //        };
    //        Timer swipeTimer = new Timer();
    //        swipeTimer.schedule(new TimerTask() {
    //            @Override
    //            public void run() {
    //                handler.post(Update);
    //            }
    //        }, 3000, 3000);
    //
    //        // Pager listener over indicator
    //        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    //
    //            @Override
    //            public void onPageSelected(int position) {
    //                currentPage = position;
    //
    //            }
    //
    //            @Override
    //            public void onPageScrolled(int pos, float arg1, int arg2) {
    //
    //            }
    //
    //            @Override
    //            public void onPageScrollStateChanged(int pos) {
    //
    //            }
    //        });
    //
    //    }

}