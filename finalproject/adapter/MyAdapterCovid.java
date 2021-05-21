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
import com.example.finalproject.model.Data;

import java.util.ArrayList;

public class MyAdapterCovid extends RecyclerView.Adapter<MyAdapterCovid.MyHolder> {
    private Context context;
    private ArrayList<Data> arrayList;
    private static OnItemClickListener listener;
    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        MyAdapterCovid.listener = listener;
    }

    public MyAdapterCovid(Context context, ArrayList<Data> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public com.example.finalproject.adapter.MyAdapterCovid.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patientregistercard,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.finalproject.adapter.MyAdapterCovid.MyHolder holder, int position) {
        Data data = arrayList.get(position);
        holder.patientName.setText(data.getName());
        holder.patientContact.setText(data.getPhone());
        holder.patientAge.setText(data.getAge());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        private AppCompatTextView patientName,patientContact,patientAge;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patientName);
            patientContact = itemView.findViewById(R.id.patientContact);
            patientAge = itemView.findViewById(R.id.patientAge);
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

