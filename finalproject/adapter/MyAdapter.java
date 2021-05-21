package com.example.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.StatesCities;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    String[] items;
    public Integer img[] = {R.drawable.patient,R.drawable.risk,R.drawable.bed,R.drawable.blocked,R.drawable.doctor,
            R.drawable.growth,R.drawable.checklist};
    Integer[] icons;
    private Context context;

    public MyAdapter(){}

    public MyAdapter(String[] items,Integer[] icons)
    {
        this.items = items;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mylist,parent,false);
        AppCompatImageView imageView = view.findViewById(R.id.iconImg);
        AppCompatTextView textView = view.findViewById(R.id.name);
        imageView.setImageResource(icons[position]);
        textView.setText(items[position]);
        return view;
    }
}
