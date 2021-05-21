package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.finalproject.adapter.ListDoctorAdapter;
import com.example.finalproject.adapter.MyDoctorAdapter;
import com.example.finalproject.model.Data;
import com.example.finalproject.model.StatesCities;
import com.example.finalproject.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoctorList extends AppCompatActivity implements ListDoctorAdapter.OnItemClickListener{
    private SharedPreferences preferences;
    private ListDoctorAdapter doctorAdapter;
    private ArrayList<Data> doctorList;
    private RecyclerView recyclerView5;
    private final String  KEY_TYPE = "usertype";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#02025C"));
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        recyclerView5 = findViewById(R.id.recyclerView5);
        recyclerView5.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView5.setHasFixedSize(true);
        doctorList = new ArrayList<>();
        loadData();
    }

    public void loadData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.DOCTOR_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("dlist");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jOBj = jsonArray.getJSONObject(i);
                        Data data_ = new Data();
                        data_.setName(jOBj.getString("name"));
                        data_.setPhone(jOBj.getString("contact"));
                        Log.i("INFO", String.valueOf(data_.getName()));
                        doctorList.add(data_);
                    }
                    doctorAdapter = new ListDoctorAdapter(DoctorList.this,doctorList);
                    recyclerView5.setAdapter(doctorAdapter);
                    doctorAdapter.setOnItemClickListener(DoctorList.this);
                }catch (JSONException e){
                    Log.e("JSON",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR:",error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(KEY_TYPE, preferences.getString("usertype",""));
                return hashMap;
            }
        };
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        volleySingleton.addToRequestQueue(stringRequest);
    }

    @Override
    public void OnItemClick(int position) {
        if(position==0){
            Intent intent = new Intent(DoctorList.this,MapsActivity.class);
            startActivity(intent);
        }
        else if(position==1){
        Intent intent = new Intent(DoctorList.this,MapsActivity.class);
        startActivity(intent);
        }
    }
}