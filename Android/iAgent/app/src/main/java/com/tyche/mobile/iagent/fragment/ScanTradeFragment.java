package com.tyche.mobile.iagent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tyche.mobile.iagent.R;
import com.tyche.mobile.iagent.ScanQRBuyerActivity;

/**
 * Created by Vinit on 24/5/2560.
 */

 public class ScanTradeFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    Button btnScanQr;


    public ScanTradeFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ScanTradeFragment newInstance(int sectionNumber) {
        ScanTradeFragment fragment = new ScanTradeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_scan_trade, container, false);
        btnScanQr = (Button) rootView.findViewById(R.id.btnScanQr);

        btnScanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScanQRBuyerActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


}
