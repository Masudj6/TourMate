package com.example.lazy_programmer.tourmate.TourMate.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lazy_programmer.tourmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private TextView locationNameTv,fromDateTv,toDateTv,budgetTv,remainingBalanceTv;
    public DetailsFragment() {
        // Required empty public constructor
    }

    private DatabaseReference dbReference;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.tourmate_fragment_details, container, false);
        setHasOptionsMenu(true);

        locationNameTv= (TextView) v.findViewById(R.id.detailsLocationTv);
        fromDateTv= (TextView) v.findViewById(R.id.detailsFromDateTv);
        toDateTv= (TextView) v.findViewById(R.id.detailsToDateTv);
        budgetTv= (TextView) v.findViewById(R.id.detailsBudgetTv);
        remainingBalanceTv= (TextView) v.findViewById(R.id.remainingBalanceTv);

        locationNameTv.setText("Destination: "+getActivity().getIntent().getStringExtra("Location"));
        fromDateTv.setText("From: "+getActivity().getIntent().getStringExtra("FromDate"));
        toDateTv.setText("To: "+getActivity().getIntent().getStringExtra("ToDate"));
        budgetTv.setText("Budget: "+getActivity().getIntent().getStringExtra("Budget")+" tk");
        String key=getActivity().getIntent().getStringExtra("Key");

        uid=FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim();
        dbReference=FirebaseDatabase.getInstance().getReference().child("data").child(uid).child("travel_event_list").child(key);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                remainingBalanceTv.setText("Balance Left: "+dataSnapshot.child("Remaining").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

               return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
