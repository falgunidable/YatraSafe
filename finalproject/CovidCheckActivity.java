package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.finalproject.model.Data;
import com.example.finalproject.util.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CovidCheckActivity extends AppCompatActivity implements View.OnClickListener{
    private static AppCompatEditText name_Text, contact_Text;
    @SuppressLint("StaticFieldLeak")
    private static RadioGroup radioGrpG,quarantineRgb;
    private static AppCompatRadioButton rb1,rb2,rb3,rb4,rb5;
    private static AppCompatCheckBox cb1,cb2,cb3,cb4,cb5;
    private AppCompatButton submitBtn;
    private static String gender;
    private static String quarantine_;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private static final String KEY_NAME = "fname";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_AGE = "age";
    private static final String KEY_QUARANTINE= "qurantine";
    private static final String KEY_SYMPTOMS = "symptoms";
    private AppCompatSpinner spinner;
    private String[] ages;
    private static String item = null;
    private static String list="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_check);
        getSupportActionBar().hide();
        ages = getResources().getStringArray(R.array.age);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CovidCheckActivity.this, android.R.layout.simple_dropdown_item_1line,ages)//generic class
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

        spinner.setPopupBackgroundDrawable(new ColorDrawable(Color.rgb(242,243,248)));

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//anonymous inner class
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    item = (String) parent.getItemAtPosition(position);
                }
                Toast.makeText(getApplicationContext(),"Age: " + item,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitBtn.setOnClickListener(this);
        radioGrpG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = radioGrpG.getCheckedRadioButtonId();
                switch (id){
                    case R.id.malerb1: gender = rb1.getText().toString();
                        break;
                    case R.id.femalerb2: gender = rb2.getText().toString();
                        break;
                    case R.id.otherrb3: gender = rb3.getText().toString();
                        break;
                    case R.id.yesrb1: quarantine_ = rb4.getText().toString();
                        break;
                    case R.id.hospital: quarantine_ = rb5.getText().toString();
                        break;
                }
            }
        });
        quarantineRgb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id_ = quarantineRgb.getCheckedRadioButtonId();
                switch (id_){
                    case R.id.yesrb1: quarantine_ = rb4.getText().toString();
                        break;
                    case R.id.hospital: quarantine_ = rb5.getText().toString();
                        break;
                }
            }
        });
    }

    public void init(){
        name_Text = findViewById(R.id.fullName);
        contact_Text = findViewById(R.id.contactNo);
        radioGrpG = findViewById(R.id.genderCovid);
        quarantineRgb = findViewById(R.id.quarantineCovid);
        rb1 = findViewById(R.id.malerb1);
        rb2 = findViewById(R.id.femalerb2);
        rb3 = findViewById(R.id.otherrb3);
        rb4 = findViewById(R.id.yesrb1);
        rb5 = findViewById(R.id.hospital);
        spinner = findViewById(R.id.spinner);
        submitBtn = findViewById(R.id.submit);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
    }

    @Override
    public void onClick(View v) {
        if(v==submitBtn) {
            registerCUser();
        }
    }

    public void registerCUser(){
        Data data = new CovidCheckActivity.DAOClass().sendData();
        stringRequest = new StringRequest(Request.Method.POST, Constants.REG_COVID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO",response);
                if(data!=null)
                {
                    if(response.equals("success")){
                        Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), CheckLastActivity.class);
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
                hashMap.put(KEY_NAME, data.getName());
                hashMap.put(KEY_CONTACT, data.getPhone());
                hashMap.put(KEY_GENDER,data.getGender_());
                hashMap.put(KEY_AGE,data.getAge());
                hashMap.put(KEY_QUARANTINE,data.getQuarantine());
                hashMap.put(KEY_SYMPTOMS,data.getSymptoms());
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
    }

    private static class DAOClass {
        public Data sendData() { Data data = new Data();
            String fname = name_Text.getText().toString().trim();
            String contact = contact_Text.getText().toString().trim();
            
            if(cb1.isChecked())
                list = list + ", " + cb1.getText().toString();
            if(cb2.isChecked())
                list = list + ", " + cb2.getText().toString();
            if(cb3.isChecked())
                list = list + ", " + cb3.getText().toString();
            if(cb4.isChecked())
                list = list + ", " + cb4.getText().toString();
            if(cb5.isChecked())
                list = list + ", " + cb5.getText().toString();

            if (TextUtils.isEmpty(fname))
                name_Text.setError("Please Fill the Field");
            else if (TextUtils.isEmpty(contact))
                contact_Text.setError("Please Fill the Field");
            else if (contact.length() != 10)
                contact_Text.setError("Contact Number is Incorrect");
            else { Log.i("INFO",fname + "" + contact + "" + gender + " " + item + " " + quarantine_+" "+list);
                data.setName(fname);
                data.setPhone(contact);
                data.setGender_(gender);
                data.setAge(item);
                data.setQuarantine(quarantine_);
                data.setSymptoms(list);
                return data;//return new Data(fname, contact, gender, item, quarantine_,list);
            }
            return null;
        }
    }
}