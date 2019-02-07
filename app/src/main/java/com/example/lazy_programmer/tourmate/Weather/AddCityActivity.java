package com.example.lazy_programmer.tourmate.Weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.lazy_programmer.tourmate.R;

public class AddCityActivity extends Activity {


    private EditText searchCityEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_add_city);
        searchCityEt= (EditText) findViewById(R.id.searchCityEt);


    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void saveLocation(View view) {
        Intent intent = new Intent();
        intent.putExtra("AddLocation", searchCityEt.getText().toString());
        setResult(RESULT_OK, intent);
        finish();

    }
}
