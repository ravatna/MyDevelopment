package com.tyche.mobile.angkortour;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;

import java.util.ArrayList;

public class GuideDetailActivity extends AppCompatActivity {

    private ArrayList<GuideReviewItem> mList;
    private ReviewGuideAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_detail);


initView();
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

    private void initView() {

        mList = new ArrayList<>();
        mList.add(new GuideReviewItem(1,"Review Name 1","He's good.","5",R.mipmap.ic_launcher_round));
        mList.add(new GuideReviewItem(2,"Review Name 2","Wow","5",R.mipmap.ic_launcher_round));
        mList.add(new GuideReviewItem(3,"Review Name 3","don't understand when he said about information.","3",R.mipmap.ic_launcher_round));


        mList.add(new GuideReviewItem(4,"Review Name 4","He's good.","5",R.mipmap.ic_launcher_round));
        mList.add(new GuideReviewItem(5,"Review Name 5","Wow","5",R.mipmap.ic_launcher_round));
        mList.add(new GuideReviewItem(6,"Review Name 6","don't understand when he said about information.","3",R.mipmap.ic_launcher_round));

        mAdapter = new ReviewGuideAdapter(this,mList);
        ((ListView)findViewById(R.id.reviwe_guide_listview)).setAdapter(mAdapter);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        // Filled stars
        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(this, R.color.yellow));

        Button btnBookingGuide = (Button)findViewById(R.id.btnBookingGuide);

        btnBookingGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@todo: add confirm dialog.
                new AlertDialog.Builder(GuideDetailActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Are you to booking this guide?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        ////////////////////////////////////////////////
                                        new AlertDialog.Builder(GuideDetailActivity.this)
                                                .setTitle("Info")
                                                .setMessage("Service was accept your request. Please, wait for guide contact to you.")
                                                .setPositiveButton("Ok",
                                                        new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface dialog2,
                                                                                int which) {
                                                                    dialog2.dismiss();
                                                            }
                                                        }) .show();
                                        ////////////////////////////////////////////////
                                    }
                                })
                        .setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                    }
                                }).show();
            }
        });

    }

}
