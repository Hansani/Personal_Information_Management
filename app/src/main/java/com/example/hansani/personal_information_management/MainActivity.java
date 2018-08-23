package com.example.hansani.personal_information_management;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hansani.personal_information_management.db.connection.DBHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import roboguice.inject.InjectView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHandler dbHandler = new DBHandler(this);
        File database = getApplicationContext().getDatabasePath(DBHandler.DB_NAME);
        if (!database.exists()) {
            dbHandler.getWritableDatabase();
            if (copyDatabase(this)) {
                Toast.makeText(this, "copy database successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "copy database failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void pressedUpdate(View view) {
        Intent intent = new Intent(this, ToDoActivity.class);
        startActivity(intent);
    }

    public void pressedStart(View view) {
        new LocationTrack(this, this);
    }

    public boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DBHandler.DB_NAME);
            String outFileName = DBHandler.LOCATION + DBHandler.DB_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] bytes = new byte[1024];
            int n;
            while ((n = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, n);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
