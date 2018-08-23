package com.example.hansani.personal_information_management;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.hansani.personal_information_management.db.connection.DBHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Hansani on 8/22/2018.
 */

public class LocationTrack implements LocationListener {

    private final Context appContext;
    private Activity activityContext;

    private LocationManager locationManager;
    private Location location;
    private DBHandler dbhandler;

    String time_From;
    String time_To;
    String day;

    public LocationTrack(Context appContext, Activity activityContext) {
        this.appContext = appContext;
        this.activityContext = activityContext;
        this.locationManager = (LocationManager) appContext.getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activityContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            return;
        }

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        if (location != null) {
            onLocationChanged(location);
        } else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            activityContext.startActivity(intent);
        }
        // location updates: at least 1 meter and 200millsecs change
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 1, this);

    }


    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(activityContext, "Location changed!", Toast.LENGTH_SHORT).show();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf_time = new SimpleDateFormat("HH.MM");
        String currentTimeS = sdf_time.format(currentTime);
        SimpleDateFormat sdf_day = new SimpleDateFormat("EEEE");
        Date d = new Date();
        day = sdf_day.format(d);
        String[] time_array = appContext.getResources().getStringArray(R.array.spinner_time);
        float time_P = (float) 0.00;
        float currentTimeI = Float.parseFloat(currentTimeS);
        for (String time : time_array) {
            Float timeI = Float.parseFloat(time);
            if (currentTimeI > timeI) {
                time_P = timeI;
                continue;
            } else if (currentTimeI < timeI) {
                Float time_F = time_P;
                Float time_T = timeI;

                time_From = String.format("%.02f", time_F);
                time_To = String.format("%.02f", time_T);
                break;
            }
        }

        dbhandler = new DBHandler(appContext);
        String venue = dbhandler.getVenue(day, time_From, time_To);
        Geocoder geocoder = new Geocoder(appContext, Locale.getDefault());
        double latitude = location.getLatitude();
        double longatude = location.getLongitude();
        try {
            List<Address> address = geocoder.getFromLocation(latitude, longatude, 1);
            String addressStr = address.get(0).getAddressLine(0);
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }



        Log.d("DATE&TIME", "day=%s, time=%s");


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Toast.makeText(activityContext, locationManager.GPS_PROVIDER + "'s status changed to !",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(activityContext, "Provider " + locationManager.GPS_PROVIDER + " enabled!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(activityContext, "Provider " + locationManager.GPS_PROVIDER + " disabled!", Toast.LENGTH_SHORT).show();
    }
}
