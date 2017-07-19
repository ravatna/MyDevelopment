package com.tyche.mobile.iagent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tyche.mobile.iagent.adapter.GuideAdapter;
import com.tyche.mobile.iagent.GuideDetailActivity;
import com.tyche.mobile.iagent.R;
import com.tyche.mobile.iagent.adapter.GuideItem;

import java.util.ArrayList;

/**
 * Created by Vinit on 24/5/2560.
 */

 public class GuideFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private GuideAdapter mAdapter;
    private ArrayList<GuideItem> mList;

    public GuideFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GuideFragment newInstance(int sectionNumber) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_guide, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {

//        NewsAdapter newsAdapter = new NewsAdapter(getActivity());
//
//        ListView newsListView = (ListView)rootView.findViewById(R.id.guide_listview);
//        newsListView.setAdapter(newsAdapter);
//        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                App.getInstance().objNews = (JSONObject)parent.getItemAtPosition(position);
//                Intent intent = new Intent(getActivity(),NewsDetailActivity.class);
//                startActivity(intent);
//            }
//        });

        mList = new ArrayList<>();
        mList.add(new GuideItem(1,"Name 1","Robert Lotto","10",R.drawable.g1));
        mList.add(new GuideItem(1,"Name 2","Lee Zun","9",R.drawable.g3));
        mAdapter = new GuideAdapter(getActivity(),mList);
        ((ListView)rootView.findViewById(R.id.guide_listview)).setAdapter(mAdapter);
        ((ListView)rootView.findViewById(R.id.guide_listview)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GuideDetailActivity.class);
                startActivity(intent);
            }
        });

    }

}
