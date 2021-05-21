package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.finalproject.adapter.MyAdapterCovid;
import com.example.finalproject.fragments.ListFragment;
import com.example.finalproject.fragments.ProfileFragment;
import com.example.finalproject.fragments.StatisticFragment;
import com.example.finalproject.model.Data;
import com.example.finalproject.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DoctorActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#02025C"));
        getSupportActionBar().setBackgroundDrawable(drawable);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        Fragment fragment;

        int id = item.getItemId();
        if(id==R.id.ocean) {
            fragment = new ProfileFragment();
        }else if(id == R.id.book){
            fragment = new StatisticFragment();
        }else{
            fragment = new ListFragment();
        }
        transaction.replace(R.id.myFragment, fragment).commit();
        transaction.addToBackStack(null);  //buffer - removed from stack
        return true;
    }
}