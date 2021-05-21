package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

public class CheckLastActivity extends AppCompatActivity {
    private AppCompatButton checkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_last);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#02025C"));
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkBtn = findViewById(R.id.checkBtn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==checkBtn){
                    Intent intent = new Intent(getApplicationContext(),CovidCheckActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}