package com.example.finalproject.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.UserAddActivity;
import com.example.finalproject.model.Data;
import com.example.finalproject.model.UserInputData;

import java.util.ArrayList;

public class UserInput extends RecyclerView.Adapter<UserInput.MyHolder> {
    private Context context;
    private static SharedPreferences sharedPreferences;
    private ArrayList<UserInputData> arrayList;

    public UserInput(Context context, ArrayList<UserInputData> arrayList) {
        sharedPreferences = context.getSharedPreferences("mypref",Context.MODE_PRIVATE);
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public UserInput.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usershow,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserInput.MyHolder holder, int position) {
        UserInputData userInputData = arrayList.get(position);
        holder.userName.setText(userInputData.getName());
        holder.userSuggest.setText(userInputData.getSuggestion());
        holder.userLocation.setText(userInputData.getLocation());
        holder.userDate.setText(userInputData.getDate());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        private AppCompatTextView userName,userSuggest,userLocation,userDate;
        private AppCompatButton userRate;
        private AppCompatRatingBar userStar;
        private String value;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userCName);
            userSuggest = itemView.findViewById(R.id.userCSuggestion);
            userLocation = itemView.findViewById(R.id.userCLocation);
            userDate = itemView.findViewById(R.id.userCDate);
            userStar = itemView.findViewById(R.id.userRate);

            userStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    value = String.valueOf(rating);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("points",value);
                    editor.commit();
                    editor.apply();
                    Log.i("INFO",value);
                }
            });
        }
    }
}
