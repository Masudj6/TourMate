package com.example.lazy_programmer.tourmate.NearBy.NearbyJSON.Data;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.lazy_programmer.tourmate.R;

public class PlaceShortDetailsActivity extends AppCompatActivity {

    private TextView placeDetailsTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_place_short_details);

        //popup window for adding event
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int) (height*.5));

        String s=getIntent().getStringExtra("data");
        placeDetailsTv= (TextView) findViewById(R.id.placeShortDetailsTv);
        placeDetailsTv.setText(s);
    }
}
