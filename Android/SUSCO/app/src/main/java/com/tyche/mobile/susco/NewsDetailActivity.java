package com.tyche.mobile.susco;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class NewsDetailActivity extends AppCompatActivity {


    private Button btnBack,btnPageLeft,btnPageRight;
    private ImageView imgPicture1,imgPicture2,imgPicture3;
    private byte[] imageBytes;
    static public Bitmap[] decodedImage;
    static public String[] codeImage;
    private  int currentPage = 0;
    private String tmpImg1,tmpImg2,tmpImg3;
    private  int NUM_ITEMS = 0;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    public static final String[] IMAGE_NAME = {"eagle", "horse", "bonobo", "wolf", "owl", "bear",};
    String imgB1 = "";
    String imgB2 = "";
    String imgB3 = "";
    static ScrollView scrMain;
    static String mcode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        NUM_ITEMS = 0;

        btnBack = (Button)findViewById(R.id.btnBack);
        btnPageLeft = (Button)findViewById(R.id.btnPageLeft);
        btnPageRight = (Button)findViewById(R.id.btnPageRight);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

         scrMain = (ScrollView)findViewById(R.id.scrMain);
        TextView txvTitle = (TextView)findViewById(R.id.txtTitle);
        TextView txvNewsDate = (TextView)findViewById(R.id.txtNewsDate);
        WebView txvDesc = (WebView)findViewById(R.id.txtDesc);

        txvDesc.getSettings().setJavaScriptEnabled(true);
        txvDesc.getSettings().setGeolocationEnabled(true);
        txvDesc.getSettings().setAllowContentAccess(true);

        imgPicture1 = (ImageView)findViewById(R.id.imgBanner1);
        imgPicture2 = (ImageView)findViewById(R.id.imgBanner2);
        imgPicture3 = (ImageView)findViewById(R.id.imgBanner3);

        //////////////////////////////////////////////////////

        btnPageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int current = viewPager.getCurrentItem();

                    if( (current-1) < 0 ){
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount());
                    }else{
                        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                    }

                }catch (Exception ex){ }
            }
        });

        btnPageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int current = viewPager.getCurrentItem() ;
                    if( (current + 1) >= viewPager.getAdapter().getCount() ){
                        viewPager.setCurrentItem(0);
                    }else{
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }

                }catch (Exception ex){ }
            }
        });

        try {
            mcode = App.getInstance().customerMember.getString("member_code");
            if(App.getInstance().objNews.getString("pic1_id").length() > 0){
                NUM_ITEMS++;
            }

            if(App.getInstance().objNews.getString("pic2_id").length() > 0){
                NUM_ITEMS++;
            }

            if(App.getInstance().objNews.getString("pic3_id").length() > 0){
                NUM_ITEMS++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////

        try {

            txvTitle.setText(App.getInstance().objNews.getString("news_head"));// not yet to use title.
            txvNewsDate.setText(App.getInstance().objNews.getString("news_date"));

            WebSettings settings = txvDesc.getSettings();
            settings.setDefaultTextEncodingName("utf-8");

             String styleSheet =  "\n<style>\n" +
                    "\n" +
"@font-face {\n" +
                     "    font-family: MyFont;\n" +
                     "    src: url(\"fonts/Kanit-Regular.ttf\")\n" +
                     "}" +
                    "body {\n" +
                    "    font-family: MyFont;\n" +
                    "\n" +
                    "}\n" +
                    " </style>";

            txvDesc.loadDataWithBaseURL("file:///android_asset/",
                    styleSheet + App.getInstance().objNews.getString("news_text"), "text/html", "UTF-8", null);

//            txvDesc.loadDataWithBaseURL(
//                    "file:///android_asset/"
//                    ,styleSheet + App.getInstance().objNews.getString("news_text")
//                    ,"text/html; charset=utf-8"
//                    ,null
//            );


        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }

        decodedImage = new Bitmap[NUM_ITEMS];
        codeImage = new String[NUM_ITEMS];




    try {
        if(!App.getInstance().objNews.getString("pic1_id").equals("")) {
            imgB1 = App.getInstance().objNews.getString("pic1_id");

            //decode base64 string to image
            imageBytes = Base64.decode(imgB1, Base64.DEFAULT);

            decodedImage[0] = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            codeImage[0] = App.getInstance().objNews.getString("code_image1");

        }
    } catch (JSONException e) {
        e.printStackTrace();
    }





        try {
            if(!App.getInstance().objNews.getString("pic2_id").equals("")) {
                imgB2 = App.getInstance().objNews.getString("pic2_id");

                //decode base64 string to image
                imageBytes = Base64.decode(imgB2, Base64.DEFAULT);
                decodedImage[1] = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                codeImage[1] = App.getInstance().objNews.getString("code_image2");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(!App.getInstance().objNews.getString("pic3_id").equals("")) {
                imgB3 = App.getInstance().objNews.getString("pic3_id");
                //decode base64 string to image
                imageBytes = Base64.decode(imgB3, Base64.DEFAULT);
                decodedImage[2] = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                codeImage[2] = App.getInstance().objNews.getString("code_image3");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);

        overrideFonts(this,findViewById(R.id.main_content) );

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {

                if (currentPage == NUM_ITEMS) {
                    currentPage = 0;
                }

                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);

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
            GetImageBase64 img64_1 = new GetImageBase64();
            img64_1.imgView = imageView;
            img64_1.i = position;
            img64_1.imagecode = codeImage[position];
            img64_1.m_cookieToken = App.getInstance().cookieToken;
            img64_1.m_formToken = App.getInstance().formToken;
            img64_1.Width = "1024";
            img64_1.Height = "400";
            img64_1._mcode = mcode;
            img64_1.checkWidth = "0";
            img64_1.CustomWidthHeight = "1";
            img64_1.execute();

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog settingsDialog = new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                    settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    settingsDialog.setContentView(getActivity().getLayoutInflater().inflate(R.layout.dialog_popup_image, null));

                    ((Button)settingsDialog.findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            settingsDialog.dismiss();
                            scrMain.scrollTo(0,0);

                        }
                    });

                    ImageView x = (ImageView)settingsDialog.findViewById(R.id.imageView);

                    x.setImageBitmap(decodedImage[position]);

                    settingsDialog.setCancelable(true);
                    settingsDialog.show();
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
    private static class GetImageBase64 extends AsyncTask<Void, Void, String> {
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

            Log.i("xxxxxxxb",result);
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
