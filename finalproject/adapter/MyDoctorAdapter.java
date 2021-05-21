package com.example.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.model.Data;

import java.util.ArrayList;

public final class MyDoctorAdapter extends RecyclerView.Adapter<MyDoctorAdapter.MyHolder> {
        private Context context;
        private ArrayList<Data> arrayList;
        private static MyDoctorAdapter.OnItemClickListener listener;
        public interface OnItemClickListener{
            void OnItemClick(int position);
        }

        public void setOnItemClickListener(MyDoctorAdapter.OnItemClickListener listener){
            MyDoctorAdapter.listener = listener;
        }

    public MyDoctorAdapter(Context context, ArrayList<Data> arrayList){
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public com.example.finalproject.adapter.MyDoctorAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctorlist,parent,false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.finalproject.adapter.MyDoctorAdapter.MyHolder holder, int position) {
            Data data = arrayList.get(position);
            holder.doctorlName.setText(data.getName());
            holder.doctorlEmail.setText(data.getEmail());
            holder.doctorlContact.setText(data.getPhone());
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public static class MyHolder extends RecyclerView.ViewHolder{
            private AppCompatTextView doctorlName,doctorlEmail,doctorlContact;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                doctorlName = itemView.findViewById(R.id.doctorlName);
                doctorlEmail = itemView.findViewById(R.id.doctorlEmail);
                doctorlContact = itemView.findViewById(R.id.doctorlPhone);
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
