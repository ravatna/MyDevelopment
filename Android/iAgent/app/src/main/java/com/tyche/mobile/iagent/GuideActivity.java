package com.tyche.mobile.iagent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tyche.mobile.iagent.adapter.GuideAdapter;
import com.tyche.mobile.iagent.adapter.GuideItem;

import java.util.ArrayList;

public class GuideActivity extends Activity {

    private GuideAdapter mAdapter;
    private ArrayList<GuideItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {

        mList = new ArrayList<>();
        mList.add(new GuideItem(1,"Name 1","Robert Lotto","10",R.drawable.g1));
        mList.add(new GuideItem(1,"Name 2","Lee Zun","9",R.drawable.g3));
        mAdapter = new GuideAdapter(this,mList);
        ((ListView)findViewById(R.id.guide_listview)).setAdapter(mAdapter);
        ((ListView)findViewById(R.id.guide_listview)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GuideActivity.this, GuideDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
