package com.tyche.mobile.imember;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.tyche.mobile.imember.adapter.InsuranceProgramAdapter;
import com.tyche.mobile.imember.adapter.InsuranceProgramItem;

import java.util.ArrayList;

public class PremiumDetailActivity extends AppCompatActivity {

    private ArrayList<InsuranceProgramItem> mList;
    private InsuranceProgramAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_detail);


initView();
    }


    private void initView() {

        mList = new ArrayList<>();
        mList.add(new InsuranceProgramItem(3,"Mr.Khumpun","Life Insurance Program 1", "Payment period: 2017/08/1"));
        mList.add(new InsuranceProgramItem(2,"Mr.Khumpun","Life Insurance Program 1", "Payment period: 2017/07/1 [Checked]"));
        mList.add(new InsuranceProgramItem(1,"Mr.Khumpun","Life Insurance Program 1", "Payment period: 2017/06/1 [Checked]"));

        mAdapter = new InsuranceProgramAdapter(this,mList);
        ((ListView)findViewById(R.id.reviwe_guide_listview)).setAdapter(mAdapter);



    }

}
