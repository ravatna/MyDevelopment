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
import com.tyche.mobile.iagent.SubmitAppPolicyActivity;

/**
 * Created by Vinit on 24/5/2560.
 */

 public class MainProgramFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private EditText edtPrice;
    Button btnGenQr;
    private ImageView imgDel;

    public MainProgramFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainProgramFragment newInstance(int sectionNumber) {
        MainProgramFragment fragment = new MainProgramFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_program, container, false);



        Button btnLifeProgram = (Button)rootView.findViewById(R.id.btn_life_program);
        Button btnCarProgram = (Button)rootView.findViewById(R.id.btn_car_program);
        Button btnHealthProgram = (Button)rootView.findViewById(R.id.btn_health_program);

        btnLifeProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainProgramFragment.this.getActivity(), SubmitAppPolicyActivity.class);
                startActivity(intent);
            }
        });

        btnCarProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainProgramFragment.this.getActivity(), SubmitAppPolicyActivity.class);
                startActivity(intent);
            }
        });

        btnHealthProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainProgramFragment.this.getActivity(), SubmitAppPolicyActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
