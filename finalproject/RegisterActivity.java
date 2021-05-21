package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.finalproject.model.Data;
import com.example.finalproject.util.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static AppCompatEditText userText, emailText, contactText, passwordText, confirmText;
    private static AppCompatTextView showMsg;
    private static RadioGroup radioGrp;
    private static AppCompatRadioButton rb1,rb2,rb3;
    private AppCompatButton registerBtn;
    private static AppCompatSpinner spinnerUser;
    private String[] userType;
    private static String item = null;
    private static String gender;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_USERTYPE = "userType";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        userType = getResources().getStringArray(R.array.userType);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line,userType)//generic class
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

        spinnerUser.setPopupBackgroundDrawable(new ColorDrawable(Color.rgb(239,239,244)));

        spinnerUser.setAdapter(arrayAdapter);
        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//anonymous inner class
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    item = (String) parent.getItemAtPosition(position);
                    view.setBackground(new ColorDrawable(Color.parseColor("#EFEFF4")));
                }
                //Toast.makeText(getApplicationContext(),"Age: " + item,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registerBtn.setOnClickListener(this);
        radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = radioGrp.getCheckedRadioButtonId();
                switch (id){
                    case R.id.rb1: gender = rb1.getText().toString();
                                    break;
                    case R.id.rb2: gender = rb2.getText().toString();
                                    break;
                    case R.id.rb3: gender = rb3.getText().toString();
                        break;
                }
            }
        });
    }

    private static boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void init(){
        userText = findViewById(R.id.userText);
        emailText = findViewById(R.id.emailText);
        contactText = findViewById(R.id.contactText);
        radioGrp = findViewById(R.id.radioGrp);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        spinnerUser = findViewById(R.id.spinnerUser);
        passwordText = findViewById(R.id.passText);
        confirmText = findViewById(R.id.confirmText);
        registerBtn = findViewById(R.id.registerBtn);
        showMsg = findViewById(R.id.showMsg);
    }

    @Override
    public void onClick(View v) {
        if(v==registerBtn)
            registerUser();
    }

    public void registerUser(){
        Data data = new DAOClass().setData();
        stringRequest = new StringRequest(Request.Method.POST, Constants.REG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO",response);
                if(data!=null)
                {
                    if(response.equals("success")){
                        Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
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
                hashMap.put(KEY_NAME, data.getUsername());
                hashMap.put(KEY_EMAIL, data.getEmail());
                hashMap.put(KEY_CONTACT, data.getContact());
                hashMap.put(KEY_GENDER,data.getGender());
                hashMap.put(KEY_USERTYPE,data.getUserType());
                hashMap.put(KEY_PASSWORD, data.getPassword());
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
    }

    private static class DAOClass {
        public Data setData() {
            String uname = Objects.requireNonNull(userText.getText()).toString().trim();
            String email = Objects.requireNonNull(emailText.getText()).toString().trim();
            String contact = Objects.requireNonNull(contactText.getText()).toString().trim();
            String passwd = Objects.requireNonNull(passwordText.getText()).toString().trim();
            String confirmp = Objects.requireNonNull(confirmText.getText()).toString().trim();

            if (TextUtils.isEmpty(uname))
                userText.setError("Please Fill the Field");
            else if (TextUtils.isEmpty(email))
                emailText.setError("Please Fill the Field");
            else if(!validEmail(email))
                emailText.setError("Invalid Email");
            else if (TextUtils.isEmpty(contact))
                contactText.setError("Please Fill the Field");
            else if (contact.length() != 10)
                contactText.setError("Contact Number is Incorrect");
            else if(radioGrp.getCheckedRadioButtonId() == -1)
                showMsg.setError("Please Select Gender");
            else if (TextUtils.isEmpty(passwd))
                passwordText.setError("Please Fill the Field");
            else if (passwd.length() < 8)
                passwordText.setError("Password is too Short");
            else if (TextUtils.isEmpty(confirmp))
                confirmText.setError("Please Fill the Field");
            else if(!confirmp.equals(passwd))
                confirmText.setError("Password Doesn't Match");
            else {
                return new Data(uname, email, contact, gender, item, passwd);
            }
            return null;
        }
    }
}