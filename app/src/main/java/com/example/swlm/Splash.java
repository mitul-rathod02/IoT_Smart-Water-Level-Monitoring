package com.example.swlm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    TextView txtName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // for hide the header and action bar on top for only splash screen activity only
        //getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtName = findViewById(R.id.txtName);

        // animate the text view - top to down in splash screen
        txtName.animate().translationY(1000).setDuration(2000).setStartDelay(2600);

        // after a 3990 milli second redirect to the Main activity
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3990);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(Splash.this,view_water_level.class);
                    startActivity(intent);
                    finish(); // if user back from main activity then splash screen is not start aging it exit the app
                }
            }
        };
        thread.start();
    }
}