package com.example.lazy_programmer.tourmate;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.example.lazy_programmer.tourmate.TourMate.LoginActivity;
import com.example.lazy_programmer.tourmate.TourMate.TourMateHomePage;
import com.example.lazy_programmer.tourmate.Weather.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainHomePage extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgTourMate,imgNearby,imgWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home_page);

        imgTourMate = (ImageView) findViewById(R.id.imgTourMate);
        imgNearby = (ImageView) findViewById(R.id.imgNearby);
        imgWeather= (ImageView) findViewById(R.id.imgWeather);

        imgTourMate.setOnClickListener(this);
        imgNearby.setOnClickListener(this);
        imgWeather.setOnClickListener(this);

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.REVERSE, 0.5f, Animation.REVERSE, 0.5f);


//Setup anim with desired properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(6000); //Put desired duration per anim cycle here, in milliseconds

//Start animation
        imgTourMate.startAnimation(anim);

    }

    private FirebaseAuth firebaseAuth;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgTourMate:
                try {
                    firebaseAuth=FirebaseAuth.getInstance();
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    if(user==null){
                        startActivity(new Intent(this, LoginActivity.class));
                    }

                    else{
                        startActivity(new Intent(this, TourMateHomePage.class));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.imgNearby:
                startActivity(new Intent(this, com.example.lazy_programmer.tourmate.NearBy.NearByMainActivity.class));
                break;
            case R.id.imgWeather:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
