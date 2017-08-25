package com.tyche.mobile.susco;

import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Member;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.tyche.mobile.susco.R.id.btnSuscoOnline;
import static com.tyche.mobile.susco.R.id.lnrContentHistory;

public class MemberHistoryActivity extends AppCompatActivity {


    private Button btnBack,btnSuscoOnline;
    LinearLayout lnrContentHistory,lnrContentHistory2;
    private LocalActivityManager mLocalActivityManager;
    private String m_formToken,m_cookieToken;

    String[] aMonths;
    private TextView txvDataDate;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        m_cookieToken = App.getInstance().cookieToken.toString();
        m_formToken = App.getInstance().formToken.toString();


        setContentView(R.layout.activity_member_history);
        lnrContentHistory = (LinearLayout) findViewById(R.id.lnrContentHistory);
        lnrContentHistory2 = (LinearLayout) findViewById(R.id.lnrContentHistory2);
        btnBack = (Button)findViewById(R.id.btnBack);
        btnSuscoOnline = (Button) findViewById(R.id.btnSuscoOnline);
        btnSuscoOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.susco.co.th/index.php"));
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vi = inflater.inflate(R.layout._custom_tab_widget, null);
        View vi2 = inflater.inflate(R.layout._custom_tab_widget, null);
        final TextView txvTitle= (TextView)vi.findViewById(R.id.tabTitle);
        final TextView txvTitle2= (TextView)vi2.findViewById(R.id.tabTitle);
        overrideFonts(this,txvTitle);
        overrideFonts(this,txvTitle2);

        TabHost tabHost = (TabHost)findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);
        txvTitle.setText("ประวัติสะสมคะแนน");
        txvTitle.setBackgroundColor(Color.parseColor("#FF008080"));

        txvTitle.setTextColor(Color.parseColor("#FFFFFF"));
        txvTitle2.setTextColor(Color.parseColor("#FF008080"));
        txvDataDate = (TextView)findViewById(R.id.txvDataDate);

        final TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1") .setIndicator(vi).setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        txvTitle2.setText("ประวัติแลกคะแนน");
        txvTitle2.setBackgroundColor(Color.parseColor("#FFFFD92E"));
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2") .setIndicator(vi2).setContent(R.id.tab2);
        tabHost.addTab(tabSpec2);

        init();
        labelMonth();


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("tab1"))
                {
                    txvTitle.setTextColor(Color.parseColor("#FFFFFF"));
                    txvTitle2.setTextColor(Color.parseColor("#FF008080"));
                }

                if(tabId.equals("tab2"))
                {
                    txvTitle.setTextColor(Color.parseColor("#FFFFD92E"));
                    txvTitle2.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        overrideFonts(this,findViewById(R.id.contentView) );

        ///////////////////////////////////////////////
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                doMemberTransection();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        /////////////////////////////////////////////////
    }

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
            pd = new ProgressDialog(MemberHistoryActivity.this);
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

        swipeContainer.setRefreshing(false);


        if(result == null)
            return ;

        ////////////////////////////////
        try {
            JSONObject jsonObj = new JSONObject(result);
            App.getInstance().loginObject = jsonObj;

            // keep transaction on json array object.
            App.getInstance().transactionDialies = jsonObj.getJSONArray("transaction_daily");
            App.getInstance().redeemTransactions = jsonObj.getJSONArray("get_redeem_request");
            init(); // initial list item on transaction datatable.
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////

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

    private void labelMonth(){
        Calendar c = Calendar.getInstance();

        Locale lc = new Locale("th","TH");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm",lc);
        SimpleDateFormat tf = new SimpleDateFormat("kk:mm",lc);

        int year=0,month=0;

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);

        aMonths = new String[] {"มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"};

        String d =   "ข้อมูลประจำเดือน " + aMonths[month] + " " + (year+543) ;

        txvDataDate.setText(d);
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(this);

        lnrContentHistory.removeAllViews();
        lnrContentHistory2.removeAllViews();

        for(int i = 0; i <   App.getInstance().transactionDialies.length(); i ++){
            try {
                JSONObject jsonObj = App.getInstance().transactionDialies.getJSONObject(i);
                View giftView = inflater.inflate(R.layout._item_history,null);
                ((TextView)giftView.findViewById(R.id.txvCol1)).setText(jsonObj.getString("service_date"));
                ((TextView)giftView.findViewById(R.id.txvCol2)).setText(jsonObj.getString("branch_code"));
                ((TextView)giftView.findViewById(R.id.txvCol3)).setText(jsonObj.getString("point_earn"));
                if(i%2==0){
                    giftView.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                lnrContentHistory.addView(giftView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for(int i = 0; i <   App.getInstance().redeemTransactions.length(); i ++){

            try {
                JSONObject jsonObj = App.getInstance().redeemTransactions.getJSONObject(i);

                View giftView = inflater.inflate(R.layout._item_history,null);

                ((TextView)giftView.findViewById(R.id.txvCol1)).setText(jsonObj.getString("service_date"));
                ((TextView)giftView.findViewById(R.id.txvCol2)).setText(jsonObj.getString("branch_code") + " " + jsonObj.getString("item_qty") + " ชิ้น " + " " + jsonObj.getString("item_point") + " คะแนน" );
                ((TextView)giftView.findViewById(R.id.txvCol3)).setText( jsonObj.getString("member_point"));

                if(i%2==0){
                    giftView.setBackgroundColor(Color.parseColor("#ffffff"));
                }

                lnrContentHistory2.addView(giftView);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        overrideFonts(this,findViewById(R.id.contentView) );
    }// .End init()

}
