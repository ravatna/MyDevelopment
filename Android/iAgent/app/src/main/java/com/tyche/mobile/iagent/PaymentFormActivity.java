package com.tyche.mobile.iagent;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

public class PaymentFormActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_form);

        Button btnBookingGuide = (Button)findViewById(R.id.btn_submit_payment);

        btnBookingGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@todo: add confirm dialog.
                new AlertDialog.Builder(PaymentFormActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Are you to conform this transaction?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

//                                        ////////////////////////////////////////////////
//                                        new AlertDialog.Builder(PaymentFormActivity.this)
//                                                .setTitle("Info")
//                                                .setMessage("Service was accept your request.")
//                                                .setPositiveButton("Ok",
//                                                        new DialogInterface.OnClickListener() {
//
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog2,
//                                                                                int which) {
//                                                                dialog2.dismiss();
//
//
//
//
//                                                            }
//                                                        }) .show();
//                                        ////////////////////////////////////////////////



                                        Intent intent = new Intent(PaymentFormActivity.this, PeymentConfirmActivity.class);
                                        startActivity(intent);

                                        
                                    }
                                })
                        .setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                    }
                                }).show();
            }
        });

    }
}
