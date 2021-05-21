package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.finalproject.adapter.ListDoctorAdapter;
import com.example.finalproject.adapter.UserInput;
import com.example.finalproject.model.Data;
import com.example.finalproject.model.UserInputData;
import com.example.finalproject.util.Constants;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAddActivity extends AppCompatActivity {
    private static EditText nameText, suggestText, locationText, dateText;
    private LinearLayout layoutData;
    private AppCompatButton addBtn,hideBtn,showBtn;
    private SharedPreferences preferences;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private UserInput adapter;
    private ArrayList<UserInputData> arrayList_;
    private RecyclerView userRecycler;
    private static final String KEY_NAME = "Name";
    private static final String KEY_SUGGESTION = "Suggestion";
    private static final String KEY_LOCATION = "Location";
    private static final String KEY_DATE = "Published";
    private static final String KEY_RATING = "rating";
    private static String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#02025C"));
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        userRecycler = findViewById(R.id.recyclerViewUser);
        userRecycler.setLayoutManager(new GridLayoutManager(this,1));
        userRecycler.setHasFixedSize(true);
        arrayList_ = new ArrayList<>();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==addBtn) {
                    registerUser();
                }
            }
        });
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==showBtn)
                    layoutData.setVisibility(View.VISIBLE);
            }
        });
        hideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==hideBtn)
                    layoutData.setVisibility(View.GONE);
            }
        });
    }

    public void init(){
        nameText = findViewById(R.id.nameUserText);
        suggestText = findViewById(R.id.suggestText);
        locationText = findViewById(R.id.editTextTextPostalAddress);
        dateText = findViewById(R.id.editTextDate);
        addBtn = findViewById(R.id.addData);
        hideBtn = findViewById(R.id.hideData);
        showBtn = findViewById(R.id.showData);
        layoutData = findViewById(R.id.layoutAdd);
    }

    public void registerUser(){
        String name = nameText.getText().toString().trim();
        String suggest = suggestText.getText().toString().trim();
        String location = locationText.getText().toString().trim();
        String date = dateText.getText().toString().trim();

        UserInputData userInput = new UserInputData(name,suggest,location,date);
        stringRequest = new StringRequest(Request.Method.POST, Constants.USER_INPUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO",response);
                if(response.equals("success")){
                    Toast.makeText(getApplicationContext(), "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    nameText.setText("");
                    suggestText.setText("");
                    locationText.setText("");
                    dateText.setText("");
                }else{
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.getMessage());
            }
        })
        {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_NAME, userInput.getName());
                hashMap.put(KEY_SUGGESTION, userInput.getSuggestion());
                hashMap.put(KEY_LOCATION, userInput.getLocation());
                hashMap.put(KEY_DATE,userInput.getDate());
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
    }

    public void loadData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.DISPLAY_UDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO-",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("duser");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jOBj = jsonArray.getJSONObject(i);
                        UserInputData userInputData = new UserInputData();
                        userInputData.setName(jOBj.getString("Name"));
                        userInputData.setSuggestion(jOBj.getString("Suggestion"));
                        userInputData.setLocation(jOBj.getString("Location"));
                        userInputData.setDate(jOBj.getString("Published"));
                        arrayList_.add(userInputData);
                    }
                    adapter = new UserInput(UserAddActivity.this,arrayList_);
                    userRecycler.setAdapter(adapter);
                }catch (JSONException e){
                    Log.e("JSON",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR:",error.toString());
            }
        });
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        volleySingleton.addToRequestQueue(stringRequest);
    }
}