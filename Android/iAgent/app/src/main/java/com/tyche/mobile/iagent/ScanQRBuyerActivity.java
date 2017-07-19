package com.tyche.mobile.iagent;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRBuyerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ScanQRBuyerActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ScanQRBuyerActivity.this,
                    Manifest.permission.CAMERA)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(ScanQRBuyerActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        1000);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        mScannerView = new ZXingScannerView(this);
        ArrayList<BarcodeFormat> lb = new ArrayList<>();
        lb.add(BarcodeFormat.CODE_128);
        lb.add(BarcodeFormat.CODE_39);
        lb.add(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(lb);
        mScannerView.setResultHandler(this);
        setContentView(mScannerView);
        mScannerView.startCamera(0);

    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

//    @Override
//    public void handleResult(Result rawResult) {
//        Toast.makeText(this, "Contents = " + rawResult.getContents() +
//                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
//        // Note:
//        // * Wait 2 seconds to resume the preview.
//        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
//        // * I don't know why this is the case but I don't have the time to figure out.
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mScannerView.resumeCameraPreview(ScanQRBuyerActivity.this);
//            }
//        }, 2000);
//    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        Toast.makeText(this, "Contents = " + result.getText() +
                ", Format = " + result.getBarcodeFormat().name(), Toast.LENGTH_SHORT).show();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanQRBuyerActivity.this);
            }
        }, 2000);
    }
}
