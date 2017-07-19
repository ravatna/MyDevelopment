package com.tyche.mobile.iagent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tyche.mobile.iagent.QrTradeActivity;
import com.tyche.mobile.iagent.R;

/**
 * Created by Vinit on 24/5/2560.
 */

 public class TradeFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private EditText edtPrice;
    Button btnGenQr;
    private ImageView imgDel;

    public TradeFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TradeFragment newInstance(int sectionNumber) {
        TradeFragment fragment = new TradeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_trade, container, false);
        btnGenQr = (Button) rootView.findViewById(R.id.btnGenerateQr);
        edtPrice = (EditText) rootView.findViewById(R.id.edtPrice);
        imgDel = (ImageView)rootView.findViewById(R.id.imgDelNumber);


        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPrice.setText("0");
            }
        });

        btnGenQr.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(),QrTradeActivity.class);
        intent.putExtra("put_price",edtPrice.getText().toString());
        startActivity(intent);
    }
});
        return rootView;
    }

}
