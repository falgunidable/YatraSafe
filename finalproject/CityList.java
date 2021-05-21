package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.adapter.MyCAdapter;
import com.example.finalproject.model.StatesCities;
import com.example.finalproject.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CityList extends AppCompatActivity implements MyCAdapter.OnItemClickListener{
    private SharedPreferences preferences;
    private RecyclerView recyclerView;
    private MyCAdapter adapter;
    private ArrayList<StatesCities> arrayList;
    private ProgressDialog progressDialog;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#02025C"));
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        arrayList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.logIn);
        Log.i("INFO", String.valueOf(menuItem.getTitle()));
        if(preferences.getString("email","").equals("")) {
            menuItem.setTitle("SIGN IN/ SIGN UP ");
            Log.i("INFO", String.valueOf(menuItem));
        }else{
            menuItem.setTitle("");
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        stringRequest  = new StringRequest(Request.Method.GET, Constants.API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("cities");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jOBj = jsonArray.getJSONObject(i);
                        StatesCities mydata = new StatesCities(jOBj.getString("name"),jOBj.getString("image"));
                        arrayList.add(mydata);
                    }
                    adapter = new MyCAdapter(CityList.this,arrayList);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(CityList.this);
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
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return true;
    }

    @Override
    public void OnItemClick(int position) {
        Bundle bundle = new Bundle();
        StatesCities statesCities = arrayList.get(position);
        if(position==3){
        Intent intent = new Intent(CityList.this,ShowListActivity.class);
        bundle.putString("name",statesCities.getName());
        intent.putExtras(bundle);
        startActivity(intent);}
        else if(position==6){
            Intent intent = new Intent(CityList.this,NashikView.class);
            bundle.putString("name",statesCities.getName());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}