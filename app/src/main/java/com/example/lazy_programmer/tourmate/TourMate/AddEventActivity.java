package com.example.lazy_programmer.tourmate.TourMate;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazy_programmer.tourmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Lazy-Programmer on 10/22/2017.
 */

public class AddEventActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth firebaseAuth;

    private Calendar calendar,calander1;
    private int fromYear,fromMonth,fromDay;
    private int toYear,toMonth,toDay;

//    SimpleDateFormat dateFormatter=new SimpleDateFormat("dd/MM/yyyy");

    String key,fromDate,toDate,budget,location;

    private EditText locationNameEt,fromDateEt, toDateEt, budgetEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourmate_activity_add_event);

        //popup window for adding event
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int) (height*.5));

        //

        firebaseAuth=FirebaseAuth.getInstance();
        calendar=Calendar.getInstance(Locale.getDefault());
        calander1=Calendar.getInstance(Locale.getDefault());

        //initializing views
        locationNameEt= (EditText) findViewById(R.id.locationNameEt);
        fromDateEt= (EditText) findViewById(R.id.fromDateEt);
        toDateEt = (EditText) findViewById(R.id.toDateEt);
        budgetEt = (EditText) findViewById(R.id.budgetEt);


        //from Date
        fromDay=calendar.get(Calendar.DAY_OF_MONTH);
        fromMonth=calendar.get(Calendar.MONTH);
        fromYear=calendar.get(Calendar.YEAR);

        //to date
        toDay=calendar.get(Calendar.DAY_OF_MONTH);
        toMonth=calendar.get(Calendar.MONTH);
        toYear=calendar.get(Calendar.YEAR);


        //getting intent data
        if(getIntent().getStringExtra("Menu")!=null){
            ((TextView)findViewById(R.id.createTv)).setText("Update");
            key=getIntent().getStringExtra("Key");
            fromDate=getIntent().getStringExtra("From");
            toDate=getIntent().getStringExtra("To");
            budget=getIntent().getStringExtra("Budget");
            location=getIntent().getStringExtra("Location");
            locationNameEt.setText(location);
            fromDateEt.setText(fromDate);
            toDateEt.setText(toDate);
            budgetEt.setText(budget);
        }

    }

    //EditText Date
    public void eventFromDate(View view) {
        DatePickerDialog fromDpd=new DatePickerDialog(this,fromDateListener,fromYear,fromMonth,fromDay);
        fromDpd.show();
    }
    DatePickerDialog.OnDateSetListener fromDateListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(year,month,dayOfMonth);
            SimpleDateFormat sdf=new SimpleDateFormat("dd MMM yyyy");
            fromDateEt.setText(sdf.format(calendar.getTime()));

        }
    };


    public void eventToDate(View view) {
        DatePickerDialog toDpd=new DatePickerDialog(this,toDateListener,toYear,toMonth,toDay);
        toDpd.show();
    }

    DatePickerDialog.OnDateSetListener toDateListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
           calendar.set(year,month,dayOfMonth);
            SimpleDateFormat sdf=new SimpleDateFormat("dd MMM yyyy");
            toDateEt.setText(sdf.format(calendar.getTime()));
        }
    };

    int eventKey ;
    private ProgressDialog load;
    public void onCreateEvent(View view) {

        load=new ProgressDialog(this);
        load.setMessage("Creating event...");
        load.show();
            String eventDestinationName=locationNameEt.getText().toString();
            String eventBudget= budgetEt.getText().toString();



            if (!eventDestinationName.isEmpty()&&!eventBudget.isEmpty()){

                String uid=firebaseAuth.getCurrentUser().getUid().toString();
                Log.d("uid",uid);
                if(key!=null){
                    eventKey=Integer.parseInt(key);
                }else {
                    eventKey = TourMateHomePage.count + 1;
                }
                mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("data").child(uid).child("travel_event_list");

                String eventFromDate=fromDateEt.getText().toString();
                String eventToDate= toDateEt.getText().toString();

                int flag=5,flag1=5;

                if (eventFromDate!=null && eventToDate!=null) {
                    try {
                      flag=  new SimpleDateFormat("dd MMM yyyy").parse(eventFromDate).compareTo(new SimpleDateFormat("dd MMM yyyy").parse(eventToDate));
                        SimpleDateFormat sdf=new SimpleDateFormat("dd MMM yyyy");
                        String presentDate=sdf.format(calander1.getTime());
                      flag1=  new SimpleDateFormat("dd MMM yyyy").parse(eventFromDate).compareTo(sdf.parse(presentDate));

                    }catch(Exception e){

                    }
                    //Toast.makeText(this, ""+flag, Toast.LENGTH_SHORT).show();

                    if (flag<=0 && flag1>0) {
                        HashMap<String,String> datamap=new HashMap<>();
                        datamap.put("Location Name",eventDestinationName);
                        datamap.put("From Date",eventFromDate);
                        datamap.put("To Date",eventToDate);
                        datamap.put("Budget",eventBudget);
                        datamap.put("Remaining",eventBudget);

                        mDatabaseReference.child(String.valueOf(eventKey)).setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent i=new Intent(AddEventActivity.this,TourMateHomePage.class);
                                    setResult(RESULT_OK,i);
                                    AddEventActivity.this.finish();
                                    Toast.makeText(AddEventActivity.this,"Event Created",Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    Toast.makeText(AddEventActivity.this,"Check your network connection",Toast.LENGTH_SHORT).show();
                                }
                                load.dismiss();
                            }
                        });
                    }else{
                        load.dismiss();
                        fromDateEt.setError("incorrect date");
                        toDateEt.setError("incorrect date");
                        Toast.makeText(this, "Please check you date", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    load.dismiss();
                    fromDateEt.setError("Empty date");
                    toDateEt.setError("Empty date");
                }

            }

            else {
                if (eventDestinationName.isEmpty()){
                    locationNameEt.setError("This field can't be empty");
                }
                if (eventBudget.isEmpty()){
                    budgetEt.setError("This field can't be empty");
                }
            }


        }



    public void onCancel(View view) {
        this.finish();
    }
}