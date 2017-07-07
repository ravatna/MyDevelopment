package com.tyche.mobile.angkortour;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    BranchFragment angkorMap;
    Button btnSuscoOnline,btnUserInfo;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ScrollDisabledViewPager mViewPager;
    private TextView mCaptionTitle;
    private ImageView mImgHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // * การแลกรับส่วนลดและของรางวัลท่านสามารถแลกได้ที่สถานีบริการใกล้บ้านท่าน (add this text to hint)
        // * home set header caption
        // * news detail use like banner and touch to big picture
        // set font to sukhumvit
        // * add id card line on user profile below mobile

        angkorMap = new BranchFragment();

        mCaptionTitle = (TextView)findViewById(R.id.toolbar_save);
        mImgHeader = (ImageView)findViewById(R.id.imgHeader);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ScrollDisabledViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(false);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tabLayout.getSelectedTabPosition() == 0){

                    tabLayout.getTabAt(0).setIcon(R.drawable.man_user_active);
                    tabLayout.getTabAt(1).setIcon(R.drawable.signs_inactive);
                    tabLayout.getTabAt(2).setIcon(R.drawable.people_inactive);
                    tabLayout.getTabAt(3).setIcon(R.drawable.shopping_cart_inactive);

                    mCaptionTitle.setText(getResources().getString(R.string.title_user));
                }

                if(tabLayout.getSelectedTabPosition() == 1){
                    tabLayout.getTabAt(0).setIcon(R.drawable.man_user_inactive);
                    tabLayout.getTabAt(1).setIcon(R.drawable.signs_active);
                    tabLayout.getTabAt(2).setIcon(R.drawable.people_inactive);
                    tabLayout.getTabAt(3).setIcon(R.drawable.shopping_cart_inactive);
                    mCaptionTitle.setText(getResources().getString(R.string.title_location));
                }
                if(tabLayout.getSelectedTabPosition() == 2){
                    tabLayout.getTabAt(0).setIcon(R.drawable.man_user_inactive);
                    tabLayout.getTabAt(1).setIcon(R.drawable.signs_inactive);
                    tabLayout.getTabAt(2).setIcon(R.drawable.people_active);
                    tabLayout.getTabAt(3).setIcon(R.drawable.shopping_cart_inactive);
                    mCaptionTitle.setText(getResources().getString(R.string.title_guide));

                }
                if(tabLayout.getSelectedTabPosition() == 3){
                    tabLayout.getTabAt(0).setIcon(R.drawable.man_user_inactive);
                    tabLayout.getTabAt(1).setIcon(R.drawable.signs_inactive);
                    tabLayout.getTabAt(2).setIcon(R.drawable.people_inactive);
                    tabLayout.getTabAt(3).setIcon(R.drawable.shopping_cart);
                    mCaptionTitle.setText(getResources().getString(R.string.title_trade));

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

                selectPage(tabLayout,mViewPager,0);

            }
        });
        btnUserInfo.setVisibility(View.INVISIBLE);
        int[] tabIcons = new int[4];

        tabLayout.getTabAt(0).setIcon(R.drawable.man_user_active);
        tabLayout.getTabAt(1).setIcon(R.drawable.signs_inactive);
        tabLayout.getTabAt(2).setIcon(R.drawable.people_inactive);
        tabLayout.getTabAt(3).setIcon(R.drawable.shopping_cart_inactive);

        //tabLayout.getTabAt(4).setIcon(R.drawable.megaphon_gray_32);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {



        if(-1 == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = this.getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    //((ImageView)findViewById(R.id.imgProfile)).setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
//            fragment.onActivityResult(requestCode, resultCode, data);
//        }
//    }

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
                return  UserInfoFragment.newInstance(position);
            }else if(position == 4){

            }else if(position == 1){
                return  angkorMap;
//                return  NewsFragment.newInstance(position);
               // return  ScoreFragment.newInstance(position);
            }else if(position == 2){
                return  GuideFragment.newInstance(position);
            } else if(position == 3){
                return ScanTradeFragment.newInstance(position);
//                return  TradeFragment.newInstance(position);
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
//            switch (position) {
//                case 0:
//                    return "หน้าหลัก";
//                case 4:
//                    return "ข่าวประชาสัมพันธ์";
//                case 1:
//                    return "คะแนนสะสม";
//                case 2:
//                    return "สาขา";
//                case 3:
//                    return "สมาชิก";
//            }
            return null;

        }


//
//@Override
//public CharSequence getPageTitle(int position) {
//
//
//    int icon[] = new int[4];
//    icon[0] = R.drawable.ic_user_selector;
//    icon[1] = R.drawable.ic_user_selector;
//    icon[2] = R.drawable.ic_user_selector;
//    icon[3] = R.drawable.ic_user_selector;
//
//    Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.home64);
//    drawable.setBounds(0,0,48,48);
//    ImageSpan imageSpan = new ImageSpan(drawable);
//    SpannableString spannableString = new SpannableString(" ");
//    spannableString.setSpan(imageSpan, 0,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//    return spannableString;
//}

    }
}
