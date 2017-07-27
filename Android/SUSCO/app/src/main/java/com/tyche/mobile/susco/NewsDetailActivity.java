package com.tyche.mobile.susco;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.TextView;

import org.json.JSONException;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;

import static com.tyche.mobile.susco.R.id.btnPageLeft;
import static com.tyche.mobile.susco.R.id.btnPageRight;
import static com.tyche.mobile.susco.R.id.imgPic;
import static com.tyche.mobile.susco.R.id.imgProfile;

public class NewsDetailActivity extends AppCompatActivity {


    private Button btnBack,btnPageLeft,btnPageRight;
    private ImageView imgPicture1,imgPicture2,imgPicture3;
    private byte[] imageBytes;
    static public Bitmap[] decodedImage;

    static  int NUM_ITEMS = 0;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    public static final String[] IMAGE_NAME = {"eagle", "horse", "bonobo", "wolf", "owl", "bear",};
    String imgB1 = "";
    String imgB2 = "";
    String imgB3 = "";

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

        TextView txvTitle = (TextView)findViewById(R.id.txtTitle);
        TextView txvNewsDate = (TextView)findViewById(R.id.txtNewsDate);
        WebView txvDesc = (WebView)findViewById(R.id.txtDesc);
        imgPicture1 = (ImageView)findViewById(R.id.imgBanner1);
        imgPicture2 = (ImageView)findViewById(R.id.imgBanner2);
        imgPicture3 = (ImageView)findViewById(R.id.imgBanner3);

        //////////////////////////////////////////////////////

        btnPageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                }catch (Exception ex){

                }
            }
        });

        btnPageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                }catch (Exception ex){

                }
            }
        });

        try {
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
            txvDesc.loadData(App.getInstance().objNews.getString("news_text"), "text/html; charset=utf-8",null);

        } catch (JSONException e) {
            e.printStackTrace();
            finish();
        }


        decodedImage = new Bitmap[NUM_ITEMS];



    try {
        if(!App.getInstance().objNews.getString("pic1_id").equals("")) {
        //Log.i("JSON News", App.getInstance().objNews.toString());


        imgB1 = App.getInstance().objNews.getString("pic1_id");
        //decode base64 string to image
        imageBytes = Base64.decode(imgB1, Base64.DEFAULT);

        decodedImage[0] = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        //imgPicture1.setImageBitmap(decodedImage);
        //imgPicture1.setVisibility(View.VISIBLE);

        //NUM_ITEMS++;

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
                //imgPicture2.setImageBitmap(decodedImage);
                //imgPicture2.setVisibility(View.VISIBLE);
                //NUM_ITEMS++;
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
                //imgPicture3.setImageBitmap(decodedImage);
                //imgPicture3.setVisibility(View.VISIBLE);
                //NUM_ITEMS++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);

        overrideFonts(this,findViewById(R.id.main_content) );

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
//            String imageFileName = IMAGE_NAME[position];
//            int imgResId = getResources().getIdentifier(imageFileName, "drawable", "com.javapapers.android.swipeimageslider");
//            imageView.setImageResource(imgResId);


            imageView.setImageBitmap(decodedImage[position]);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog settingsDialog = new Dialog(getActivity());
                    settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    settingsDialog.setContentView(getActivity().getLayoutInflater().inflate(R.layout.dialog_popup_image, null));

                    ((Button)settingsDialog.findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            settingsDialog.dismiss();
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

}
