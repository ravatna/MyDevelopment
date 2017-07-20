package com.tyche.mobile.imember.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.tyche.mobile.imember.R;
import com.tyche.mobile.imember.ScrollingActivity;
import com.tyche.mobile.imember.VideoPlayerActivity;

/**
 * Created by Vinit on 24/5/2560.
 */

 public class BranchFragment extends Fragment implements OnMapReadyCallback{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    SupportMapFragment mapFragment;
    public BranchFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    static private BranchFragment fragment ;
//    public static BranchFragment newInstance(int sectionNumber) {
//
//        if(fragment == null) {
//            fragment = new BranchFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//        }
//
//        return fragment;
//    }
HorizontalScrollView hscrollView;
ImageButton imbMapPlace;
    View rootView;
    Button btnSOS;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // rootView  = inflater.inflate(R.layout.fragment_main_branch, container, false);


        if (rootView != null) {
            ViewGroup viewGroupParent = (ViewGroup) rootView.getParent();
            if (viewGroupParent != null)
                viewGroupParent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_main_branch, container, false);
            imbMapPlace = (ImageButton)rootView.findViewById(R.id.imbMapPlace);
            hscrollView = (HorizontalScrollView)rootView.findViewById(R.id.hscrollView);

            imbMapPlace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(hscrollView.getVisibility()== View.VISIBLE) {
                        hscrollView.setVisibility(View.GONE);
                        imbMapPlace.setBackgroundResource(R.drawable.arrows_up);
                    }else{
                        hscrollView.setVisibility(View.VISIBLE);
                        imbMapPlace.setBackgroundResource(R.drawable.arrows_down);
                    }
                }
            });


            ImageView imgPlace1 = (ImageView) rootView.findViewById(R.id.imgPlace1);
            ImageView imgPlace2 = (ImageView) rootView.findViewById(R.id.imgPlace2);
            imgPlace1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),ScrollingActivity.class);
                    startActivity(intent);
                }
            });

            imgPlace2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),VideoPlayerActivity.class);
                    startActivity(intent);
                }
            });

             btnSOS = (Button)rootView.findViewById(R.id.btnSOS);

            btnSOS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //@todo: add confirm dialog.
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Confirm")
                            .setMessage("Are you to need help from officer ?")
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                            ////////////////////////////////////////////////
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("Info")
                                                    .setMessage("Service was accept your request. Please, wait for officer contact to you.")
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


            mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager() .findFragmentById(R.id.mapView);
        } catch (Exception e) {

//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                //
//            }
//        });
        }



        return rootView;
    }

    @Override
    public void onDestroy() {
        if(mapFragment != null) {
            mapFragment.onDestroy();
        }
        super.onDestroy();

    }

    @Override
    public void onResume() {

        if(mapFragment != null) {
            mapFragment.onResume();
        }
        super.onResume();
    }

    @Override
    public void onLowMemory() {

        if(mapFragment != null) {
            mapFragment.onLowMemory();
        }
        super.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
