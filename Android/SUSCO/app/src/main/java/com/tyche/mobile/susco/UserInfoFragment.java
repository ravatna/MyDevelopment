package com.tyche.mobile.susco;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Member;

import static com.tyche.mobile.susco.R.id.edtPassword;

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
    static UserInfoFragment fragment;

    private Button btnPageLeft,btnPageRight;
    private TextView txvMyName;
    private TextView txvPhoneNo;
    private TextView txvEmail,txvIdCard;
    private byte[] imageBytes;
    private ImageView imgProfile,imgFrameProfile;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    String _password="",_email="",_cid_card="",_imagebase64="",_fname="",_lname="";

    private String m_formToken,m_cookieToken;
    private static final String MY_PREFS = "susco_tyche";
    private String _mobile = "";
    private String tmpIdCard = "";
    private String tmpEmail = "";

    private LinearLayout lnrPhoneNo,lnrIdCard,lnrPassword,lnrEmail,lnrCard,lnrHistory,lnrLogout;
    private LinearLayout lnrMyName;

    public UserInfoFragment() {
        }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static UserInfoFragment newInstance(int sectionNumber) {
        if(fragment == null) {
            fragment = new UserInfoFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
        }
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


        lnrMyName = (LinearLayout) rootView.findViewById(R.id.lnrMyName);
        lnrIdCard = (LinearLayout) rootView.findViewById(R.id.lnrIdCard);
        lnrPhoneNo = (LinearLayout) rootView.findViewById(R.id.lnrPhoneNo);
        lnrPassword = (LinearLayout) rootView.findViewById(R.id.lnrPassword);
        lnrEmail = (LinearLayout) rootView.findViewById(R.id.lnrEmail);
        lnrCard = (LinearLayout) rootView.findViewById(R.id.lnrCard);
        lnrHistory = (LinearLayout) rootView.findViewById(R.id.lnrHistory);
        lnrLogout = (LinearLayout) rootView.findViewById(R.id.lnrLogout);

        txvIdCard = (TextView) rootView.findViewById(R.id.txvIdCard);
        txvMyName = (TextView) rootView.findViewById(R.id.txvMyName);
        txvEmail = (TextView) rootView.findViewById(R.id.txvEmail);
        txvPhoneNo = (TextView) rootView.findViewById(R.id.txvPhoneNo);
        imgProfile = (ImageView)rootView.findViewById(R.id.imgProfile);
        imgFrameProfile = (ImageView)rootView.findViewById(R.id.imgFrameProfile);

        try {

            txvPhoneNo.setText(App.getInstance().customerMember.getString("mobile"));

            // @todo debug
            if(!App.getInstance().customerMember.getString("fname").equals("") && !App.getInstance().customerMember.getString("lname").equals("")) {
                txvMyName.setText(App.getInstance().customerMember.getString("fname").replace("\r","").replace("\n","") + " " + App.getInstance().customerMember.getString("lname").replace("\r","").replace("\n",""));
            }else{
                txvMyName.setText("* แตะที่นี่เพื่อแก้ไข *");
            }


            // @todo debug
            if(!App.getInstance().customerMember.getString("email").equals("")) {

                txvEmail.setText(App.getInstance().customerMember.getString("email").replace("\r","").replace("\n",""));
                tmpEmail = App.getInstance().customerMember.getString("email").replace("\r","").replace("\n","");
            }else{
                txvEmail.setText("* แตะที่นี่เพื่อแก้ไข *");
            }


            if(!App.getInstance().customerMember.getString("cid_card").equals("")) {

                txvIdCard.setText(App.getInstance().customerMember.getString("cid_card").replace("\r","").replace("\n",""));
                tmpIdCard = App.getInstance().customerMember.getString("cid_card").replace("\r","").replace("\n","");
            }else{
                txvIdCard.setText("* แตะที่นี่เพื่อแก้ไข *");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        TextView txvEmail = (TextView)rootView.findViewById(R.id.txvEmail);
//        TextView txvPhone = (TextView)rootView.findViewById(R.id.txvPhoneNo);
//        TextView txvPassword = (TextView)rootView.findViewById(R.id.txvPassword);

///////////////////////////////////////////////////////////////////////
        lnrMyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder pDialog = new AlertDialog.Builder(getActivity());
                pDialog.setTitle("แก้ไข ชื่อ-สกุล");
                final EditText fname = new EditText(getActivity());
                final EditText lname = new EditText(getActivity());

                fname.setHint("ระบุชื่อ");
                lname.setHint("ระบุสกุล");
                LinearLayout ll=new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.VERTICAL);

                ll.addView(fname);
                ll.addView(lname);
                pDialog.setView(ll);
                pDialog.setPositiveButton("ปรับปรุง",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                boolean _b = false;
                                    if(fname.getText().toString().length() > 0 ){
                                        _fname = fname.getText().toString();
                                        dialog.dismiss();

                                        if(lname.getText().toString().length() > 0){
                                            _lname = lname.getText().toString();
                                            dialog.dismiss();
                                            doUpdateInfo();
                                        }else{
                                            AlertDialog.Builder ad2 = new AlertDialog.Builder(getActivity());
                                            ad2.setTitle("แจ้งเตือน");
                                            ad2.setMessage("โปรดระบุสกุล");
                                            ad2.setNeutralButton("ปิด",new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface d, int which) {
                                                    d.dismiss();
                                                }
                                            });
                                            ad2.show();
                                        }

                                    }else{
                                        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                                        ad.setTitle("แจ้งเตือน");
                                        ad.setMessage("โปรดระบุชื่อ");
                                        ad.setNeutralButton("ปิด",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface d, int which) {
                                                d.dismiss();
                                            }
                                        });
                                        ad.show();
                                    }

                            }

                        }).setNegativeButton("ยกเลิก",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                _password = "";
                            }
                        });

                AlertDialog alert111 = pDialog.create();
                alert111.show();
            }
        });
///////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////
        lnrPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder pDialog = new AlertDialog.Builder(getActivity());
                pDialog.setTitle("แก้ไขรหัสผ่าน");
                final EditText oldPass = new EditText(getActivity());
                final EditText newPass = new EditText(getActivity());
                final EditText confirmPass = new EditText(getActivity());

                oldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                confirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                oldPass.setHint("รหัสผ่านเดิม");
                newPass.setHint("รหัสผ่านใหม่");
                confirmPass.setHint("ยืนยันรหัสผ่าน");
                LinearLayout ll=new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.VERTICAL);

                ll.addView(oldPass);
                ll.addView(newPass);
                ll.addView(confirmPass);
                pDialog.setView(ll);
                pDialog.setPositiveButton("ปรับปรุง",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String pw = sharedPreferences.getString("pw","");
                                //@todo: valid add content
                                if( oldPass.getText().toString().equals(pw)){
                                    if(newPass.getText().toString().length() > 0
                                            &&newPass.getText().toString().equals(confirmPass.getText().toString())){
                                        _password = newPass.getText().toString();
                                        dialog.dismiss(); doUpdateInfo();
                                    }else{
                                        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                                        ad.setTitle("แจ้งเตือน");
                                        ad.setMessage("รหัสผ่านใหม่ไม่ถูกต้อง");
                                        ad.setNeutralButton("ปิด",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface d, int which) {
                                                d.dismiss();
                                            }
                                        });
                                        ad.show();
                                    }

                                }else{
                                    AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                                    ad.setTitle("แจ้งเตือน");
                                    ad.setMessage("รหัสผ่านเดิมไม่ถูกต้อง");
                                    ad.setNeutralButton("ปิด",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface d, int which) {
                                            d.dismiss();
                                        }
                                    });
                                    ad.show();
                                }

                            }

                        }).setNegativeButton("ยกเลิก",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                _password = "";
                            }
                        });

                AlertDialog alert111 = pDialog.create();
                alert111.show();
            }
        });
///////////////////////////////////////////////////////////////////////

lnrPhoneNo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("แก้ไขเบอร์ติดต่อ");
        final EditText edtPassword = new EditText(getActivity());
        int maxLength = 10;
        edtPassword.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

        edtPassword.setHint("เบอร์โทรศัพท์");
        edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        LinearLayout ll=new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);

        ll.addView(edtPassword);
        alertDialog.setView(ll);
        alertDialog.setPositiveButton("ปรับปรุง",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                     //   if(edtPassword.getText().toString().length() >= 9) {
                            if(!App.getInstance().validMobilePhone(edtPassword.getText().toString())){

                            _mobile = "";
                            dialog.dismiss();
                            doUpdateInfo();
                        }else{
                            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                            ad.setMessage("เบอร์ติดต่อไม่ถูกต้อง");
                            ad.setTitle("แจ้งเตือน");
                            ad.setNeutralButton("ปิด", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface d, int which) {
                                    d.dismiss();
                                }
                            });
                            ad.show();
                        }
                    }
                });

        alertDialog.setNegativeButton("ยกเลิก",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        _mobile = "";
                    }
                });

        AlertDialog alert11 = alertDialog.create();
//alert11.show();
        try {
            if(!App.getInstance().customerMember.getString("mobile").equals("")){
                AlertDialog.Builder aaDialog = new AlertDialog.Builder(getActivity());
                aaDialog.setTitle("แก้ไขเบอร์ติดต่อ");
                aaDialog.setMessage(getResources().getString(R.string.msg_contact));
                aaDialog.setNegativeButton("ปิด",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog aa = aaDialog.create();
                aa.show();
            }else {


                alert11.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            alert11.show();
        }
    }
});
///////////////////////////////////////////////////////////////////////
        lnrEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("แก้ไขอีเมล์");
                final EditText edtPassword = new EditText(getActivity());
                edtPassword.setHint("อีเมล์");
                edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                int maxLength = 32;
                edtPassword.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});

                LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.VERTICAL);

                ll.addView(edtPassword);
                alertDialog.setView(ll);
                alertDialog.setPositiveButton("ปรับปรุง",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(!App.getInstance().validEmail(edtPassword.getText().toString())){
//                                if(edtPassword.getText().toString().length() >= 5) {

                                    _email = edtPassword.getText().toString();
                                    dialog.dismiss();
                                    doUpdateInfo();

                                }else{
                                    AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                                    ad.setMessage("อีเมล์ไม่ถูกต้อง");
                                    ad.setTitle("แจ้งเตือน");
                                    ad.setNeutralButton("ปิด", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface d, int which) {
                                            d.dismiss();
                                        }
                                    });
                                    ad.show();
                                }

                            }
                        });

                alertDialog.setNegativeButton("ยกเลิก",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                _email = "";
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = alertDialog.create(); // init alert before update

                try {

                    if(!App.getInstance().customerMember.getString("email").equals("")){

                        AlertDialog.Builder aaDialog = new AlertDialog.Builder(getActivity());
                        aaDialog.setTitle("แก้ไขอีเมล์");
                        aaDialog.setMessage(getResources().getString(R.string.msg_contact));
                        aaDialog.setNegativeButton("ปิด",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog aa = aaDialog.create();
                        aa.show();
                    }else {
                        alert11.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    alert11.show();
                }
            }
        });

///////////////////////////////////////////////////////////////////////
        lnrIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("แก้ไขเลขบัตรประจำตัวประชาชน");
                final EditText edtPassword = new EditText(getActivity());
                edtPassword.setHint("เลขบัตรประจำตัวประชาชน");
                edtPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputFilter.LengthFilter lengthFilter = new InputFilter.LengthFilter(13);
                edtPassword.setFilters(new InputFilter[]{lengthFilter});

                LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.VERTICAL);

                ll.addView(edtPassword);
                alertDialog.setView(ll);
                alertDialog.setPositiveButton("ปรับปรุง",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if(edtPassword.getText().toString().length() == 13) {

                                    long v = Long.parseLong(edtPassword.getText().toString());

                                    if(App.getInstance().validThaiIDCard(v)){
                                        _cid_card = edtPassword.getText().toString();

                                        doUpdateInfo();
                                    }else{
                                        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                                        ad.setMessage("เลขบัตรประจำตัวประชาชนไม่ถูกต้อง");
                                        ad.setTitle("แจ้งเตือน");
                                        ad.setNeutralButton("ปิด", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface d, int which) {
                                                d.dismiss();
                                            }
                                        });
                                        ad.show();
                                    }


                                    dialog.dismiss();


                                }else{
                                    AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                                    ad.setMessage("เลขบัตรประจำตัวประชาชนไม่ถูกต้อง");
                                    ad.setTitle("แจ้งเตือน");
                                    ad.setNeutralButton("ปิด", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface d, int which) {
                                            d.dismiss();
                                        }
                                    });
                                    ad.show();
                                }

                            }
                        });

                alertDialog.setNegativeButton("ยกเลิก",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                _cid_card = "";
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = alertDialog.create();



                    // ถ้ามีข้อมูล เดิมอยู่แล้ว หรือ มีข้อมูลที่ได้จากการกรอกสำเร็จ ให้ป้องกันการแก้ไขทันท่ี
                    if(!tmpIdCard.equals("")){
                        AlertDialog.Builder aaDialog = new AlertDialog.Builder(getActivity());
                        aaDialog.setTitle("แก้ไขเลขบัตรประจำตัวประชาชน");
                        aaDialog.setMessage(getResources().getString(R.string.msg_contact));
                        aaDialog.setNegativeButton("ปิด",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog aa = aaDialog.create();
                        aa.show();
                    }else {
                        alert11.show();
                    }

            }
        });
///////////////////////////////////////////////////////////////////////
        TextView txvMemberCard = (TextView)rootView.findViewById(R.id.txvCard);
        lnrCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(),CardActivity.class);

                getActivity().startActivity(intent);
                // @todo add content

            }
        });

//////////////////////////////////////////////////////////////////////
        TextView txvMemberHistory = (TextView)rootView.findViewById(R.id.txvHistory);
        lnrHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getActivity(), MemberHistoryActivity.class);

                getActivity().startActivity(intent);
                // @todo add content

            }
        });


        TextView txvLogout = (TextView)rootView.findViewById(R.id.txvLogout);
        txvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //@todo: add confirm dialog.
                new AlertDialog.Builder(getActivity())
                        .setTitle("ลงชื่อออก")
                        .setMessage("ต้องการลงชื่อออกจากระบบใช่หรือไม่?")
                        .setPositiveButton("ใช่",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        editor.putString("login_json","");
                                        editor.commit();

                                        getActivity().finish();
                                        getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));
                                    }
                                })
                        .setNegativeButton(
                                "ไม่",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                }).show();


            }
        });

        imgFrameProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePictureProfileActivity.class);
                startActivity(intent);
            }
        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            imgProfile.setImageDrawable(getActivity().getDrawable(R.drawable.user));
//        } else {
//            imgProfile.setImageDrawable(getActivity().getDrawable(R.drawable.user,getContext().getTheme()));
//        }



        // if imgProfile == null then do assign value image from base64 string.
        if(App.getInstance().imgProfile == null) {
            String imageString = "";
            try {
                imageString = App.getInstance().customerMember.getString("cid_card_pic");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!imageString.equals("")) {
                //decode base64 string to image
                imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                App.getInstance().imgProfile = decodedImage;
                imgProfile.setImageBitmap(App.getInstance().imgProfile);
            } // .End if !imageString == ""

        }// .End if imgProfile != null
else {
            imgProfile.setImageBitmap(App.getInstance().imgProfile);
        }



        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                getActivity().startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        overrideFonts(getActivity(),rootView );
        return rootView;
    }

    /////////////////////////////////////////////////////////////////////////////////

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /////////////////////////////////////////////////////////////////////////////////


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

    /////////////////////////////////////////////////////////////
    public  boolean isCanOnline() {
        ConnectivityManager
                cm = (ConnectivityManager) getActivity().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    } // .End isCanOnline


    /////////////////////////////////////////////////////////
    private void doUpdateInfo() {

        // check internet connection.
        if(! isCanOnline()){

            // alert message about internet state on screen
            new AlertDialog.Builder(getActivity())
                    .setTitle("การเชื่อมต่อเครือข่าย")
                    .setMessage("ไม่พบการเชื่อมต่อเครือข่ายอินเตอร์เน็ตในปัจจุบัน")
                    .setNeutralButton("ปิด",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,  int which) {

                                }
                            })
                    .show();


        } else{
            // new ScoreFragment.CatalogForMember().execute();
            new  UpdateInfo().execute();
        }



    }

    /////////////////////////////////////////////////////////
    private void doUpdateInfoImage() {

        // check internet connection.
        if(! isCanOnline()){

            // alert message about internet state on screen
            new AlertDialog.Builder(getActivity())
                    .setTitle("การเชื่อมต่อเครือข่าย")
                    .setMessage("ไม่พบการเชื่อมต่อเครือข่ายอินเตอร์เน็ตในปัจจุบัน")
                    .setNeutralButton("ปิด",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,  int which) {

                                }
                            })
                    .show();


        } else{

            // alert message about internet state on screen
            new AlertDialog.Builder(getActivity())
                    .setTitle("ยืนยัน")
                    .setMessage("ต้องการบันทึกภาพที่เลือกใช่หรือไม่")
                    .setPositiveButton("ใช่",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,  int which) {
                                    new  UpdateInfoImage().execute();
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("ไม่",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog,  int which) {

                            imgProfile.setImageBitmap(App.getInstance().imgProfile);
                            dialog.cancel();
                        }
                    })
                    .show();


        }



    }

    private class UpdateInfo extends AsyncTask<Void, Void, String> {
        String strJson,postUrl;
        ProgressDialog pd;
        String _mcode = "";
        @Override
        protected void onPreExecute() {

            try {
                _mcode = App.getInstance().customerMember.getString("member_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Create Show ProgressBar
            strJson = "{'member_code':'" + _mcode  + "'"

                    + ",'password':'" + _password + "'"
                    + ",'email':'" + _email + "'"
                    + ",'frist_name':'" + _fname + "'"
                    + ",'last_name':'" + _lname + "'"
                    + ",'cid_card':'" + _cid_card + "'"
                    + ",'imagebase64':'" + _imagebase64 + "'"

                    + ",'formToken':'" + m_formToken + "'"
                    + ",'cookieToken':'" + m_cookieToken
                    + "'}";
            postUrl  = App.getInstance().m_server + "/UpdateDetailCustomer/UpdateDetail";
            pd = new ProgressDialog(getActivity());
            pd.setMessage("กำลังดำเนินการ...");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(Void... urls)   {
            String result = null;
            try {
                result = post(postUrl, strJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // blah blah

            return result;
        }

        protected void onPostExecute(String result)  {

            if(pd.isShowing()){
                pd.dismiss();
                pd = null;
            }

            parseResultUpdateInfo(result);

        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

    }


    /**
     * Task for update image profile of user.
      */
    private class UpdateInfoImage extends AsyncTask<Void, Void, String> {
        String strJson,postUrl;
        ProgressDialog pd;
        String _mcode = "";
        @Override
        protected void onPreExecute() {

            try {
                _mcode = App.getInstance().customerMember.getString("member_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Create Show ProgressBar
            strJson = "{'member_code':'" + _mcode  + "'"

                    + ",'password':'" + _password + "'"
                    + ",'email':'" + _email + "'"
                    + ",'frist_name':'" + _fname + "'"
                    + ",'last_name':'" + _lname + "'"
                    + ",'cid_card':'" + _cid_card + "'"
                    + ",'imagebase64':'" + _imagebase64 + "'"

                    + ",'formToken':'" + m_formToken + "'"
                    + ",'cookieToken':'" + m_cookieToken
                    + "'}";
            postUrl  = App.getInstance().m_server + "/UpdateDetailCustomer/UpdateDetail";
            pd = new ProgressDialog(getActivity());
            pd.setMessage("กำลังดำเนินการ...");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(Void... urls)   {
            String result = null;
            try {
                result = post(postUrl, strJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // blah blah

            return result;
        }

        protected void onPostExecute(String result)  {

            if(pd.isShowing()){
                pd.dismiss();
                pd = null;
            }

            parseResultUpdateInfoImage(result);

        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

    }// .End update image

    private void parseResultUpdateInfo(String result) {
        // clear need update image and clear temp image
        clearTempProfileProcess();

        if(result == null)
            return ;

        ////////////////////////////////


        try {
            JSONObject jsonObj = new JSONObject(result);
            if(jsonObj.getString("success").equals("true")) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("ปรับปรุงข้อมูล")
                        .setMessage("ปรับปรุงข้อมูลเรียบร้อยแล้ว\n กรุณาออกจากระบบและเข้าสู่ระบบใหม่อีกครั้ง")
                        .setPositiveButton("ปิด",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        if(!_cid_card.isEmpty()){
                                            tmpIdCard = _cid_card;
                                            txvIdCard.setText(_cid_card);
                                            _cid_card = "";
                                        }

                                        _mobile = "";
                                        _email = "";
                                        _imagebase64 = "";
                                        _password = "";
                                        _fname = "";
                                        _lname="";
                                        dialog.dismiss();
                                        new ReLogin().execute();
                                    }
                                })
                        .show();
            }else{
                new AlertDialog.Builder(getActivity())
                        .setTitle("ปรับปรุงข้อมูล")
                        .setMessage("ไม่สามารถแก้ไขข้อมูลขณะนี้ โปรดทำรายการใหม่ภายหลัง")
                        .setPositiveButton("ปิด",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .show();

                _cid_card = "";
                _mobile = "";
                _email = "";
                _imagebase64 = "";
                _password = "";
                _fname = "";
                _lname="";
            }
        } catch (JSONException e) {
            e.printStackTrace();

            new AlertDialog.Builder(getActivity())
                    .setTitle("ปรับปรุงข้อมูล")
                    .setMessage("ไม่สามารถแก้ไขข้อมูลขณะนี้ โปรดทำรายการใหม่ภายหลัง")
                    .setPositiveButton("ปิด",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            })
                    .show();

            _cid_card = "";
            _mobile = "";
            _email = "";
            _imagebase64 = "";
            _password = "";
            _fname = "";
            _lname="";
        }



    }

    /**
     * Parse content from json string.
     * @param result
     */
    private void parseResultUpdateInfoImage(String result) {



        clearTempProfileProcess();


        if(result == null)
            return ;

        ////////////////////////////////

        try {
            JSONObject jsonObj = new JSONObject(result);
            if(jsonObj.getString("success").equals("true")) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("ปรับปรุงข้อมูล")
                        .setMessage("ปรับปรุงข้อมูลเรียบร้อยแล้ว")
                        .setPositiveButton("ปิด",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        _cid_card = "";
                                        App.getInstance().imgProfile = App.getInstance().imgTempProfile;
                                        //App.getInstance().imgTempProfile = null;
                                        _mobile = "";
                                        _email = "";
                                        _imagebase64 = "";
                                        _password = "";
                                        _fname = "";
                                        _lname="";
                                        dialog.dismiss();
                                        new ReLogin().execute();
                                    }
                                })
                        .show();
            }else{
                new AlertDialog.Builder(getActivity())
                        .setTitle("ปรับปรุงข้อมูล")
                        .setMessage("ไม่สามารถแก้ไขข้อมูลขณะนี้ โปรดทำรายการใหม่ภายหลัง")
                        .setPositiveButton("ปิด",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .show();

                _cid_card = "";
                _mobile = "";
                _email = "";
                _imagebase64 = "";
                _password = "";
                _fname = "";
                _lname="";
            }
        } catch (JSONException e) {
            e.printStackTrace();

            new AlertDialog.Builder(getActivity())
                    .setTitle("ปรับปรุงข้อมูล")
                    .setMessage("ไม่สามารถแก้ไขข้อมูลขณะนี้ โปรดทำรายการใหม่ภายหลัง")
                    .setPositiveButton("ปิด",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            })
                    .show();

            _cid_card = "";
            _mobile = "";
            _email = "";
            _imagebase64 = "";
            _password = "";
            _fname = "";
            _lname="";
        }

        new ReLogin().execute();

    }// End

    private void clearTempProfileProcess() {
        // update need update image prifile to false in any case.
        App.getInstance().needUpdateImageProfile = false;
        // clear temp in image
        App.getInstance().imgTempProfile = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        // check pass working form change image profile.
        if(App.getInstance().needUpdateImageProfile == true) {

            if (App.getInstance().imgTempProfile != null) {
                imgProfile.setImageBitmap(App.getInstance().imgTempProfile);
                _imagebase64 = App.convert(App.getInstance().imgTempProfile);

                doUpdateInfoImage();
            }

            App.getInstance().needUpdateImageProfile = false;
        }
    }


    private class ReLogin extends AsyncTask<Void, Void, String> {
        String strJson,postUrl;
        ProgressDialog pd;
        String u = "",p = "";
        @Override
        protected void onPreExecute() {
            try {
                u = App.getInstance().customerMember.getString("mobile");
                p = sharedPreferences.getString("pw","");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Create Show ProgressBar
            strJson = "{'mobile_customer':'" + u  + "','pass_customer':'" + p + "'}";
            postUrl  = App.getInstance().m_server + "/Security/login_customer_susco";
            pd = new ProgressDialog(getActivity());
            pd.setMessage("กำลังดำเนินการ...");
            pd.setCancelable(false);
            pd.show();

        }

        protected String doInBackground(Void... urls)   {

            String result = null;
            try {
                result = post(postUrl, strJson);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(String result)  {

            if(pd.isShowing()){
                pd.dismiss();
                pd = null;
            }

            parseResultLogin(result);

        }


        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }

    }

    private void parseResultLogin(String result) {

        try {
            JSONObject jsonObj = new JSONObject(result);
            App.getInstance().loginObject = jsonObj;

            editor.putString("login_json",jsonObj.toString());
            editor.commit();

            if(jsonObj.getBoolean("success"))
            {

                JSONArray arr = jsonObj.getJSONArray("customer_detail");

                App.getInstance().formToken = jsonObj.getString("formToken");
                App.getInstance().cookieToken = jsonObj.getString("cookieToken");

                App.getInstance().customerMember = arr.getJSONObject(0);
                //App.getInstance().selectNews = jsonObj.getJSONArray("select_news");

                byte[] imageBytes;
                String imageString = "";
                try {
                    imageString = App.getInstance().customerMember.getString("cid_card_pic");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!imageString.equals("")) {
                    //decode base64 string to image
                    imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    App.getInstance().imgProfile = decodedImage;

                } // .End if !imageString == ""
            } // .End if


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }// .End parseResult

    }