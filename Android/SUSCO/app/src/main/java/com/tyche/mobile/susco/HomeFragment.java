package com.tyche.mobile.susco;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.tyche.mobile.susco.NewsDetailActivity.codeImage;


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

    private TextView txvDateUpdate,txtTitle1,txtTitle2,txtTitle3,txtTitle4,txtTitle5,txtTitle6,txtTitle7,txtTitle8;
    private Button btnPageLeft,btnPageRight;

    LinearLayout lnrPromotion;
    private String m_formToken,m_cookieToken;

    CirclePageIndicator indicator;
    private  ViewPager mPager;
    private  int currentPage = 0;
    private  int NUM_PAGES = 0;

    private ScrollView sclMain;
    private SwipeRefreshLayout swipeContainer;

    static Bitmap[] decodedImage;
    static String [] codeImage;
    static String _member_code;

    ImageFragmentPagerAdapter imageFragmentPagerAdapter;

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

        sclMain = (ScrollView) rootView.findViewById(R.id.sclMain);

        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getChildFragmentManager());
        mPager = (ViewPager) rootView.findViewById(R.id.pager);

        indicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);

        btnPageLeft = (Button)rootView.findViewById(R.id.btnPageLeft);
        btnPageRight = (Button)rootView.findViewById(R.id.btnPageRight);

        txvDateUpdate = (TextView)rootView.findViewById(R.id.txvDateUpdate);
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
            _member_code = App.getInstance().customerMember.getString("member_code");
            txvMyName.setText(App.getInstance().customerMember.getString("fname").replace("\r","").replace("\n","") + " " + App.getInstance().customerMember.getString("lname").replace("\r","").replace("\n",""));
            txvMyScore.setText(App.getInstance().customerMember.getString("point_summary"));
            txvMyNumber.setText(App.getInstance().customerMember.getString("mobile"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        m_cookieToken = App.getInstance().cookieToken.toString();
        m_formToken = App.getInstance().formToken.toString();

//////////////////////////////////////////////////////

        btnPageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int current = mPager.getCurrentItem();

                    if( (current-1) < 0 ){
                        mPager.setCurrentItem(mPager.getAdapter().getCount());
                    }else{
                        mPager.setCurrentItem(mPager.getCurrentItem()-1);
                    }

                }catch (Exception ex){ }
            }
        });

        btnPageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int current = mPager.getCurrentItem() ;
                    if( (current + 1) >= mPager.getAdapter().getCount() ){
                        mPager.setCurrentItem(0);
                    }else{
                        mPager.setCurrentItem(mPager.getCurrentItem()+1);
                    }

                }catch (Exception ex){ }

            }
        });

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

                doPriceOil();
                doBanner();
                doNews();
                doMemberTransection();


            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        /////////////////////////////////////////////////
        doPriceOil();
        doBanner();
        doNews();
        doMemberTransection();



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

    private void initNews() {
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


                GetImageBase64 img64 = new GetImageBase64();
                img64.imgView = ((ImageView)giftView.findViewById(R.id.imgPic));
                img64.imagecode = j.getString("code_image1");
                img64.m_cookieToken = App.getInstance().cookieToken;
                img64.m_formToken = App.getInstance().formToken;
                img64.Width = "1024";
                img64.Height = "400";
                img64._mcode = App.getInstance().customerMember.getString("member_code");
                img64.checkWidth = "0";
                img64.CustomWidthHeight = "1";
                img64.execute();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        overrideFonts(getActivity(),lnrPromotion );
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
            if(App.getInstance().showProgressDialog) {
                pd.show();
            }

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
            // Now we call setRefreshing(false) to signal refresh has finished
            swipeContainer.setRefreshing(false);

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


            App.getInstance().showProgressDialog = false;
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

            Calendar c = Calendar.getInstance();

            Locale lc = new Locale("th","TH");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm",lc);
            SimpleDateFormat tf = new SimpleDateFormat("kk:mm",lc);


            String formattedTime = tf.format(c.getTime());

            int year=0,month=0,day=0,hh=0,mm=0;

                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DATE);



            String d =   day + "/" + (month+1) + "/" + (year+543) ;


            txvDateUpdate.setText(String.format("ณ วันที่ %s %s น.",d ,formattedTime));

            //Log.i("JSON",result);
            for(int i =0; i < jsonArray.length(); i++){
                JSONObject item = jsonArray.getJSONObject(i);

                //txvDateUpdate.setText(item.getString("updatedate").replace("-","/"));

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



    ///////////////////////////////////////////

///////////////// news ///////////////////////
private void doNews() {
    new News().execute();
}
    private class News extends AsyncTask<Void, Void, String> {
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
            postUrl  = App.getInstance().m_server + "/News/getNews";
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
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(result);
                App.getInstance().selectNews = jsonObj.getJSONArray("select_news");

                initNews();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  parseResultNews(result);
        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

    }// .End task news
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
            strJson = "{'membercode':'" + _mcode  + "','image_width':'" + 512 + "','image_height':'" + 256+ "','formToken':'" + m_formToken  + "','cookieToken':'" + m_cookieToken  + "'}";

           //Log.i("XXXX",strJson);
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

    }// .End Banner

    private void parseResultBanner(String result) {
        if(result == null)
            return ;

        ////////////////////////////////
        try {
            JSONObject jsonObj = new JSONObject(result);
            App.getInstance().objBanner = jsonObj;
            JSONArray jsonArray = jsonObj.getJSONArray("banner");


            decodedImage = new Bitmap[jsonArray.length()];
            codeImage = new String[jsonArray.length()];

            for(int i =0; i < jsonArray.length(); i++){
                final JSONObject item = jsonArray.getJSONObject(i);

                String imageString = item.getString("image");
                //decode base64 string to image
                byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                decodedImage[i] = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                codeImage[i] = item.getString("code_image");




            } // end for

            mPager.setAdapter(imageFragmentPagerAdapter);
            indicator.setViewPager(mPager);
            final float density = getResources().getDisplayMetrics().density;

            NUM_PAGES =decodedImage.length;

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
            }, 5000, 5000);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }// .End parseBannerResult



    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return decodedImage.length;
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position);
        }
    }

    public static class SwipeFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
            Bundle bundle = getArguments();
            final int position = bundle.getInt("position");

            imageView.setImageBitmap(decodedImage[position]);


            GetImageBase64_type2 img64 = new GetImageBase64_type2();
            img64.imgView = imageView;
            img64.imagecode = codeImage[position];
            img64.i = position;
            img64.m_cookieToken = App.getInstance().cookieToken;
            img64.m_formToken = App.getInstance().formToken;
            img64.Width = "2048";
            img64.Height = "400";
            img64._mcode = _member_code;
            img64.checkWidth = "0";
            img64.CustomWidthHeight = "1";
            img64.execute();

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = App.getInstance().objBanner.getJSONArray("banner");
                        final JSONObject item = jsonArray.getJSONObject(position);

                        if(!item.getString("banner_url").equals("")) {
                            if(!item.getString("banner_url").contains("http://") && !item.getString("banner_url").contains("https://")) {
                                Uri webpage = Uri.parse("http://" + item.getString("banner_url"));
                                Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                                getActivity().startActivity(myIntent);
                            }else{
                                Uri webpage = Uri.parse(item.getString("banner_url"));
                                Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                                getActivity().startActivity(myIntent);
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            return swipeView;
        }

        static SwipeFragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }
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
            strJson = "{\"member_code\":\"" + _mcode
                    + "\",\"imagecode\":\"" + imagecode
                    + "\",\"Width\":" + Width
                    + ",\"Height\":" + Height
                    + ",\"checkWidth\":" + checkWidth // for fix width ,1 for fix height
                    + ",\"CustomWidthHigth\":" + CustomWidthHeight // for fix width ,1 for fix height
                    + ",\"formToken\":\"" + m_formToken
                    + "\",\"cookieToken\":\"" + m_cookieToken  + "\"}";
            postUrl  = App.getInstance().m_server + "/GetPicture/getimagebase64";
Log.i("xxxxxxxxx",strJson);

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


            if(result == null)
                return ;

            Log.i("xxxxxxxb",result);
            ////////////////////////////////
            try {
                JSONObject jsonObj = new JSONObject(result);

                if(jsonObj.getBoolean("success")){


                    //decode base64 string to image

                    byte[] imageBytes = Base64.decode(jsonObj.getString("imagebase64"), Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imgView.setImageBitmap(decodedImage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

    }


    private static class GetImageBase64_type2 extends AsyncTask<Void, Void, String> {
        int i = 0;
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
            strJson = "{\"member_code\":\"" + _mcode
                    + "\",\"imagecode\":\"" + imagecode
                    + "\",\"Width\":" + Width
                    + ",\"Height\":" + Height
                    + ",\"checkWidth\":" + checkWidth // for fix width ,1 for fix height
                    + ",\"CustomWidthHigth\":" + CustomWidthHeight // for fix width ,1 for fix height
                    + ",\"formToken\":\"" + m_formToken
                    + "\",\"cookieToken\":\"" + m_cookieToken  + "\"}";
            postUrl  = App.getInstance().m_server + "/GetPicture/getimagebase64";
            Log.i("xxxxxxxxx",strJson);

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


            if(result == null)
                return ;

            ////////////////////////////////
            try {
                JSONObject jsonObj = new JSONObject(result);

                if(jsonObj.getBoolean("success")){


                    //decode base64 string to image

                    byte[] imageBytes = Base64.decode(jsonObj.getString("imagebase64"), Base64.DEFAULT);
                    Bitmap di = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imgView.setImageBitmap(di);
                    decodedImage[i] = di;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

    }




}