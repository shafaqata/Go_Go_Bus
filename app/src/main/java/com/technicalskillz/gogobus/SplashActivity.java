package com.technicalskillz.gogobus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        imageView=findViewById(R.id.logoImage);
        textView=findViewById(R.id.logoText);

        Animation animationlogo;
        Animation animationText;


        animationlogo= AnimationUtils.loadAnimation(this,R.anim.my_logo_anim);
        animationText= AnimationUtils.loadAnimation(this,R.anim.text_anim);
        imageView.setAnimation(animationlogo);
        textView.setAnimation(animationText);

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        };
        new Handler().postDelayed(runnable,3000);
    }
}