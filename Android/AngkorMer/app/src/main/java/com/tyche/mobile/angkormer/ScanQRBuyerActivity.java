package com.tyche.mobile.angkormer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import android.widget.Toast;

import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRBuyerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);
        ArrayList<BarcodeFormat> lb = new ArrayList<>();
        lb.add(BarcodeFormat.CODE_128);
        lb.add(BarcodeFormat.CODE_39);
        lb.add(BarcodeFormat.QR_CODE);
        mScannerView.setFormats(lb);
        mScannerView.setResultHandler(this);
        setContentView(mScannerView);

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
