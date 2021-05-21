package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.adapter.MyAdapter;

public class NashikView extends AppCompatActivity {
    private TextView cityName;
    private ListView listView1;
    public String[] items;
    private Integer imgs[] = {R.drawable.patient, R.drawable.risk, R.drawable.bed, R.drawable.blocked, R.drawable.doctor,
            R.drawable.growth, R.drawable.checklist};
    private MyAdapter adapter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nashik_view);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#02025C"));
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cityName = findViewById(R.id.cityText);
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        items = getResources().getStringArray(R.array.items);
        listView1 = findViewById(R.id.listView1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new MyAdapter(items, imgs);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(NashikView.this,items[position],Toast.LENGTH_SHORT).show();
                if(position == 0){
                    Intent intent = new Intent(getApplicationContext(),CasesNashik.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else if(position == 4){
                    Intent intent = new Intent(getApplicationContext(),DoctorList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                if(position == 6){
                    Intent intent = new Intent(getApplicationContext(),CheckLastActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            cityName.setText(bundle.getCharSequence("name"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
//        if(id == R.id.action_logout)
//        {
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.remove("email");
//            editor.remove("password");
//            editor.clear();
//            editor.apply();
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            finish();
//        }
        return true;
    }
}