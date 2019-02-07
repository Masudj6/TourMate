package com.example.lazy_programmer.tourmate.NearBy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazy_programmer.tourmate.NearBy.DataDetails.MainDetails;
import com.example.lazy_programmer.tourmate.NearBy.DataDetails.DataAPI;
import com.example.lazy_programmer.tourmate.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceDetailsActivity extends AppCompatActivity {

    private TextView placeNameTv, placeAddressTv, placePhoneTv, placeRating, weekDayTv;
    private ImageView placeBigImage;

    private String id, name, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_place_details);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        placeNameTv = (TextView) findViewById(R.id.placeDetailsNameTv);
        placeAddressTv = (TextView) findViewById(R.id.placeDetailsAddressTv);
        placePhoneTv = (TextView) findViewById(R.id.placeDetailsPhoneTv);
        placeRating = (TextView) findViewById(R.id.placeDetailsRatingTv);
        weekDayTv = (TextView) findViewById(R.id.weekdaysDetailsTv);

        placeBigImage = (ImageView) findViewById(R.id.placeImageView);


        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        try {
            getPlaceData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
    }

    String phoneNumber;

    Call<MainDetails> mainCall;
    private DataAPI dataAPI;
    private MainDetails mainModel;
    private ProgressDialog load;

    private void getPlaceData() {

        load = new ProgressDialog(this);
        load.setMessage("loading data...");
        load.show();
        String query = "json?placeid=" + id + "&key=AIzaSyAiKf9Hv4Ofq0OJ-u8sDoMD0yJrdN6N4gA";
        dataAPI = RetrofitClientPlaceDetails.getClient().create(DataAPI.class);
        Log.d("lat", "before call : " + query);
        mainCall = dataAPI.getPlaceData(query);
        mainCall.enqueue(new Callback<MainDetails>() {
            @Override
            public void onResponse(Call<MainDetails> call, Response<MainDetails> response) {
                mainModel = response.body();

                placeNameTv.setText(name);
                placeAddressTv.setText("Address:  " + address);
                placeRating.setText("Rating:  " + mainModel.getResult().getRating());
                phoneNumber = mainModel.getResult().getFormattedPhoneNumber();
                placePhoneTv.setText("Phone:  " + mainModel.getResult().getFormattedPhoneNumber());
                weekDayTv.setText("     Week Days:  \n" + mainModel.getResult().getOpeningHours().getWeekdayText().get(0) + "\n" +
                        mainModel.getResult().getOpeningHours().getWeekdayText().get(1) + "\n" +
                        mainModel.getResult().getOpeningHours().getWeekdayText().get(2) + "\n" +
                        mainModel.getResult().getOpeningHours().getWeekdayText().get(3) + "\n" +
                        mainModel.getResult().getOpeningHours().getWeekdayText().get(4) + "\n" +
                        mainModel.getResult().getOpeningHours().getWeekdayText().get(5) + "\n" +
                        mainModel.getResult().getOpeningHours().getWeekdayText().get(6));

                String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + mainModel.getResult().getPhotos().get(0).getPhotoReference() + "&key=AIzaSyAiKf9Hv4Ofq0OJ-u8sDoMD0yJrdN6N4gA";
                Picasso.with(PlaceDetailsActivity.this) // Context
                        .load(url) // URL or file
                        .into(placeBigImage);

                load.dismiss();
            }

            @Override
            public void onFailure(Call<MainDetails> call, Throwable t) {
                Toast.makeText(PlaceDetailsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                load.dismiss();
            }
        });
    }

    public void callNumber(View view) {

        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
}
