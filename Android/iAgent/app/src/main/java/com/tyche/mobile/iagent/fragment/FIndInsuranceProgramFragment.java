package com.tyche.mobile.iagent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tyche.mobile.iagent.PremiumDetailActivity;
import com.tyche.mobile.iagent.R;
import com.tyche.mobile.iagent.adapter.InsuranceProgramAdapter;
import com.tyche.mobile.iagent.adapter.InsuranceProgramItem;

import java.util.ArrayList;

/**
 * Created by Vinit on 24/5/2560.
 */

 public class FIndInsuranceProgramFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private InsuranceProgramAdapter mAdapter;
    private ArrayList<InsuranceProgramItem> mList;

    public FIndInsuranceProgramFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FIndInsuranceProgramFragment newInstance(int sectionNumber) {
        FIndInsuranceProgramFragment fragment = new FIndInsuranceProgramFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_guide, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {



        mList = new ArrayList<>();
        mList.add(new InsuranceProgramItem(1,"Member One","Life Insurance Program 1", "Payment period: 2017/08/1"));
        mList.add(new InsuranceProgramItem(2,"Member One","Car Insurance Program 1", "Payment period:  -"));
        mAdapter = new InsuranceProgramAdapter(getActivity(),mList);
        ((ListView)rootView.findViewById(R.id.guide_listview)).setAdapter(mAdapter);
        ((ListView)rootView.findViewById(R.id.guide_listview)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getActivity(), PremiumDetailActivity.class);
                startActivity(intent);


            }
        });

    }

}
