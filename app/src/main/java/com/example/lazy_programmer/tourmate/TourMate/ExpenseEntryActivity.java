package com.example.lazy_programmer.tourmate.TourMate;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Locale;

public class ExpenseEntryActivity extends AppCompatActivity {

    private EditText expenseDetailsEt,expenseCostEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourmate_expense_entry);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int) (height*.5));


        expenseCostEt= (EditText) findViewById(R.id.expenseCostEt);
        expenseDetailsEt= (EditText) findViewById(R.id.expenseDetailsEt);
    }

    private DatabaseReference dbReferenceExpence,dbReferenceBudget;
    private String uid,key,remaining;

    public void onEntryExpense(View view) {
        try {
            final ProgressDialog loading=new ProgressDialog(this);
            loading.setMessage("please wait");
            loading.show();
            uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim();
            key=getIntent().getStringExtra("Key");


            dbReferenceExpence = FirebaseDatabase.getInstance().getReference().child("data").child(uid).child("expenses").child(key);
            dbReferenceBudget = FirebaseDatabase.getInstance().getReference().child("data").child(uid).child("travel_event_list").child(key);
            dbReferenceBudget.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    remaining=dataSnapshot.child("Remaining").getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            String expenseDetails=expenseDetailsEt.getText().toString();
            final String expenseCost=expenseCostEt.getText().toString();

            if (expenseDetails!=null && expenseDetails!=null) {

                HashMap<String,String>dataMap=new HashMap<>();
                dataMap.put("Cost",expenseCost);
                dataMap.put("Time",getCurrentTime());

                dbReferenceExpence.child(expenseDetails).setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            int flag=Integer.parseInt(remaining);
                            flag=flag-Integer.parseInt(expenseCost);
                            dbReferenceBudget.child("Remaining").setValue(flag+"");

                            loading.dismiss();
                            Toast.makeText(ExpenseEntryActivity.this, "Expense saved", Toast.LENGTH_SHORT).show();
                            ExpenseEntryActivity.this.finish();
                        }else{
                            Toast.makeText(ExpenseEntryActivity.this, "Check Network connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                loading.dismiss();
                if(expenseDetails==null)
                expenseDetailsEt.setError("can't be empty");
                if(expenseCost==null)
                    expenseCostEt.setError("can't be empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
private Calendar calendar;
    private String getCurrentTime(){
        String time="";
        calendar= Calendar.getInstance(Locale.getDefault());

        try {
            SimpleDateFormat sdf=new SimpleDateFormat("dd MMM   hh:mm a");
            time=sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }

}
