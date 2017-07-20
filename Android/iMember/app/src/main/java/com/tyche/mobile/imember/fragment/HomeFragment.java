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

import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.views.BannerSlider;

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
    LinearLayout lnrPromotion;
    private String m_formToken,m_cookieToken;

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


        BannerSlider bannerSlider = (BannerSlider) rootView.findViewById(R.id.banner_slider1);
        //add banner using image url
       //  bannerSlider.addBanner(new RemoteBanner("Put banner image url here ..."));
        //add banner using resource drawable
        bannerSlider.addBanner(new DrawableBanner(R.drawable.susco_banner_01));
        bannerSlider.addBanner(new DrawableBanner(R.drawable.banner_news_500px_214px_01));
        bannerSlider.addBanner(new DrawableBanner(R.drawable.banner_news_500px_214px_02));

        lnrPromotion = (LinearLayout)rootView.findViewById(R.id.lnrPromotion);
        txvMyName = (TextView) rootView.findViewById(R.id.txvMyName);
        txvMyNumber = (TextView) rootView.findViewById(R.id.txvMyNumber);
        txvMyScore = (TextView)rootView.findViewById(R.id.txvMyScore);

        try {
            txvMyName.setText(App.getInstance().customerMember.getString("fname") + " " + App.getInstance().customerMember.getString("lname"));
            txvMyScore.setText(App.getInstance().customerMember.getString("point_summary"));
            txvMyNumber.setText(App.getInstance().customerMember.getString("mobile"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        m_cookieToken = App.getInstance().cookieToken.toString();
        m_formToken = App.getInstance().formToken.toString();
        // doMemberTransection();

        //init();

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
//                        Intent intent = new Intent(getActivity(),NewsDetailActivity.class);
//                        startActivity(intent);
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

}