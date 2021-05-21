package com.example.finalproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.Cases;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.MyHolder>{
    private Context context;
    private ArrayList<Cases> arrayList;

    public CasesAdapter(Context context,ArrayList<Cases> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CasesAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.covidcases,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CasesAdapter.MyHolder holder, int position) {
        Cases cases = arrayList.get(position);
        holder.currCase.setText(cases.getCurrCases());
        holder.recCase.setText(cases.getRecCases());
        holder.death.setText(cases.getDeaths());
    }

    @Override
    public int getItemCount() {
        Log.i("INFO", String.valueOf(arrayList.size()));
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        private AppCompatTextView currCase,recCase,death;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            currCase = itemView.findViewById(R.id.curCase);
            recCase = itemView.findViewById(R.id.recCases);
            death = itemView.findViewById(R.id.death);
        }
    }
}
