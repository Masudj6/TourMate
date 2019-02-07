package com.example.lazy_programmer.tourmate.Weather;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazy_programmer.tourmate.R;
import com.example.lazy_programmer.tourmate.Weather.adapters.ListViewWeather;
import com.example.lazy_programmer.tourmate.Weather.adapters.ListViewcustomAdapter;
import com.example.lazy_programmer.tourmate.Weather.adapters.RecyclerCustomAdapter;
import com.example.lazy_programmer.tourmate.Weather.adapters.RecyclerWeather;
import com.example.lazy_programmer.tourmate.Weather.newData.DataAPI;
import com.example.lazy_programmer.tourmate.Weather.newData.WeatherModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.text.ParseException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private LinearLayout mainLayout;
    private DrawerLayout drawer;
    private WeatherModel weatherModel;
    private DataAPI dataAPI;
    private ImageView imgWeatherTypeIcon,imgDetails;
    private TextView currentLocationNameTv,dateTv,maxTv,minTv,weatherTypeTv,humidityTv,detailsFeelsLike,detailsHumidity,detailsVisibility,detailsUVIndex;
    private ArrayList<RecyclerWeather> hourlyForecastList;
    private ArrayList<ListViewWeather> forecastList;
    private RecyclerView hourlyRecyclerView;
    private ListView forecastDayListView;
    private ListViewWeather listViewWeather;
    private SharedPreferences weatherPreferences;
    private RecyclerWeather recyclerWeather;
    private Location location;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    String lat=2.344533+"",lon=5.343223+"";
    private ArrayList<String> menuList=new ArrayList<>();
    SharedPreferences.Editor editor;
    public static Context context = null;
    public boolean check=false;
    NavigationView navigationView;
    private ProgressDialog loadingDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main);
        context = this;

        //initailizing views...
        mainLayout= (LinearLayout) findViewById(R.id.main_layout);
        ImageButton btnDrawer= (ImageButton) findViewById(R.id.btnDrawer);
        currentLocationNameTv= (TextView) findViewById(R.id.currentLoactionNameTv);
        dateTv= (TextView) findViewById(R.id.dateTv);
        maxTv= (TextView) findViewById(R.id.weatherMaxTv);
        minTv= (TextView) findViewById(R.id.weatherMinTv);
        weatherTypeTv= (TextView) findViewById(R.id.weatherTypeTv);
        humidityTv= (TextView) findViewById(R.id.currentHumidityTv);
        imgWeatherTypeIcon= (ImageView) findViewById(R.id.imgWeatherType);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        hourlyRecyclerView= (RecyclerView) findViewById(R.id.hourlyRecyclerView);
        forecastDayListView= (ListView) findViewById(R.id.forecastDayListView);
        detailsFeelsLike= (TextView) findViewById(R.id.detailsFeelsLikeTv);
        detailsHumidity= (TextView) findViewById(R.id.detailsHumidityTv);
        detailsVisibility= (TextView) findViewById(R.id.detailsVisibilityTv);
        detailsUVIndex= (TextView) findViewById(R.id.detailsUVIndexTv);
        imgDetails= (ImageView) findViewById(R.id.imageViewDetails);

        //  creating and launching progressbar



        weatherModel=new WeatherModel();

        //getting previous data if no network available
        if(!haveNetworkConnection()){
                weatherModel=(WeatherModel) InternalStorage.readObject(MainActivity.this,"Weather");
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        // Setting Nav menu and menu items
        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
         navigationView= (NavigationView) findViewById(R.id.nav_view);

            menuList=(ArrayList<String>) InternalStorage.readObject(this,"menus");
            if(menuList!=null){
                for (String s:menuList) {
                   Menu m= navigationView.getMenu();
                    m.add(s);
                }
            }else{
                menuList=new ArrayList<>();
            }
            check=true;
        navigationView.setNavigationItemSelectedListener(this);


        //getting location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationReslt) {
                location = locationReslt.getLastLocation();
                lat=location.getLatitude()+"";
                lon=location.getLongitude()+"";

                if(location!=null){
                    loadingDialog.hide();
                    editor=weatherPreferences.edit();
                    editor.putString("Lat",location.getLatitude()+"");
                    editor.putString("Lon",location.getLongitude()+"");
                    editor.apply();
                    editor.commit();
                }

            }
        };

        //getting data from web
        if(location==null){
            weatherPreferences=getPreferences(MODE_PRIVATE);
            lat=weatherPreferences.getString("Lat","23.74");
            lon=weatherPreferences.getString("Lon","90.36");

        }
        String main=getIntent().getStringExtra("PlaceName");

        if(main!=null){
            Toast.makeText(context, ""+main, Toast.LENGTH_SHORT).show();
            setWeatherData(main+"&days=10");
        }else{
            setWeatherData(lat+","+lon+"&days=10");
        }


    }

    public void generateMenuItem(String loc) {
        NavigationView navMenu= (NavigationView) findViewById(R.id.nav_view);
        navMenu.setNavigationItemSelectedListener(this);
        Menu menu = navMenu.getMenu();
        menu.add(R.id.g2, Menu.NONE, Menu.NONE,loc);
    }


    private void createLocationRequest() {
        locationRequest=new LocationRequest();
        locationRequest.setInterval(6000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return ;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setCancelable(true);

        loadingDialog.show();
        //loadingDialog.cancel();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 ){
            if(resultCode==RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(this, data);
              //  Toast.makeText(MainActivity.this, ""+place.getName()+" "+place.getLatLng()+" "+place.getLocale(), Toast.LENGTH_SHORT).show();
                String s=place.getName().toString();
                if(s.length()>0){
                    menuList.add(s);
                    generateMenuItem(s);
                    setWeatherData(s+"&days=10");
                }

            }
        }
        InternalStorage.writeObject(this,"menus",menuList);

    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);


    }

    Call<WeatherModel> weatherModelCall;
public static boolean bool= true;
    private void setWeatherData(String loc) {
        boolean flag=true;

        if(!haveNetworkConnection() ||location==null){
            weatherPreferences=getPreferences(MODE_PRIVATE);

            String s=weatherPreferences.getString("Time",null);
            dateTv.setText(s);

            s=weatherPreferences.getString("LocName",null);
            currentLocationNameTv.setText(s);

            s=weatherPreferences.getString("Max",null);
            maxTv.setText(s);

            s=weatherPreferences.getString("Min",null);
            minTv.setText(s);

            s=weatherPreferences.getString("Type",null);
            weatherTypeTv.setText(s);

            s=weatherPreferences.getString("Humidity",null);
            humidityTv.setText(s);
            if(!haveNetworkConnection())
                flag=false;
            else if(bool)
                loc=weatherPreferences.getString("LocName",null)+"&days=10";
        }
        if (flag){

            String query="forecast.json?key=b4293bf8bc9340308dc185149172009&q="+loc;
            dataAPI= RetrofitClient.getClient().create(DataAPI.class);
            weatherModelCall=dataAPI.getAtmosphereInfo(query);
        weatherModelCall.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {

                        weatherModel=response.body();
                    // setting weather data

                SharedPreferences.Editor editor=weatherPreferences.edit();
                try {


                dateTv.setText(weatherModel.getLocations().getLocaltime());
                editor.putString("Time",weatherModel.getLocations().getLocaltime());

                currentLocationNameTv.setText(weatherModel.getLocations().getName());
                editor.putString("LocName",weatherModel.getLocations().getName());
                maxTv.setText(""+(int)weatherModel.getForecast().getForecastday().get(0).getDay().getMaxtempC()+ (char) 0x00B0);
                editor.putString("Max",""+(int)weatherModel.getForecast().getForecastday().get(0).getDay().getMaxtempC()+ (char) 0x00B0);
                minTv.setText(""+(int)weatherModel.getForecast().getForecastday().get(0).getDay().getMintempC()+ (char) 0x00B0);
                editor.putString("Min",""+(int)weatherModel.getForecast().getForecastday().get(0).getDay().getMintempC()+ (char) 0x00B0);
                String type=weatherModel.getCurrent().getCondition().getTypeText();
                weatherTypeTv.setText(type+"");
                editor.putString("Type",weatherModel.getCurrent().getCondition().getTypeText()+"");
                int i=(int)weatherModel.getCurrent().getTempC();
                humidityTv.setText(""+i+ (char) 0x00B0);
                editor.putString("Humidity",""+(int)weatherModel.getCurrent().getTempC()+ (char) 0x00B0);

                editor.apply();
                editor.commit();
                //Setting details of day

                detailsFeelsLike.setText(""+(int)weatherModel.getCurrent().getFeelslikeC()+(char)0x00B0);
                detailsVisibility.setText(weatherModel.getCurrent().getVisibility()+" KM");
                detailsHumidity.setText(weatherModel.getCurrent().getHumidity()+"%");
                detailsUVIndex.setText(weatherModel.getCurrent().getPressureMb()+" mb");



                //setting Adapters, ListView and RecyclerView

                recyclerWeather=new RecyclerWeather();
                listViewWeather=new ListViewWeather();

                // Recycler View
                hourlyForecastList=recyclerWeather.getHourlyForecastList(weatherModel);

                RecyclerCustomAdapter recyclerCustomAdapter=
                        new RecyclerCustomAdapter(MainActivity.this,hourlyForecastList);

                LinearLayoutManager llm=new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
                hourlyRecyclerView.setLayoutManager(llm);
                hourlyRecyclerView.setAdapter(recyclerCustomAdapter);

                //List View

                try {
                    forecastList=listViewWeather.getForecastList(weatherModel);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ListViewcustomAdapter listViewcustomAdapter=new ListViewcustomAdapter(MainActivity.this,forecastList);
                forecastDayListView.setAdapter(listViewcustomAdapter);
                    loadingDialog.dismiss();
                // setting icons
                Drawable d;


                if(type.equals("Sunny")){
                    d=getResources().getDrawable(R.drawable.weather_sunny_bg_new);
                    mainLayout.setBackground(d);
                    imgWeatherTypeIcon.setImageResource(R.drawable.weather_sunny1);
                    imgDetails.setImageResource(R.drawable.weather_sunny1);
                }else if(type.equals("Clear")){

                    imgWeatherTypeIcon.setImageResource(R.drawable.weather_clear);
                    imgDetails.setImageResource(R.drawable.weather_clear);
                }else if(type.equals("Cloudy")){
                    d=getResources().getDrawable(R.drawable.weather_cloudy_bg_new);
                    mainLayout.setBackground(d);
                    imgDetails.setImageResource(R.drawable.weather_cloudy);
                    imgWeatherTypeIcon.setImageResource(R.drawable.weather_cloudy);
                }else if(type.equals("Thundery outbreaks possible")){
                    d=getResources().getDrawable(R.drawable.weather_storm_bg);
                    mainLayout.setBackground(d);
                    imgDetails.setImageResource(R.drawable.weather_storm);
                    imgWeatherTypeIcon.setImageResource(R.drawable.weather_storm);
                }else if(type.equals("Patchy rain possible")){
                    d=getResources().getDrawable(R.drawable.weather_rainy_bg_new1);
                    mainLayout.setBackground(d);
                    imgDetails.setImageResource(R.drawable.weather_partly_rain);
                    imgWeatherTypeIcon.setImageResource(R.drawable.weather_partly_rain);
                }else if(type.equals("Partly cloudy_bg")){
                    d=getResources().getDrawable(R.drawable.weather_cloudy_bg_new);
                    mainLayout.setBackground(d);
                    imgDetails.setImageResource(R.drawable.weather_partly_cloudy);
                    imgWeatherTypeIcon.setImageResource(R.drawable.weather_partly_cloudy);
                }else if(type.equals("Patchy light rain with thunder")){
                    d=getResources().getDrawable(R.drawable.weather_storm_bg);
                    mainLayout.setBackground(d);
                    imgDetails.setImageResource(R.drawable.weather_storm);
                    imgWeatherTypeIcon.setImageResource(R.drawable.weather_storm);
                }else if(type.equals("Mist")){
                    imgDetails.setImageResource(R.drawable.weather_mist);
                    imgWeatherTypeIcon.setImageResource(R.drawable.weather_mist);
                }else if(type.equals("Light rain shower")){
                    d=getResources().getDrawable(R.drawable.weather_rainy_bg_new1);
                    mainLayout.setBackground(d);
                    imgDetails.setImageResource(R.drawable.weather_partly_rain);
                    imgWeatherTypeIcon.setImageResource(R.drawable.weather_sunny1);
                }else if(type.equals("Overcast")){
                    imgDetails.setImageResource(R.drawable.weather_overcast);
                    imgWeatherTypeIcon.setImageResource(R.drawable.weather_overcast);
                }else if(type.equals("Moderate rain")){
                    d=getResources().getDrawable(R.drawable.weather_rainy_bg_new1);
                    mainLayout.setBackground(d);
                    imgDetails.setImageResource(R.drawable.weather_rain);
                    imgWeatherTypeIcon.setImageResource(R.drawable.weather_rain);
                }
                }catch(Exception e){
                   // Toast.makeText(MainActivity.this, "Area not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Check your internet", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
        }
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.nav_current) {
            if (location != null)
                setWeatherData(location.getLatitude() + "," + location.getLongitude()+"&days=10");

        }else if (item.getItemId()==R.id.editItem){
                Intent i=new Intent(this,EditCityActivity.class);
                this.finish();
                startActivity(i);

            }else{
            bool=false;
                setWeatherData(item.getTitle().toString()+"&days=10");
           // Toast.makeText(context, ""+item.getTitle().toString(), Toast.LENGTH_SHORT).show();
            }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public void selectLocation(View view) {

        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (Exception e) {
            // TODO: Handle the error.
        }


    }

    public void selectDays(View v){
        weatherPreferences=getPreferences(MODE_PRIVATE);
        String s=weatherPreferences.getString("LocName",null);
        if(v.getId()==R.id.btn5d){
                setWeatherData(s+"&days=5");
        } else if(v.getId()==R.id.btn10d){
                setWeatherData(s+"&days=10");
        }
    }



    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }



}
