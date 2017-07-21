package com.tyche.mobile.iagent.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tyche.mobile.iagent.App;
import com.tyche.mobile.iagent.ContactHQActivity;
import com.tyche.mobile.iagent.LoginActivity;
import com.tyche.mobile.iagent.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Vinit on 24/5/2560.
 */

 public class UserInfoFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static int SELECT_PHOTO = -1;
    private TextView txvMyName;
    private TextView txvPointNo;
    private byte[] imageBytes;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String _password="",_email="",_cid_card="",_imagebase64="";

    private String m_formToken,m_cookieToken;
    private static final String MY_PREFS = "angkor_tyche";
    private String _mobile = "";
    private Button btnCallHQ;

    public UserInfoFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static UserInfoFragment newInstance(int sectionNumber) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_user, container, false);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        m_cookieToken = App.getInstance().cookieToken.toString();
        m_formToken = App.getInstance().formToken.toString();

        txvMyName = (TextView) rootView.findViewById(R.id.txvMyName);
        btnCallHQ = (Button)rootView.findViewById(R.id.btn_call_HQ);
        btnCallHQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //@todo: add confirm dialog.
                Intent intent = new Intent(UserInfoFragment.this.getActivity(), ContactHQActivity.class);
                startActivity(intent);
            }
        });




        Button btnLogout = (Button)rootView.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //@todo: add confirm dialog.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Logout")
                        .setMessage("Are you want to logout ?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {


                                        getActivity().finish();
                                        getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));
                                    }
                                })
                        .setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {


                                    }

                                }).show();


            }
        });

        overrideFonts(getActivity(),rootView );
        return rootView;
    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Kanit-Regular.ttf"));
            }
        } catch (Exception e) {
            Log.e("UpdateFontface",e.getMessage());
        }
    } // end method


}
