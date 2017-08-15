package com.tyche.mobile.susco;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.tyche.mobile.susco.R.id.btnSuscoOnline;
import static com.tyche.mobile.susco.R.id.lnrContentHistory;

public class MemberHistoryActivity extends AppCompatActivity {


    private Button btnBack,btnSuscoOnline;
    LinearLayout lnrContentHistory,lnrContentHistory2;
    private LocalActivityManager mLocalActivityManager;


    String[] aMonths;
    private TextView txvDataDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

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

        TabHost tabHost = (TabHost)findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);
        txvTitle.setText("ประวัติสะสมแต้ม");
        txvTitle.setBackgroundColor(Color.parseColor("#FF008080"));

        txvTitle.setTextColor(Color.parseColor("#FFFFFF"));
        txvTitle2.setTextColor(Color.parseColor("#FF008080"));
        txvDataDate = (TextView)findViewById(R.id.txvDataDate);


        final TabHost.TabSpec tabSpec = tabHost.newTabSpec("tab1") .setIndicator(vi).setContent(R.id.tab1);
        tabHost.addTab(tabSpec);




        txvTitle2.setText("ประวัติแลกแต้ม");
        txvTitle2.setBackgroundColor(Color.parseColor("#FFFFD92E"));
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2") .setIndicator(vi2).setContent(R.id.tab2);
        tabHost.addTab(tabSpec2);

        init();


        Calendar c = Calendar.getInstance();

        Locale lc = new Locale("th","TH");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm",lc);
        SimpleDateFormat tf = new SimpleDateFormat("kk:mm",lc);

        String formattedDate = df.format(c.getTime());
        String formattedTime = tf.format(c.getTime());

        int year=0,month=0,day=0,hh=0,mm=0;

        // Date date = new Date();

        //c.setTime(date);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
        hh = c.get(Calendar.HOUR);
        mm = c.get(Calendar.MINUTE);

        aMonths = new String[] {"มกราคม","กุมภาพันธ์","มีนาคม","เมษายน","พฤษภาคม","มิถุนายน","กรกฎาคม","สิงหาคม","กันยายน","ตุลาคม","พฤศจิกายน","ธันวาคม"};

        String d =   "ข้อมูลประจำเดือน " + aMonths[month] + " " + (year+543) ;

txvDataDate.setText(d);

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
    }

}
