package com.example.finalproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.finalproject.DoctorActivity;
import com.example.finalproject.R;
import com.example.finalproject.VolleySingleton;
import com.example.finalproject.adapter.MyAdapterCovid;
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

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static final int RESULT_OK = -1;
    private AppCompatTextView doctor_Name, doctor_Email, doctor_Contact;
    private AppCompatImageView doctorImage;
    private AppCompatButton uploadBtn;
    private SharedPreferences preferences;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private final String  KEY_EMAIL = "email";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_CONTACT = "contact";
    public static final int PICK_IMAGE_REQUEST =1;
    private MyAdapterCovid myAdapterCovid;
    private ArrayList<Data> arrayList;
    private RecyclerView recyclerView1;
    private View view;
    private Bitmap bitmap;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        preferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView1.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView1.setHasFixedSize(true);
        arrayList = new ArrayList<>();

        doctor_Name = view.findViewById(R.id.doctorName);
        doctor_Email = view.findViewById(R.id.doctorEmail);
        doctor_Contact = view.findViewById(R.id.doctorContact);
        uploadBtn = view.findViewById(R.id.uploadBtn);
        doctorImage = view.findViewById(R.id.doctorPhoto);

        uploadBtn.setOnClickListener(this);
        doctorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        loadData();
        displayData();
        fetchImage();
        doctor_Name.setText(preferences.getString("name",""));
        doctor_Email.setText(preferences.getString("email",""));
        doctor_Contact.setText(preferences.getString("contact",""));
        return view;
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
                doctorImage.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                doctorImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void displayData(){
        StringRequest stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, Constants.DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO",response);
                Toast.makeText(getActivity(),"data",Toast.LENGTH_SHORT).show();
                try{ JSONObject jsonObject = new JSONObject(response);
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
        VolleySingleton volleySingleton = VolleySingleton.getInstance(getActivity());
        volleySingleton.addToRequestQueue(stringRequest);
    }

    public void loadData(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.PATIENT_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO",response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jOBj = jsonArray.getJSONObject(i);
                        Data mdata = new Data(jOBj.getString("fname"),jOBj.getString("contact"),
                                jOBj.getString("age"));

                        Log.i("INFO", String.valueOf(mdata.getName()));
                        arrayList.add(mdata);
                    }
                    myAdapterCovid = new MyAdapterCovid(getActivity(),arrayList);
                    recyclerView1.setAdapter(myAdapterCovid);
                    //myAdapterCovid.setOnItemClickListener(LoginActivity.this);
                }catch (JSONException e){
                    Log.e("JSON",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton volleySingleton = VolleySingleton.getInstance(getActivity());
        volleySingleton.addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v==uploadBtn)
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
                        Toast.makeText(getActivity(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
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
            singleton = VolleySingleton.getInstance(getActivity());
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
                    Picasso.get().load(jsonArray.getJSONObject(0).getString("image")).fit().into(doctorImage);
//                    String status = jsonArray.getJSONObject(0).getString("image");
//                    boolean val =Boolean.parseBoolean(status);
//                    if(val) {
//                        Picasso.get().load(jsonArray.getJSONObject(0).getString("image")).fit().into(doctorImage);
//                    }else{
//                        doctorImage.setImageResource(R.mipmap.ic_launcher);
//                    }
                }catch (JSONException e){
                    Log.e("ERROR",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
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
        singleton = VolleySingleton.getInstance(getActivity());
        singleton.addToRequestQueue(stringRequest);
    }
}