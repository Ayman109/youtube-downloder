package com.SimpleApp.youtubedownloder;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        @SuppressLint("ResourceType")
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.animator.logo_animator);
         LinearLayout linearLayout = (LinearLayout) findViewById(R.id.logo_splash);
        linearLayout.setAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this , MainActivity.class));
            }
        },5000);

    }
}