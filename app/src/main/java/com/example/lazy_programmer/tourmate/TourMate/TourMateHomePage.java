package com.example.lazy_programmer.tourmate.TourMate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lazy_programmer.tourmate.MainHomePage;
import com.example.lazy_programmer.tourmate.R;
import com.example.lazy_programmer.tourmate.TourMate.Adapters.EventAdapter;
import com.example.lazy_programmer.tourmate.TourMate.Adapters.EventModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class TourMateHomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<EventModel> eventList;
    private RecyclerView eventListView;
    private FloatingActionButton fab;
    private DatabaseReference dbReference;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_mate_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        eventListView= (RecyclerView) findViewById(R.id.eventListView);
        eventList=new ArrayList<>();
//        eventList.add(new EventModel("123","From: 22 Oct 2017","To: 25 Oct 2017","Budget: 2000/tk","Narayanganj"));
        getAllData();



        eventListView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(TourMateHomePage.this,AddEventActivity.class),1);
            }
        });



    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        if(item.getOrder()==0){
            //Toast.makeText(this, ""+item.toString()+" "+1, Toast.LENGTH_SHORT).show();
            Intent i=new Intent(this,AddEventActivity.class);
            i.putExtra("Menu","context");
            i.putExtra("Key",eventList.get(item.getGroupId()).getKey().toString());
            i.putExtra("From",eventList.get(item.getGroupId()).getFromDate().toString());
            i.putExtra("To",eventList.get(item.getGroupId()).getToDate().toString());
            i.putExtra("Budget",eventList.get(item.getGroupId()).getBudget().toString());
            i.putExtra("Location",eventList.get(item.getGroupId()).getLocationName().toString());
            startActivity(i);
        }else if(item.getOrder()==1){
            String eventId=eventList.get(item.getGroupId()).getKey().toString();
            dbReference=FirebaseDatabase.getInstance().getReference().child("data").child(uid).child("travel_event_list").child(eventId);
            dbReference.child("Budget").setValue(null);
            dbReference.child("From Date").setValue(null);
            dbReference.child("To Date").setValue(null);
            dbReference.child("Location Name").setValue(null);
            getAllData();
           // eventList.remove(item.getGroupId());
            Toast.makeText(this, ""+item.toString()+" "+item.getGroupId(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public  static int count=0;
    ProgressDialog loading;
    private void getAllData() {

        try {
            loading=new ProgressDialog(this);
            loading.setMessage("Loading data...");
            loading.show();

            dbReference=FirebaseDatabase.getInstance().getReference().child("data").child(uid).child("travel_event_list");

            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    eventList.clear();
                   // count=(int)dataSnapshot.getChildrenCount();
                    for (DataSnapshot ds:dataSnapshot.getChildren()){

                        try {
                            eventList.add(new EventModel(ds.getKey().toString(), ds.child("From Date").getValue().toString(), ds.child("To Date").getValue().toString(), ds.child("Budget").getValue().toString(), ds.child("Location Name").getValue().toString(),ds.child("Remaining").getValue().toString()));
                            count = Integer.parseInt(ds.getKey());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    Collections.sort(eventList, new Comparator<EventModel>() {
                        @Override
                        public int compare(EventModel o1, EventModel o2) {
                            try {
                                return new SimpleDateFormat("dd MMM yyyy").parse(o1.getFromDate()).compareTo(new SimpleDateFormat("dd MMM yyyy").parse(o2.getFromDate()));
                            } catch (Exception e) {

                            }

                        return 0;
                        }
                    });
                    Collections.reverse(eventList);
                    EventAdapter adapter=new EventAdapter(eventList,TourMateHomePage.this);
                    LinearLayoutManager llm=new LinearLayoutManager(TourMateHomePage.this,LinearLayoutManager.VERTICAL,false);
                    eventListView.setLayoutManager(llm);
                    eventListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    loading.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                   // Toast.makeText(TourMateHomePage.this, "Data loading failed", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                getAllData();
            }
        }
    }

    boolean isDoublePressed=false;
    @Override
    public void onBackPressed() {
        if (isDoublePressed){
            this.finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            isDoublePressed=true;
        }

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.tour_mate_home_page, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

//           startActivity(new Intent(this,MainHomePage.class));
            this.finish();
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this,ProfileActivity.class));

        } else if (id == R.id.nav_logout) {
            FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            startActivity(new Intent(this,MainHomePage.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
