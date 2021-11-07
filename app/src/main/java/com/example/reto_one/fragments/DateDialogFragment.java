package com.example.reto_one.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.example.reto_one.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DateDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateDialogFragment extends DialogFragment {


    //State
    private int year;
    private int month;
    private int day;

    private OnDateSelectedListener listener;

    public DateDialogFragment() {
        Calendar c = Calendar.getInstance();
        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
        this.day = c.get(Calendar.DAY_OF_MONTH);
    }

    public static DateDialogFragment newInstance() {
        DateDialogFragment fragment = new DateDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_dialog_fragment, container, false);
        CalendarView datePicker = view.findViewById(R.id.datePicker);
        TimePicker timePicker = view.findViewById(R.id.timePicker);
        Button nextBtn = view.findViewById(R.id.nextBtn);

        //Para obtener los datos del calendar se debe manejar el evento
        datePicker.setOnDateChangeListener((root, year, month, dayOfMonth) -> {
            this.year = year;
            this.month = month;
            this.day = dayOfMonth;
        });

        //El boton de NEXT recoge toda la información en un objeto calendar y por medio
        //de observer, se envia al fragmento CreateEventFragment
        nextBtn.setOnClickListener(
                v->{
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, this.year);
                    calendar.set(Calendar.MONTH, this.month);
                    calendar.set(Calendar.DAY_OF_MONTH, this.day);
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());
                    listener.onDate(calendar.getTime().getTime());
                    this.dismiss();
                }
        );

        return view;
    }

    public void setListener(OnDateSelectedListener listener) {
        this.listener = listener;
    }

    //Callback
    interface OnDateSelectedListener{
        void onDate(long date);
    }
}