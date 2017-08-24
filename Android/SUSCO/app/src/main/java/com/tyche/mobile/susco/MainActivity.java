package com.tyche.mobile.susco;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    UserInfoFragment userInfoFragment;
    Button btnSuscoOnline,btnUserInfo;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ScrollDisabledViewPager mViewPager;
    private TextView mCaptionTitle;
    private ImageView mImgHeader;


    public  boolean isCanOnline() {
        ConnectivityManager
                cm = (ConnectivityManager) MainActivity.this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    } // .End isCanOnline

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // check internet connection.
        if(!isCanOnline()){

            // alert message about internet state on screen
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("การเชื่อมต่อเครือข่าย")
                    .setMessage("ไม่เพบการเชื่อมต่อเครือข่ายอินเตอร์เน็ตในปัจจุบัน")
                    .setNeutralButton("ปิด",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,  int which) {

                                }
                            })
                    .show();


        }



        setContentView(R.layout.activity_main);
        userInfoFragment = new UserInfoFragment();
        // * การแลกรับส่วนลดและของรางวัลท่านสามารถแลกได้ที่สถานีบริการใกล้บ้านท่าน (add this text to hint)
        // * home set header caption
        // * news detail use like banner and touch to big picture
        // set font to sukhumvit
        // * add id card line on user profile below mobile

        mCaptionTitle = (TextView)findViewById(R.id.toolbar_save);
        mImgHeader = (ImageView)findViewById(R.id.imgHeader);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ScrollDisabledViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(false);
        mViewPager.setOffscreenPageLimit(4);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tabLayout.getSelectedTabPosition() == 0){

                    tabLayout.getTabAt(0).setIcon(R.drawable.home_gray_32);
                    //tabLayout.getTabAt(4).setIcon(R.drawable.megaphon_gray_32);
                    tabLayout.getTabAt(1).setIcon(R.drawable.medal_gray_32);
                    tabLayout.getTabAt(2).setIcon(R.drawable.placeholder_gray_32);
                    tabLayout.getTabAt(3).setIcon(R.drawable.user_gray_64);
                    mCaptionTitle.setText("หน้าหลัก");
                    mCaptionTitle.setVisibility(View.GONE);
                    mImgHeader.setVisibility(View.VISIBLE);
                    tab.setIcon(R.drawable.home32);

                }if(tabLayout.getSelectedTabPosition() == 4){
                    tabLayout.getTabAt(0).setIcon(R.drawable.home_gray_32);
                    //tabLayout.getTabAt(4).setIcon(R.drawable.megaphon_gray_32);
                    tabLayout.getTabAt(1).setIcon(R.drawable.medal_gray_32);
                    tabLayout.getTabAt(2).setIcon(R.drawable.placeholder_gray_32);
                    tabLayout.getTabAt(3).setIcon(R.drawable.user_gray_64);
                    mCaptionTitle.setText("ข่าวประชาสัมพันธ์");
                    mCaptionTitle.setVisibility(View.GONE);
                    mImgHeader.setVisibility(View.VISIBLE);
                    tab.setIcon(R.drawable.megaphon32);
                }
                if(tabLayout.getSelectedTabPosition() == 1){
                    tabLayout.getTabAt(0).setIcon(R.drawable.home_gray_32);
                    //tabLayout.getTabAt(4).setIcon(R.drawable.megaphon_gray_32);
                    tabLayout.getTabAt(1).setIcon(R.drawable.medal_gray_32);
                    tabLayout.getTabAt(2).setIcon(R.drawable.placeholder_gray_32);
                    tabLayout.getTabAt(3).setIcon(R.drawable.user_gray_64);
                    mCaptionTitle.setVisibility(View.GONE);
                    mImgHeader.setVisibility(View.VISIBLE);
                    mCaptionTitle.setText("คะแนนสะสม");
                    tab.setIcon(R.drawable.medal32);
                }
                if(tabLayout.getSelectedTabPosition() == 2){
                    tabLayout.getTabAt(0).setIcon(R.drawable.home_gray_32);
                   // tabLayout.getTabAt(4).setIcon(R.drawable.megaphon_gray_32);
                    tabLayout.getTabAt(1).setIcon(R.drawable.medal_gray_32);
                    tabLayout.getTabAt(2).setIcon(R.drawable.placeholder_gray_32);
                    tabLayout.getTabAt(3).setIcon(R.drawable.user_gray_64);
                    mCaptionTitle.setVisibility(View.GONE);
                    mImgHeader.setVisibility(View.VISIBLE);
                    mCaptionTitle.setText("สาขา");
                    tab.setIcon(R.drawable.placeholder64);
                }
                if(tabLayout.getSelectedTabPosition() == 3){
                    tabLayout.getTabAt(0).setIcon(R.drawable.home_gray_32);
                    //tabLayout.getTabAt(4).setIcon(R.drawable.megaphon_gray_32);
                    tabLayout.getTabAt(1).setIcon(R.drawable.medal_gray_32);
                    tabLayout.getTabAt(2).setIcon(R.drawable.placeholder_gray_32);
                    tabLayout.getTabAt(3).setIcon(R.drawable.user_gray_64);
                    mCaptionTitle.setVisibility(View.GONE);
                    mImgHeader.setVisibility(View.VISIBLE);
                    mCaptionTitle.setText("ข้อมูลสมาชิก");
                    tab.setIcon(R.drawable.user64);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {



            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        btnSuscoOnline = (Button) findViewById(R.id.btnSuscoOnline);
        btnSuscoOnline.setVisibility(View.INVISIBLE);
        btnSuscoOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://www.susco.co.th/index.php"));
                startActivity(i);

            }
        });

        btnUserInfo = (Button)findViewById(R.id.btnUserInfo);
        btnUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPage(tabLayout,mViewPager,3);

            }
        });
        btnUserInfo.setVisibility(View.INVISIBLE);


        int[] tabIcons = new int[3];

        tabLayout.getTabAt(0).setIcon(R.drawable.home32);
        //tabLayout.getTabAt(4).setIcon(R.drawable.megaphon_gray_32);
        tabLayout.getTabAt(1).setIcon(R.drawable.medal_gray_32);
        tabLayout.getTabAt(2).setIcon(R.drawable.placeholder_gray_32);
        tabLayout.getTabAt(3).setIcon(R.drawable.user_gray_64);
    }


    @Override
    public void onBackPressed() {


              AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("ยืนยัน");
            alert.setMessage("ต้องการปิด SUSCO Smart Member ใช่หรือไม่?");

            alert.setNeutralButton( "ใช่",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });

            alert.setNegativeButton( "ไม่ใช่",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(-1 == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = this.getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ((ImageView)findViewById(R.id.imgProfile)).setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    void selectPage(TabLayout tabLayout, ViewPager viewPager, int pageIndex){
        tabLayout.setScrollPosition(pageIndex,0f,true);
        viewPager.setCurrentItem(pageIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if(position == 0){
                return  HomeFragment.newInstance(position);
            }else if(position == 4){
                return  NewsFragment.newInstance(position);
            }else if(position == 1){
                return  ScoreFragment.newInstance(position);
            }else if(position == 2){
                return  BranchFragment.newInstance(position);
            } else if(position == 3){
                return  userInfoFragment.newInstance(position);
            }

            return HomeFragment.newInstance(0);
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show  total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "หน้าหลัก";
                case 4:
                    return "ข่าวประชาสัมพันธ์";
                case 1:
                    return "คะแนนสะสม";
                case 2:
                    return "สาขา";
                case 3:
                    return "สมาชิก";
            }
            return null;

        }
    }
}
