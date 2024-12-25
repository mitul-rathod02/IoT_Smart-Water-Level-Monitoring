package com.example.swlm;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;

public class welcome extends AppCompatActivity {

    TextView txtWaterLevel, txtDateTime;
    Button btnPrevious;
    DatabaseReference databaseReference;

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        txtWaterLevel = findViewById(R.id.txtWaterLevel);
        txtDateTime = findViewById(R.id.txtDateTime);
        btnPrevious = findViewById(R.id.btnPrevious);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("water_level");

        // Listen for changes in water level data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get water level and datetime from Firebase
                    Double waterLevel = snapshot.child("level").getValue(Double.class);
                    String datetime = snapshot.child("datetime").getValue(String.class);

                    // Debug logs
                    Log.d(TAG, "Water Level: " + waterLevel);
                    Log.d(TAG, "Datetime: " + datetime);

                    // Update UI with fetched water level
                    updateWaterLevelDisplay(waterLevel, datetime);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error

                Log.e(TAG, "Database Error: " + error.getMessage());
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(welcome.this, view_water_level.class);
                startActivity(i);
            }
        });
    }

    private void updateWaterLevelDisplay(Double waterLevel, String datetime) {
        if (waterLevel != null && datetime != null) {
            // Update progress bar and text view with the current water level and datetime
            txtWaterLevel.setText("Water Level: " + waterLevel + "%");
            txtDateTime.setText("Date and time: \n" + datetime);
        }
    }

       /* // Listen for changes in water level data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get water level and datetime from Firebase
                    Double waterLevel = snapshot.child("level").getValue(Double.class);
                    String datetime = snapshot.child("datetime").getValue(String.class);

                    // Update UI with fetched water level
                    updateWaterLevelDisplay(waterLevel, "datetime");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(welcome.this, view_water_level.class);
                startActivity(i);
            }
        });

    }
    private void updateWaterLevelDisplay(Double waterLevel, String datetime) {
        if (waterLevel != null && datetime != null) {
            // Update progress bar and text view with the current water level
            // progressBar.setProgress(waterLevel.intValue());
            txtWaterLevel.setText("Water Level: " + waterLevel + "%");
            txtDateTime.setText("Date and time: " + datetime);
            // Change progress bar color to red when water level is equal to or less than 10
            /*if (waterLevel <= 10) {
                progressBar.setProgressTintList(getResources().getColorStateList(android.R.color.holo_red_light));
            } else {
                // Reset progress bar color
                progressBar.setProgressTintList(null);
            }*/
       //}

}
