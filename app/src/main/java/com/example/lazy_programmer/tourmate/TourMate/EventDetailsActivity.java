package com.example.lazy_programmer.tourmate.TourMate;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.lazy_programmer.tourmate.NearBy.NearByMainActivity;
import com.example.lazy_programmer.tourmate.R;
import com.example.lazy_programmer.tourmate.TourMate.fragments.DetailsFragment;
import com.example.lazy_programmer.tourmate.TourMate.fragments.ExpenseFragment;
import com.example.lazy_programmer.tourmate.TourMate.fragments.MomentFragment;
import com.example.lazy_programmer.tourmate.Weather.MainActivity;
import com.google.firebase.database.DatabaseReference;

public class EventDetailsActivity extends AppCompatActivity {

    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment fragment;
    private Button btnDetails,btnExpense,btnMoment;
    Drawable d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourmate_activity_event_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnDetails= (Button) findViewById(R.id.btnFragmentDetails);
        btnExpense= (Button) findViewById(R.id.btnFragmentExpense);
        btnMoment= (Button) findViewById(R.id.btnFragmentMoment);

        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();
        fragment=new DetailsFragment();
        getSupportActionBar().setTitle("   Event Details");
        ft.add(R.id.fragmentContainer,fragment);
        ft.commit();
        btnDetails.setBackgroundColor(Color.GRAY);
        if(d==null)
        d=btnMoment.getBackground();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(View view) {
        fm=getSupportFragmentManager();
        ft=fm.beginTransaction();

//        btnMoment.setBackground(d);
//        btnExpense.setBackground(d);
//        btnDetails.setBackground(d);
        btnMoment.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btnExpense.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        btnDetails.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


        if (view.getId()==R.id.btnFragmentDetails){
            getSupportActionBar().setTitle("   Event Details");
            fragment=new DetailsFragment();
            btnDetails.setBackgroundColor(Color.GRAY);
        }else if(view.getId()==R.id.btnFragmentExpense){
            getSupportActionBar().setTitle("   Expense List");
            fragment=new ExpenseFragment();
            btnExpense.setBackgroundColor(Color.GRAY);
        }else if(view.getId()==R.id.btnFragmentMoment){
            getSupportActionBar().setTitle("   Moments List");
            fragment=new MomentFragment();
            btnMoment.setBackgroundColor(Color.GRAY);
        }
        ft.replace(R.id.fragmentContainer,fragment);
        ft.commit();
    }


    public void expenseEntry(MenuItem item) {

        Intent i=new Intent(this,ExpenseEntryActivity.class);
        i.putExtra("Key",getIntent().getStringExtra("Key"));
        i.putExtra("Remaining",getIntent().getStringExtra("Remaining"));

        startActivity(i);


    }

    public void momentEntry(MenuItem item) {

        Intent i=new Intent(this,MomentEntryActivity.class);
        i.putExtra("Key",getIntent().getStringExtra("Key"));
        startActivity(i);
    }

    public void weatherCall(MenuItem item) {
        Intent i=new Intent(this, MainActivity.class);
        i.putExtra("PlaceName",getIntent().getStringExtra("Location"));
        startActivity(i);
    }

//    public void nearmeCall(MenuItem item) {
//        Intent i=new Intent(this, NearByMainActivity.class);
//        i.putExtra("PlaceName",getIntent().getStringExtra("Location"));
//        startActivity(i);



   // }
}
