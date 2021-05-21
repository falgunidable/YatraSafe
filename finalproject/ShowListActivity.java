package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

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
import android.widget.Toast;

import com.example.finalproject.adapter.MyAdapter;

public class ShowListActivity extends AppCompatActivity {
    private AppCompatTextView nameText;
    private ListView listView;
    public String[] items;
    private Integer imgs[] = {R.drawable.patient, R.drawable.risk, R.drawable.bed, R.drawable.blocked, R.drawable.doctor,
            R.drawable.growth, R.drawable.checklist};
    private MyAdapter adapter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#02025C"));
        getSupportActionBar().setBackgroundDrawable(drawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nameText = findViewById(R.id.nameText);
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        items = getResources().getStringArray(R.array.items);
        listView = findViewById(R.id.listView);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter = new MyAdapter(items, imgs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ShowListActivity.this,items[position],Toast.LENGTH_SHORT).show();
                if(position == 0){
                    Intent intent = new Intent(getApplicationContext(),CovidCases.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else if(position == 4){
                    Intent intent = new Intent(getApplicationContext(),DoctorList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else if(position == 6){
                    Intent intent = new Intent(getApplicationContext(),CheckLastActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            nameText.setText(bundle.getCharSequence("name"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.list_show_view, menu);
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
        return true;
    }
}