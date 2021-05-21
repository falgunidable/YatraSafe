package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.finalproject.adapter.CasesAdapter;
import com.example.finalproject.model.Cases;
import com.example.finalproject.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CovidCases extends AppCompatActivity {
    private AppCompatImageView imageView;
    private RecyclerView recyclerView3;
    private CasesAdapter casesAdapter;
    private ArrayList<Cases> arrayList_;
    private ProgressDialog progressDialog;
    private StringRequest stringRequest_;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_cases);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#02025C"));
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView3 = (RecyclerView)findViewById(R.id.recyclerCase);
        recyclerView3.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView3.setHasFixedSize(true);
        arrayList_ = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        imageView =findViewById(R.id.imageView);
        Bitmap imageBitmap= BitmapFactory.decodeResource(getResources(),  R.drawable.cases);
        RoundedBitmapDrawable roundedBitmapDrawable=
                RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
        roundedBitmapDrawable.setCornerRadius(500.0f);
        roundedBitmapDrawable.setAntiAlias(true);
        imageView.setImageDrawable(roundedBitmapDrawable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData(){
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...Please Wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.create();
        progressDialog.show();

        requestQueue = Volley.newRequestQueue(this);
        stringRequest_  = new StringRequest(Request.Method.GET, Constants.CASES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("Maharashtra");
                    JSONObject result1 = result.getJSONObject("districtData");
                    JSONObject mData = result1.getJSONObject("Mumbai");
                    //JSONObject inData = mData.getJSONObject(String.valueOf(0));
                    Log.i("INFO", mData.names().toString());
                    Log.i("INFO", mData.getString("confirmed"));
                    Cases mCases = new Cases(mData.getString("confirmed"),mData.getString("recovered"),
                            mData.getString("deceased"));
                    arrayList_.add(mCases);
                    casesAdapter = new CasesAdapter(CovidCases.this,arrayList_);
                    recyclerView3.setAdapter(casesAdapter);
                } catch (JSONException e) {
                    Log.e("ERROR",e.getMessage());
                    progressDialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest_);
    }
}