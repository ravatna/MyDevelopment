package com.tyche.mobile.imember;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rewat on 26/5/2560.
 */

public class ReviewGuideAdapter extends BaseAdapter {
    private Context ctx;
    private static LayoutInflater inflater=null;
    private ArrayList<GuideReviewItem> mReviewGuideList;

    public ReviewGuideAdapter(Context _ctx, ArrayList<GuideReviewItem> guideReviewItem) {
        ctx = _ctx;
        mReviewGuideList = guideReviewItem;
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mReviewGuideList.size();
    }

    public GuideReviewItem getItem(int position) {
        return mReviewGuideList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        GuideReviewItem item =  mReviewGuideList.get(position);

        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout._item_guide_review, null);


        TextView txvTitle = (TextView)vi.findViewById(R.id.txtTitle);
        TextView txvShortDesc = (TextView)vi.findViewById(R.id.txtShortDesc);


        // Setting all values in listview
            txvTitle.setText(item.m_name);
            txvShortDesc.setText(item.m_shortDesc);


        return vi;
    }
}
