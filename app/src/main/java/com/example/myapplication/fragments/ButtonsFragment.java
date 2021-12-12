package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.callbacks.CallBackMovePlayer;

public class ButtonsFragment extends Fragment {
    private ImageView panel_IMG_left_arrow;
    private ImageView panel_IMG_right_arrow;
    private AppCompatActivity activity;
    private CallBackMovePlayer callBackMovePlayer;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCallBackMovePlayer(CallBackMovePlayer callBackMovePlayer) {
        this.callBackMovePlayer = callBackMovePlayer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);
        findViews(view);
        initViews();
        return view;
    }


    private void initViews() {
        panel_IMG_right_arrow.setOnClickListener(v -> {
            if (callBackMovePlayer != null) {
                callBackMovePlayer.movePlayer(true);
            }
        });
        panel_IMG_left_arrow.setOnClickListener(v -> {
            if (callBackMovePlayer != null) {
                callBackMovePlayer.movePlayer(false);
            }
        });
    }

    private void findViews(View view) {
        panel_IMG_left_arrow = view.findViewById(R.id.panel_IMG_left_arrow);
        panel_IMG_right_arrow = view.findViewById(R.id.panel_IMG_right_arrow);
    }
}