package com.example.finalproject.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.LoginActivity;
import com.example.finalproject.MainActivity;
import com.example.finalproject.R;

public class ListFragment extends Fragment {
    private AppCompatButton logout;
    private SharedPreferences preferences;
    private View view;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);
        preferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == logout)
                {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("email");
                    editor.remove("password");
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}