package com.example.lazy_programmer.tourmate.NearBy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lazy_programmer.tourmate.NearBy.Adapter.PlaceAdapter;
import com.example.lazy_programmer.tourmate.NearBy.Adapter.PlaceModel;
import com.example.lazy_programmer.tourmate.NearBy.NearbyJSON.Data.DataAPI;
import com.example.lazy_programmer.tourmate.NearBy.NearbyJSON.Data.Main;
import com.example.lazy_programmer.tourmate.NearBy.NearbyJSON.Data.PlaceShortDetailsActivity;
import com.example.lazy_programmer.tourmate.NearBy.NearbyJSON.Data.Result;
import com.example.lazy_programmer.tourmate.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearByMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Location location;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private ArrayList<PlaceModel> places;
    public ListView placeListView;

    private String lat,lon;
    private boolean flag=true;
    private String type="";
    ProgressDialog load;
    String main=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                try {
                    Intent i=builder.build(NearByMainActivity.this);
                    startActivityForResult(i,1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //main part

        //getting location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        places=new ArrayList<>();
        placeListView= (ListView) findViewById(R.id.placeListView);
        load=new ProgressDialog(this);
        load.setMessage("Please wait...");


//        main=getIntent().getStringExtra("PlaceName");
//        if(main!=null){
//            try {
//                getPlaceData("","",main);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }else{

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationReslt) {
                    location = locationReslt.getLastLocation();
                    lat=location.getLatitude()+"";
                    lon=location.getLongitude()+"";

                    if(location!=null && flag){
                        flag=false;


                        try {
                            getPlaceData(location.getLatitude()+","+location.getLongitude(),null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            };

        //}

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                Place place=PlacePicker.getPlace(data,this);
                String s=place.getAddress().toString();
                Intent i=new Intent(this, PlaceShortDetailsActivity.class);
                i.putExtra("data",s);
                startActivity(i);
            }
        }
    }

    Call<Main> mainCall;
    private DataAPI dataAPI;
    private Main mainModel;
    private void getPlaceData(String loc, String name) {
        load.show();
       // https://maps.googleapis.com/maps/api/place/textsearch/xml?query=atm+in+dhaka&key=AIzaSyAiKf9Hv4Ofq0OJ-u8sDoMD0yJrdN6N4gA
        String query="";
//        if(main!=null && name!=null){
//            query="textsearch/xml?query="+name+"+in+"+main+"a&key=AIzaSyAiKf9Hv4Ofq0OJ-u8sDoMD0yJrdN6N4gA";
        if(loc!=null && name!=null){
            query= "nearbysearch/json?location="+loc+"&radius=500&type="+name+"&key=AIzaSyAiKf9Hv4Ofq0OJ-u8sDoMD0yJrdN6N4gA";
        }else{
            query= "nearbysearch/json?location="+loc+"&radius=500&key=AIzaSyAiKf9Hv4Ofq0OJ-u8sDoMD0yJrdN6N4gA";
        }
        places.clear();
        dataAPI= RetrofitClientPlace.getClient().create(DataAPI.class);
        Log.d("latlong", "before call : "+query);
        mainCall=dataAPI.getPlaceData(query);
        mainCall.enqueue(new Callback<Main>() {
            @Override
            public void onResponse(Call<Main> call, Response<Main> response) {
                mainModel=response.body();
                //  Toast.makeText(MainActivity.this, ""+mainModel.getResults().get(0).getName(), Toast.LENGTH_SHORT).show();
                Log.d("latlong", "onResponse: "+lat+" "+lon);

                for (int i=1;i<mainModel.getResults().size();i++){
                    Result r=mainModel.getResults().get(i);
                    places.add(new PlaceModel(r.getName(),r.getVicinity(),r.getIcon(),r.getPlaceId(),r.getRating()));

                }

//                    for (Result r:mainModel.getResults()) {
//                        places.add(new PlaceModel(r.getName(),r.getVicinity(),r.getIcon(),r.getId(),r.getRating()));
//                      //  Toast.makeText(MainActivity.this, ""+r.getName(), Toast.LENGTH_SHORT).show();
//                    }

                PlaceAdapter adapter=new PlaceAdapter(NearByMainActivity.this,places);
                placeListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                load.dismiss();
            }

            @Override
            public void onFailure(Call<Main> call, Throwable t) {
                Toast.makeText(NearByMainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                load.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return ;
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createLocationRequest() {
        locationRequest=new LocationRequest();
        locationRequest.setInterval(6000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.near_by_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String s=null;

        if (id == R.id.nav_ATM) {
            getSupportActionBar().setTitle("ATM List");
            s="atm";
        } else if (id == R.id.nav_Bank) {
            getSupportActionBar().setTitle("Bank List");
            s="bank";
        } else if (id == R.id.nav_Hospital) {
            getSupportActionBar().setTitle("Hospital List");
            s="hospital";
        } else if (id == R.id.nav_Hotel) {
            getSupportActionBar().setTitle("Hotel List");
            s="hotel";
        } else if (id == R.id.nav_restaurant) {
            getSupportActionBar().setTitle("Restaurant List");
            s="restaurant";
        } else if (id == R.id.nav_School) {
            getSupportActionBar().setTitle("School");
            s="school";
        }else if (id == R.id.nav_Police) {
            getSupportActionBar().setTitle("Police");
            s="police";
        }else if (id == R.id.nav_University) {
            getSupportActionBar().setTitle("University");
            s="university";
        }
//        if(main!=null && s!=null){
//            getPlaceData("",s,main);
         if(location!=null && s!=null){
           load.setMessage("Searching "+s);
            try {
                getPlaceData(lat+","+lon,s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
