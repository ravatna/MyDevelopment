package com.tyche.mobile.imember.adapter;

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

import com.tyche.mobile.imember.R;

import java.util.ArrayList;

/**
 * Created by rewat on 26/5/2560.
 */

public class GuideAdapter extends BaseAdapter {
    private Context ctx;
    private static LayoutInflater inflater=null;
private ArrayList<GuideItem> mGuideList;


    public GuideAdapter(Context _ctx, ArrayList<GuideItem> guideList) {
        ctx = _ctx;
        mGuideList = guideList;
        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mGuideList.size();
    }

    public GuideItem getItem(int position) {
        return mGuideList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        GuideItem item =  mGuideList.get(position);

        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout._item_info, null);


        ImageView thumb_image=(ImageView)vi.findViewById(R.id.imgPic); // thumb image
        TextView txvTitle = (TextView)vi.findViewById(R.id.txtTitle);
        TextView txvShortDesc = (TextView)vi.findViewById(R.id.txtShortDesc);
        LinearLayout lnrColor = (LinearLayout)vi.findViewById(R.id.lnrColor);
        RatingBar ratingBar = (RatingBar) vi.findViewById(R.id.ratingBar);

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        // Filled stars
        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(ctx, R.color.yellow));


        if(position % 2 == 1){
            lnrColor.setBackgroundColor(Color.parseColor("#D9AE91"));
        }else{
            lnrColor.setBackgroundColor(Color.parseColor("#8794D9"));
        }
        // Setting all values in listview



            txvTitle.setText(item.m_name);
            txvShortDesc.setText(item.m_shortDesc);
            thumb_image.setImageResource(item.m_resImg);




        return vi;
    }

    private void setRatingStarColor(Drawable drawable, @ColorInt int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            DrawableCompat.setTint(drawable, color);
        }
        else
        {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}
