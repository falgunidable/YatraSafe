package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {
    private AppCompatImageView splash;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        SharedPreferences preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        email = preferences.getString("email","");
        password = preferences.getString("password","");

        splash = findViewById(R.id.splash);

        Bitmap imageBitmap= BitmapFactory.decodeResource(getResources(),  R.drawable.screen);
        RoundedBitmapDrawable roundedBitmapDrawable=
                RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
        roundedBitmapDrawable.setCornerRadius(500.0f);
        roundedBitmapDrawable.setAntiAlias(true);
        splash.setImageDrawable(roundedBitmapDrawable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
//                if(!email.equals("") && !password.equals(""))
//                {
//                    intent = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(intent);
//                    finish();
//              }
//                else {
//                    intent = new Intent(getApplicationContext(), MainActivity.class);
//                }
            }
        }, 3500);
    }
}