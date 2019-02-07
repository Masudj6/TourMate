package com.example.lazy_programmer.tourmate.Weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lazy_programmer.tourmate.R;

import java.util.ArrayList;

public class EditCityActivity extends AppCompatActivity {
    private ArrayList<String> menuList;
    private ListView menuListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_edit_city);
        menuList=new ArrayList<>();
        menuListView= (ListView) findViewById(R.id.menuItemListView);

        menuList= (ArrayList<String>) InternalStorage.readObject(MainActivity.context,"menus");

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.weather_location_list_text_color,menuList);
        menuListView.setAdapter(arrayAdapter);
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuList.remove(position);
                InternalStorage.writeObject(MainActivity.context,"menus",menuList);
                arrayAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditCityActivity.this,MainActivity.class));
    }
}
