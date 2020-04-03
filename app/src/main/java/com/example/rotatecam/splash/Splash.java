package com.example.rotatecam.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.rotatecam.R;
import com.example.rotatecam.activity.Login;

public class Splash extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            //Exibe splash com um timer
            @Override
            public void run() {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                Intent i = new Intent(Splash.this,
                        Login.class);
                startActivity(i);
                // Fecha esta activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
