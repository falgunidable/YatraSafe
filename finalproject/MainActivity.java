package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SearchView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.finalproject.util.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private SharedPreferences preferences;
    private TextView email_Text,name_Text;
    private AppCompatImageView imageView,navImg;
    private AppCompatButton nearBy;
    private SearchView searchView;
    private ListView listView;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#02025C"));
        getSupportActionBar().setBackgroundDrawable(drawable);
        invalidateOptionsMenu();
        imageView = findViewById(R.id.gifImg);
        Glide.with(this).load("https://media1.s-nbcnews.com/i/newscms/2020_49/1647192/mask-upgrade-main-khu_e51d0cb94caaee48198f4f380c49bb98.gif").into(imageView);

        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.searchList);

        list = new ArrayList<>();
        list.add("Andhra Pradesh");
        list.add("Assam");
        list.add("Bihar");
        list.add("Chhatisgarh");
        list.add("Goa");
        list.add("Karnataka");
        list.add("Madhya Pradesh");
        list.add("Maharashtra");
        list.add("Manipur");
        list.add("Rajashthan");
        list.add("Tamil Nadu");
        list.add("Uttar Pradesh");

        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        nearBy = findViewById(R.id.nearBy);

        FloatingActionButton fab = findViewById(R.id.fab);
        email_Text = findViewById(R.id.andEmail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        name_Text = navigationView.getHeaderView(0).findViewById(R.id.andName);
        email_Text = navigationView.getHeaderView(0).findViewById(R.id.andEmail);
        navImg = navigationView.getHeaderView(0).findViewById(R.id.profileView);

        name_Text.setText(preferences.getString("name",""));
        email_Text.setText(preferences.getString("email",""));

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        //listView.setBackgroundColor(Color.BLACK);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    Intent intent = new Intent(getApplicationContext(),CityList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    listView.setVisibility(View.INVISIBLE);
                }
                else {
                    Toast.makeText(MainActivity.this, "Data Not Availablle",Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(getApplicationContext(),"No Match Found",Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)) {
                    listView.clearTextFilter();
                    listView.setVisibility(View.GONE);
                }
                else if(searchView.hasFocus()) {
                    listView.setVisibility(View.VISIBLE);
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        nearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==nearBy){
                    Intent intent = new Intent(getApplicationContext(),UserAddActivity.class);
                    startActivity(intent);
                }
            }
        });
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.logIn){
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if(id==R.id.nav_home){
            intent = new Intent(getApplicationContext(),MainActivity.class);
        }else if(id == R.id.nav_profile){
            intent = new Intent(getApplicationContext(),AdminProfile.class);
        }
        else if(id == R.id.allLogout){
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("email");
            editor.remove("password");
            editor.clear();
            editor.apply();
            intent = new Intent(getApplicationContext(), MainActivity.class);
            finish();
        }
        else {
            intent = new Intent();
        }
        startActivity(intent);
        return true;
    }
}