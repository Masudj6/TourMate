package com.example.lazy_programmer.tourmate.TourMate.fragments;


;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lazy_programmer.tourmate.R;
import com.example.lazy_programmer.tourmate.TourMate.Adapters.EventModel;
import com.example.lazy_programmer.tourmate.TourMate.Adapters.ExpenseAdapter;
import com.example.lazy_programmer.tourmate.TourMate.Adapters.ExpenseModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    private Calendar calendar;

    public ExpenseFragment() {
        // Required empty public constructor
    }
    private ListView expenseListView;
    private ArrayList<ExpenseModel> expenses;
    private DatabaseReference dbDatabaseReference;
    private String uid,key;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.tourmate_fragment_expense, container, false);
        setHasOptionsMenu(true);
        calendar= Calendar.getInstance(Locale.getDefault());
        expenseListView= (ListView) v.findViewById(R.id.expenseListView);
        key=getActivity().getIntent().getStringExtra("Key");

        expenses=new ArrayList<>();
       // expenses.add(new ExpenseModel("29 Aug 2:30 pm","Snacks","200"));



        getAndSetAllData();



        return v;
    }

    private void getAndSetAllData() {
        try {
            uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim();
            dbDatabaseReference= FirebaseDatabase.getInstance().getReference().child("data").child(uid).child("expenses").child(key);


            dbDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    expenses.clear();
                    for (DataSnapshot ds:dataSnapshot.getChildren()) {
                        expenses.add(new ExpenseModel(ds.child("Time").getValue().toString(),ds.getKey().toString(),ds.child("Cost").getValue().toString()));


                    }

                    try {

                        Collections.sort(expenses, new Comparator<ExpenseModel>() {
                            @Override
                            public int compare(ExpenseModel o1, ExpenseModel o2) {
                                try {
                                    return new SimpleDateFormat("dd MMM hh:mm a").parse(o1.getTime()).compareTo(new SimpleDateFormat("dd MMM hh:mm a").parse(o2.getTime()));
                                } catch (Exception e) {

                                }

                                return 0;
                            }
                        });
                        Collections.reverse(expenses);
                        ExpenseAdapter adapter=new ExpenseAdapter(getActivity(),expenses);
                        expenseListView.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
