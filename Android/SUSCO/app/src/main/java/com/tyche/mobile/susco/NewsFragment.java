package com.tyche.mobile.susco;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Vinit on 24/5/2560.
 */

 public class NewsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public NewsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NewsFragment newInstance(int sectionNumber) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_news, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {

        NewsAdapter newsAdapter = new NewsAdapter(getActivity());

        ListView newsListView = (ListView)rootView.findViewById(R.id.news_listview);
        newsListView.setAdapter(newsAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                App.getInstance().objNews = (JSONObject)parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(),NewsDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    private class Banner extends AsyncTask<Void, Void, String> {
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
            strJson = "{'membercode':'" + _mcode  + "','formToken':'" + App.getInstance().formToken  + "','cookieToken':'" + App.getInstance().cookieToken  + "'}";
            postUrl  = App.getInstance().m_server + "/Banner/GetBanner";
            //            pd = new ProgressDialog(getActivity() );
            //            pd.setMessage("กำลังดำเนินการ...");
            //            pd.setCancelable(false);
            //            pd.show();

        }

        protected String doInBackground(Void... urls)   {

            String result = null;
            try {

                /////////////////////////////
                RequestBody body = RequestBody.create(JSON, strJson);
                Request request = new Request.Builder()
                        .url(postUrl)
                        .addHeader("formToken",App.getInstance().formToken)
                        .addHeader("cookieToken",App.getInstance().cookieToken)
                        .post(body)
                        .build();


                Response response = client.newCall(request).execute();
                result = response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        protected void onPostExecute(String result)  {

            //            if(pd.isShowing()){
            //                pd.dismiss();
            //                pd = null;
            //            }

//            parseResultBanner(result);

        }

        public  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

    }// .End Banner

}
