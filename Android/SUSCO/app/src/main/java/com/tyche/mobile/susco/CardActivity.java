package com.tyche.mobile.susco;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

public class CardActivity extends AppCompatActivity {


    private Button btnBack;
    private TextView txvMyName;
    private TextView txvMyNumber;
    private TextView txvMyDateExpire;
    private ImageView imgQr;
    ProgressBar p;
    private RelativeLayout cardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        btnBack = (Button)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardLayout = (RelativeLayout)findViewById(R.id.cardLayout);
        imgQr = (ImageView)findViewById(R.id.imgQr);
        txvMyName = (TextView) findViewById(R.id.txvName);
        txvMyNumber = (TextView) findViewById(R.id.txvCode);
        txvMyDateExpire = (TextView)findViewById(R.id.txvDate);
        p  = (ProgressBar) findViewById(R.id.progressBar);

        cardLayout.setRotation(-90f);

        try {
            txvMyName.setText(App.getInstance().customerMember.getString("fname").replace("\r","").replace("\n","") + " " + App.getInstance().customerMember.getString("lname").replace("\r","").replace("\n",""));
//            txvMyDateExpire.setText(App.getInstance().customerMember.getString("createdate"));

            String m = App.getInstance().customerMember.getString("createdate");
            m  = m.substring(m.length()-1, -7);
            txvMyDateExpire.setText(m);

            txvMyNumber.setText(App.getInstance().customerMember.getString("member_code"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new UpdateCard().execute();
        overrideFonts(CardActivity.this,findViewById(R.id.main_content) );

    }

    private class UpdateCard extends AsyncTask<Void, Void, Bitmap> {


        @Override
        protected void onPreExecute() {
            p.setEnabled(true);
        }

        protected Bitmap doInBackground(Void... urls)   {

            QRCodeWriter writer = new QRCodeWriter();
            Bitmap bmp = null;
            try {
                BitMatrix bitMatrix = null;
                try {
                    bitMatrix = writer.encode(App.getInstance().customerMember.getString("member_code"), BarcodeFormat.QR_CODE, 512, 512);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                 bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }


            } catch (WriterException e) {
                e.printStackTrace();
            }

            return bmp;
        }

        protected void onPostExecute(Bitmap result)  {
            p.setEnabled(false);
            p.setVisibility(View.GONE);
            ((ImageView) findViewById(R.id.imgQr)).setImageBitmap(result);
        }
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
}
