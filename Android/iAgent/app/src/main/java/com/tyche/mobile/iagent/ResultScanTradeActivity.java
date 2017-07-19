package com.tyche.mobile.iagent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tyche.mobile.iagent.R;

public class ResultScanTradeActivity extends Activity {

    private Button btnAccept;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_scan_trade);

        btnAccept = (Button)findViewById(R.id.btnAccept);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ResultScanTradeActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Are you want to transfer point(s)?")
                        .setNeutralButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        new AlertDialog.Builder(ResultScanTradeActivity.this)
                                .setTitle("Info")
                                .setMessage("Transfer point(s) was sussess.")
                                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog2, int which) {
                                        dialog2.dismiss();
                                        finish();
                                    }
                                }) .show(); // sub dialog yes
                    }
                }) .show();

            }
        });

    }
}
