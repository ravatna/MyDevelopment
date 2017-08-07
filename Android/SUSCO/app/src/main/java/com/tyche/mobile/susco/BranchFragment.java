package com.tyche.mobile.susco;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Created by Vinit on 24/5/2560.
 */

 public class BranchFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public BranchFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static BranchFragment newInstance(int sectionNumber) {
        BranchFragment fragment = new BranchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_branch, container, false);

        WebView myWebView = (WebView)rootView.findViewById(R.id.branchWebView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setGeolocationEnabled(true);
        myWebView.getSettings().setAllowContentAccess(true);
//        myWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
       // myWebView.setWebChromeClient(new WebChromeClient()); //same as above

        //myWebView.loadUrl("https://www.google.com/maps/d/viewer?mid=1Y0VSBNMlw9Q2r4Wpk5NkPrUbn9s&ll=14.305836340137358%2C102.16185804805923&z=6");

        myWebView.loadUrl("file:///android_asset/newhtml.html");

        return rootView;
    }

}
