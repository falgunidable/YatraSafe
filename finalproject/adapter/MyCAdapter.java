package com.example.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.StatesCities;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyCAdapter extends RecyclerView.Adapter<MyCAdapter.MyHolder> {
    private Context context;
    private ArrayList<StatesCities> arrayList;
    private static OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        MyCAdapter.listener = listener;
    }

    public MyCAdapter(Context context,ArrayList<StatesCities> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyCAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycardviewcities,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCAdapter.MyHolder holder, int position) {
        StatesCities statesCities = arrayList.get(position);
        Picasso.get().load(statesCities.getImage()).centerCrop().fit().noFade().into(holder.cityImage);
        holder.cityText.setText(statesCities.getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        private AppCompatImageView cityImage;
        private AppCompatTextView cityText;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cityImage = itemView.findViewById(R.id.cityImage);
            cityText = itemView.findViewById(R.id.cityName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                                listener.OnItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
