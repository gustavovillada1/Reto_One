package com.example.reto_one.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.reto_one.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateEventFragment extends Fragment {


    //STATE
    private long startDate;
    private long endDate;

    //UI
    public Button startBtn;
    public Button endBtn;



    public CreateEventFragment() {
        // Required empty public constructor
    }

    public static CreateEventFragment newInstance() {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        startBtn = view.findViewById(R.id.startBtn);
        endBtn = view.findViewById(R.id.endBtn);
        startBtn.setOnClickListener(this::defineStartDate);
        endBtn.setOnClickListener(this::defineEndDate);
        return view;
    }

    private void defineStartDate(View view) {
        showDatePicker(date->{
            startDate = date;
            startBtn.setText(formatDate(startDate));
        });
    }

    private String formatDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(new Date(date));
    }

    private void defineEndDate(View view) {
        showDatePicker(date -> {
            endDate = date;
            endBtn.setText(formatDate(endDate));

        });
    }

    private void showDatePicker(DateDialogFragment.OnDateSelectedListener listener) {
        DateDialogFragment dialog = new DateDialogFragment();
        dialog.setListener(listener);
        dialog.show(getActivity().getSupportFragmentManager(), "dialog");
    }



}