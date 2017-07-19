package com.tyche.mobile.iagent.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tyche.mobile.iagent.R;

import java.util.ArrayList;

/**
 * Created by rewat on 26/5/2560.
 */

public class InsuranceProgramAdapter extends BaseAdapter {
    private Context ctx;
    private static LayoutInflater inflater=null;
private ArrayList<InsuranceProgramItem> mGuideList;


    public InsuranceProgramAdapter(Context _ctx, ArrayList<InsuranceProgramItem> guideList) {
        ctx = _ctx;
        mGuideList = guideList;
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mGuideList.size();
    }

    public InsuranceProgramItem getItem(int position) {
        return mGuideList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        InsuranceProgramItem item =  mGuideList.get(position);

        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout._item_info_2, null);


        ImageView thumb_image=(ImageView)vi.findViewById(R.id.imgPic); // thumb image
        TextView txvTitle = (TextView)vi.findViewById(R.id.txtTitle);
        TextView txvShortDesc = (TextView)vi.findViewById(R.id.txtShortDesc);




        // Setting all values in listview

            txvTitle.setText(item.m_programName);
            txvShortDesc.setText(item.m_shortDesc);

        return vi;
    }


}
