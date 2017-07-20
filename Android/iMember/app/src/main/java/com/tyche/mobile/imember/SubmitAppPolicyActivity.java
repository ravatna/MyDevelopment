package com.tyche.mobile.imember;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SubmitAppPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_app_policy);


        Button btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SubmitAppPolicyActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Are you want to register insurance program ?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
////////////////////////////////////////////////
                                        new AlertDialog.Builder(SubmitAppPolicyActivity.this)
                                                .setTitle("Info")
                                                .setMessage("Service was accept your request.")
                                                .setPositiveButton("Ok",
                                                        new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface dialog2,
                                                                                int which) {
                                                                dialog2.dismiss();

                                                                SubmitAppPolicyActivity.this.finish();
                                                            }
                                                        }) .show();
                                        ////////////////////////////////////////////////


                                    }
                                })
                        .setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {


                                    }

                                }).show();
            }
        });
    }

}
