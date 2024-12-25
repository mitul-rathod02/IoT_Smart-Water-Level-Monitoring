package com.example.swlm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skydoves.progressview.OnProgressChangeListener;
import com.skydoves.progressview.OnProgressClickListener;
import com.skydoves.progressview.ProgressView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import cjh.WaveProgressBarlibrary.WaveProgressBar;

public class MainActivity extends AppCompatActivity {

    int progress = 0;
    boolean started = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WaveProgressBar waveProgressBar = findViewById(R.id.waterLevel);
        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(started){
                    progress++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            waveProgressBar.setProgress(progress);
                        }
                    });
                    if(progress == 100){
                        progress = 0;
                    }
                }
            }
        };
        timer.schedule(timerTask, 0 , 80);
        waveProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                started = !started;
            }
        });
    }
}