package com.example.finalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.MapsActivity;
import com.example.finalproject.R;
import com.example.finalproject.model.Data;

import java.util.ArrayList;

public class ListDoctorAdapter extends RecyclerView.Adapter<ListDoctorAdapter.MyHolder>{
    private Context context;
    private ArrayList<Data> arrayList;
    private static OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        ListDoctorAdapter.listener = listener;
    }

    public ListDoctorAdapter(Context context, ArrayList<Data> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ListDoctorAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_view,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListDoctorAdapter.MyHolder holder, int position) {
        Data data = arrayList.get(position);
        holder.doctorName.setText(data.getName());
        holder.doctorContact.setText(data.getPhone());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        private AppCompatTextView doctorName,doctorContact;
        SpannableString spannableString = new SpannableString("Content");

        public SpannableString getSpannableString() {
            spannableString.setSpan(new UnderlineSpan(),0,spannableString.length(),0);
            doctorName.setText(spannableString);
            return spannableString;
        }

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.listDoctorName);
            doctorContact = itemView.findViewById(R.id.doctorPhone);

            doctorName.setPaintFlags(doctorName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            getSpannableString();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            if(position==0){
                                listener.OnItemClick(position);
                            }
                        }
                    }
                }
            });
        }
    }
}
