package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.finalproject.adapter.MyAdapterCovid;
import com.example.finalproject.adapter.MyDoctorAdapter;
import com.example.finalproject.model.Data;
import com.example.finalproject.util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminProfile extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    private AppCompatTextView adminName, adminEmail, adminContact, userPoint;
    private AppCompatButton updateBtn;
    private AppCompatImageView profileImg;
    private SharedPreferences preferences;
    private final String  KEY_EMAIL = "email";
    private final String  KEY_TYPE = "usertype";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CONTACT = "contact";
    private MyDoctorAdapter doctorAdapter;
    private ArrayList<Data> doctor;
    private RecyclerView recyclerView2;
    private Bitmap bitmap;
    private StringRequest stringRequest;
    private VolleySingleton singleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#02025C"));
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new GridLayoutManager(this,1));
        recyclerView2.setHasFixedSize(true);
        doctor = new ArrayList<>();

        init();
        displayData();
        fetchImage();

        updateBtn.setOnClickListener(this);
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        userPoint.setText("0");
    }

    public void init(){
        adminName = findViewById(R.id.adminName);
        adminEmail = findViewById(R.id.adminEmail);
        adminContact = findViewById(R.id.adminContact);
        updateBtn = findViewById(R.id.displayList);
        profileImg = findViewById(R.id.profileImage);
        userPoint = findViewById(R.id.point);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adminName.setText(preferences.getString("name",""));
        adminEmail.setText(preferences.getString("email",""));
        adminContact.setText(preferences.getString("contact",""));
        userPoint.setText(preferences.getString("points",""));
        loadData();
    }

    public void displayData(){
        StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, Constants.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    JSONObject jOBj = jsonArray.getJSONObject(0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name",jOBj.getString("name"));
                    editor.putString("contact",jOBj.getString("contact"));
                    editor.apply();
                    editor.commit();
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
                hashMap.put(KEY_EMAIL, preferences.getString("email",""));
                return hashMap;
            }
        };
        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        volleySingleton.addToRequestQueue(stringRequest);
    }

    public void loadData(){
        StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, Constants.DOCTOR_LIST, new Response.Listener<String>() {
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
                        data_.setEmail(jOBj.getString("email"));
                        data_.setPhone(jOBj.getString("contact"));
                        Log.i("INFO", String.valueOf(data_.getName()));
                        doctor.add(data_);
                    }
                    doctorAdapter = new MyDoctorAdapter(AdminProfile.this,doctor);
                    recyclerView2.setAdapter(doctorAdapter);
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

    public void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST);
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                profileImg.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                profileImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(v==updateBtn)
            updateUser();
    }

    public void updateUser(){
        String imgurl = getStringImage(bitmap);
        if(imgurl != null){
            stringRequest = new StringRequest(Request.Method.POST, Constants.IMAGE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("INFO",response);
                    if(response.equals("success"))
                    {
                        Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR",error.toString());
                }
            })
            {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put(KEY_CONTACT,preferences.getString("contact",""));
                    hashMap.put(KEY_EMAIL,preferences.getString("email",""));
                    hashMap.put(KEY_IMAGE,imgurl);
                    return hashMap;
                }
            };
            singleton = VolleySingleton.getInstance(this);
            singleton.addToRequestQueue(stringRequest);
        }
    }

    private void fetchImage(){
        stringRequest = new StringRequest(Request.Method.POST, Constants.FETCH_IMAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Picasso.get().load(jsonArray.getJSONObject(0).getString("image")).fit().into(profileImg);
//                    String status = jsonArray.getJSONObject(0).getString("image");
//                    boolean val =Boolean.parseBoolean(status);
                  //  if(val) {
                        Picasso.get().load(jsonArray.getJSONObject(0).getString("image")).fit().into(profileImg);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("image",status);
//                        editor.commit();
//                        editor.apply();
                    //}else{
//                        profileImg.setImageResource(R.mipmap.ic_launcher);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("image",status);
//                        editor.commit();
//                        editor.apply();
                    //}
                }catch (JSONException e){
                    Log.e("ERROR",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(KEY_EMAIL,preferences.getString("email",""));
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(getApplicationContext());
        singleton.addToRequestQueue(stringRequest);
    }
}