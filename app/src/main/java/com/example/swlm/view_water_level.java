package com.example.swlm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;


public class view_water_level extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView dateTimeTextView;

    private TextView waterLevelTextView;
    private DatabaseReference waterLevelRef;

    // Notification channel ID and name
    private static final String CHANNEL_ID = "water_level_channel";
    private static final String CHANNEL_NAME = "Water Level Channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_water_level);

        progressBar = findViewById(R.id.progress_bar);

        dateTimeTextView = findViewById(R.id.txtWaterLevel);
        waterLevelTextView = findViewById(R.id.txtWaterLevel);
        waterLevelRef = FirebaseDatabase.getInstance().getReference().child("water_levels");

        progressBar.setRotation(-90); // for vertical progress bar on mobile screen


        // Query to get the latest record based on datetime
        Query latestWaterLevelQuery = waterLevelRef.orderByChild("datetime").limitToLast(1);

        latestWaterLevelQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Get water level and datetime values from the latest record
                        Integer waterLevel = snapshot.child("water_level").getValue(Integer.class);
                        String dateTime = snapshot.child("datetime").getValue(String.class);
                        if (waterLevel != null && dateTime != null) {
                            // Update water level text
                            String waterLevelString = waterLevel == 1 ? "High" : "Low"; // if 1 then hogh of if 0 then low
                            waterLevelTextView.setText("Water level is " + waterLevelString + "\n\n" + dateTime);

                            // Update progress bar
                            int progress = waterLevel * 100; // 0 if low, 100 if high
//                            progressBar.setProgress(progress);
//                            progressBar.setContentDescription(progress + "% filled"); // For accessibility
                            animateProgressBar(progress);


                            // Send notification
                            sendNotification(waterLevelString);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                waterLevelTextView.setText("Error..Something Wrong.! " + databaseError.getMessage());

            }
        });
    }

    // Method to send notification
    private void sendNotification(String waterLevel) {
        // Create notification channel if Android version is Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Water Level Notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Define notification message based on water level
        String notificationMessage;
        if (waterLevel.equals("High")) {
            notificationMessage = "Water level is High & Motor Off";
        } else {
            notificationMessage = "Water level is Low & Motor On";
        }

        // Create notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.splash_wl_two)
                .setContentTitle("WATER TANK LEVEL & MOTOR CONTROL")
                .setContentText(notificationMessage)

               // .setContentText("Water level is " + waterLevel)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(123, builder.build());
    }

//    private void animateProgressBar(int progress) {
//        final Handler handler = new Handler();
//        final int delay = 50; // Delay between each increment or decrement in milliseconds
//
//        // Increment or decrement progress based on current and target progress
//        if (progress > progressBar.getProgress()) {
//            // Fill progress bar
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (progressBar.getProgress() < progress) {
//                        progressBar.setProgress(progressBar.getProgress() + 1);
//                        updateProgressBarText(progressBar.getProgress());
//
//                        handler.postDelayed(this, delay);
//                    }
//
//                }
//            }, delay);
//        } else if (progress < progressBar.getProgress()) {
//            // Empty progress bar
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (progressBar.getProgress() > progress) {
//                        progressBar.setProgress(progressBar.getProgress() - 1);
//                        updateProgressBarText(progressBar.getProgress());
//
//                        handler.postDelayed(this, delay);
//                    }
//                }
//            }, delay);
//        }
//    }


    private void animateProgressBar(final int progress) {
        final Handler handler = new Handler();
        final int delay = 50; // Delay between each increment or decrement in milliseconds

        // Increment or decrement progress based on current and target progress
        if (progress > progressBar.getProgress()) {
            // Fill progress bar
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressBar.getProgress() < progress) {
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        updateProgressBarText(progressBar.getProgress());

                        // Check if progress bar animation is complete
                        if (progressBar.getProgress() == progress) {
                            // Animation complete, send notification
                            sendNotification(waterLevelTextView.getText().toString().contains("High") ? "High" : "Low");
                        }

                        handler.postDelayed(this, delay);
                    }
                }
            }, delay);
        } else if (progress < progressBar.getProgress()) {
            // Empty progress bar
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressBar.getProgress() > progress) {
                        progressBar.setProgress(progressBar.getProgress() - 1);
                        updateProgressBarText(progressBar.getProgress());

                        // Check if progress bar animation is complete
                        if (progressBar.getProgress() == progress) {
                            // Animation complete, send notification
                            sendNotification(waterLevelTextView.getText().toString().contains("High") ? "High" : "Low");
                        }

                        handler.postDelayed(this, delay);
                    }
                }
            }, delay);
        }
    }

    private void updateProgressBarText(int progress) {
        String progressText = progress + "%";
        progressBar.setProgress(progress);
        progressBar.setContentDescription(progressText); // For accessibility
    }
}