package com.example.covid19ja.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19ja.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.ReferenceQueue;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private TextView tvTotalConfirmed, tvTotalDeaths, tvTotalRecovered, tvLasUpdated;
    private ProgressBar progressBar;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // call view
        tvTotalConfirmed=root.findViewById(R.id.tvLabelTotalConfirmed);
        tvTotalDeaths=root.findViewById(R.id.tvLabelTotalDeaths);
        tvTotalRecovered=root.findViewById(R.id.tvTotalRecovered);
        tvLasUpdated= root.findViewById(R.id.tvLasUpdated);
        progressBar=root.findViewById(R.id.progress_circular_home);

        // proceso de volley
        getData();

        return root;
    }

    private String getData(long milliSecond){
        // friday, 22 th May 2020
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss aaa ");

        Calendar calendar=  Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);

         return formatter.format(calendar.getTime());

    }

    private void getData() {
        RequestQueue queue= Volley.newRequestQueue(getActivity());

        String url="https://corona.lmao.ninja/v2/all";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    tvTotalConfirmed.setText(jsonObject.getString("cases"));
                    tvTotalDeaths.setText(jsonObject.getString("deaths"));
                    tvTotalRecovered.setText(jsonObject.getString("recovered"));
                    tvLasUpdated.setText("Last Updated"+"\n"+getData(jsonObject.getLong("updated")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                  progressBar.setVisibility(View.GONE);
                Log.d("ERROR RESPUESTA",error.toString());
            }
        });

        queue.add(stringRequest);
    }
}
