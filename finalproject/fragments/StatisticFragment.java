package com.example.finalproject.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.R;
import com.example.finalproject.util.Constants;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatisticFragment extends Fragment {
    ProgressDialog progressDialog;
    String a, b, c;
    PieChart pieChart;
    private RequestQueue requestQueue;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        // Inflate the layout for this fragment
        pieChart = (PieChart) view.findViewById(R.id.PieChart);
        // pieChart.setUsePercentValues(true);
        RewardApi();
        return view;
    }

    public void RewardApi(){

        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.INDIA_CASES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO",response);
                Toast.makeText(getActivity(),"In Statistics",Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("data");
                    JSONObject innerd = result.getJSONObject("summary");
                    Log.i("RESULT",innerd.getString("total"));
                    Log.i("RESULT1",innerd.getString("discharged"));

                    a= innerd.getString("total");
                    b= innerd.getString("discharged");
                    c= innerd.getString("deaths");

                    ArrayList<Entry> values = new ArrayList<>();
                    values.add(new Entry(Float.parseFloat(a),0));
                    values.add(new Entry(Float.parseFloat(b),0));
                    values.add(new Entry(Float.parseFloat(c),0));

                    Log.i("VALUE", String.valueOf(values));

                    PieDataSet dataSet = new PieDataSet(values,"");

                    ArrayList<String> val = new ArrayList<>();
                    val.add("Total");
                    val.add("Recovered");
                    val.add("Deaths");

                    PieData pieData = new PieData(val,dataSet);
                    pieData.setValueFormatter(new DefaultValueFormatter(0));
                    pieChart.setData(pieData);
                    pieChart.setDescription("This is a Pie Chart");

                    pieChart.setDrawHoleEnabled(true);
                    pieChart.setHoleRadius(25f);
                    pieChart.setTransparentCircleRadius(25f);

                    dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
                    pieData.setValueTextSize(12f);
                    pieData.setValueTextColor(Color.DKGRAY);
                    pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                        }

                        @Override
                        public void onNothingSelected() {

                        }
                    });
                    pieChart.animateXY(1400,1400);

                }catch (JSONException e){
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR",error.toString());
            }
        });
        requestQueue.add(stringRequest);
    }
}