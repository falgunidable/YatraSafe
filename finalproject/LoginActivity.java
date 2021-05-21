package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.finalproject.adapter.MyAdapterCovid;
import com.example.finalproject.model.Data;
import com.example.finalproject.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static AppCompatEditText loginEmail,loginPassword;
    private AppCompatTextView registerText;
    private AppCompatButton signInBtn;
    private SharedPreferences preferences;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TYPE = "usertype";
    private AppCompatSpinner userType;
    private String[] user;
    private static String item=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        getSupportActionBar().hide();
        user = getResources().getStringArray(R.array.userType);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        signInBtn.setOnClickListener(this);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line,user)//generic class
        {
            @Override
            public boolean isEnabled(int position) {
                if(position > 0)
                    return true;
                else
                    return false;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        //userType.setPopupBackgroundDrawable(new ColorDrawable(Color.rgb(161,179,246)));

        userType.setAdapter(arrayAdapter);
        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//anonymous inner class
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    item = (String) parent.getItemAtPosition(position);
                    view.setBackground(new ColorDrawable(Color.parseColor("#EAEBF3")));
                }
                //Toast.makeText(getApplicationContext(),"User Type: " + item,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void init(){
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        registerText = findViewById(R.id.registerText);
        signInBtn = findViewById(R.id.signInBtn);
        userType = findViewById(R.id.userType);
    }

    private static boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void onClick(View v) {
        if(v==signInBtn) {
            loginUser();
            //loadData();
        }
    }

    public void loginUser(){
        Data data = new DAOClass().setData();
        stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, Constants.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("INFO",response);
                        if(data!=null)
                        {
                            if(response.equals("success")){
                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("email", data.getEmail());
                                editor.putString("password", data.getPassword());
                                editor.putString("usertype",data.getUserType());
                                editor.apply();
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }else if(response.equals("doctor success")){
                                Toast.makeText(getApplicationContext(),"Doctor Login Successful",Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = preferences.edit();
                                String uname = preferences.getString("name",data.getUsername());
                                editor.putString("email", data.getEmail());
                                editor.putString("password",data.getPassword());
                                editor.apply();
                                editor.commit();
                                Intent i =new Intent(getApplicationContext(),DoctorActivity.class);
                                i.putExtra("name",uname);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR",error.toString());
                //Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(KEY_EMAIL, data.getEmail());
                hashMap.put(KEY_PASSWORD, data.getPassword());
                hashMap.put(KEY_TYPE,data.getUserType());
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
    }

    public static class DAOClass
    {
        public Data setData()
        {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();
            if(TextUtils.isEmpty(email))
                loginEmail.setError("Please Fill the Field");
            else if(!validEmail(email))
                loginEmail.setError("Invalid Email");
            else if(TextUtils.isEmpty(password))
                loginPassword.setError("Please Fill the Field");
            else if(password.length()<8)
                loginPassword.setError("Password is too short");
            else{
                Data data = new Data();
                data.setEmail(email);
                data.setPassword(password);
                data.setUserType(item);
                return data;
                //return new Data(email,password,usertype);
            }
            return null;
        }
    }
}