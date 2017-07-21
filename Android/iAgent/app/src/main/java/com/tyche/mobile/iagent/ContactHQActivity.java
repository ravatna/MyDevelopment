package com.tyche.mobile.iagent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class ContactHQActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_hq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Button btnEmail = (Button)findViewById(R.id.btnEmail);
        Button btnTelHQ = (Button)findViewById(R.id.btnCallTel);
        Button btnHotline = (Button)findViewById(R.id.btnCallHotLine);



        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","email@email.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contact from member code");
                intent.putExtra(Intent.EXTRA_TEXT, "Put your message here...");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });

        btnTelHQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "021226101"));
                if (ActivityCompat.checkSelfPermission(ContactHQActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ContactHQActivity.this.requestPermissions(new String [] { Manifest.permission.CALL_PHONE },1001);
                    }

                    return;
                }
                startActivity(intent);
            }
        });

        btnHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1688" ));
                if (ActivityCompat.checkSelfPermission(ContactHQActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ContactHQActivity.this.requestPermissions(new String [] { Manifest.permission.CALL_PHONE },1000);
                    }

                    return;
                }
                startActivity(intent);
            }
        });

    }

}
