package com.example.hansani.personal_information_management;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hansani.personal_information_management.db.connection.DBHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import roboguice.inject.InjectView;

public class ToDoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @InjectView(R.id.spinner_day_list)
    private Spinner spinner_day_list;

    @InjectView(R.id.spinner_time)
    private Spinner spinner_time_from;

    @InjectView(R.id.spinner_time_to)
    private Spinner spinner_time_to;

    @InjectView(R.id.venue)
    private String venue;

    private String day;
    private String timeFrom;
    private String timeTo;

    private DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);


    }

    public void onClick(View view) {
        dbHandler.updateVenue(day,timeFrom,timeTo,venue);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_day_list:
                day = spinner_day_list.getSelectedItem().toString();
                break;
            case R.id.spinner_time:
                timeFrom = spinner_time_from.getSelectedItem().toString();
                break;
            case R.id.spinner_time_to:
                timeTo = spinner_time_to.getSelectedItem().toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}
