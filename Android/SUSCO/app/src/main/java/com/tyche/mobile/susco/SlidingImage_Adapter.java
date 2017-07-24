package com.tyche.mobile.susco;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<Bitmap> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImage_Adapter(Context context,ArrayList<Bitmap> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);


//        imageView.setImageResource(IMAGES.get(position)/**/);
        imageView.setImageBitmap(IMAGES.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = App.getInstance().objBanner.getJSONArray("banner");
                    final JSONObject item = jsonArray.getJSONObject(position);
Log.i("JSON Banner url",item.getString("image"));
                    if(!item.getString("banner_url").equals("")) {


                        if(!item.getString("banner_url").contains("http://")) {
                            Uri webpage = Uri.parse("http://" + item.getString("banner_url"));
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                            context.startActivity(myIntent);
                        }else{
                            Uri webpage = Uri.parse(item.getString("banner_url"));
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                            context.startActivity(myIntent);
                        }




                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}