package com.example.lazy_programmer.tourmate.TourMate.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lazy_programmer.tourmate.R;
import com.example.lazy_programmer.tourmate.TourMate.Adapters.ExpenseAdapter;
import com.example.lazy_programmer.tourmate.TourMate.Adapters.ExpenseModel;
import com.example.lazy_programmer.tourmate.TourMate.Adapters.MomentAdapter;
import com.example.lazy_programmer.tourmate.TourMate.Adapters.MomentModel;
import com.example.lazy_programmer.tourmate.TourMate.AddEventActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class MomentFragment extends Fragment {


    public MomentFragment() {
        // Required empty public constructor
    }

    private ListView momentListView;
    private ArrayList<MomentModel> moments;
    private DatabaseReference dbReference;
    private String uid,key;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.tourmate_fragment_moment, container, false);
        setHasOptionsMenu(true);
        momentListView= (ListView) v.findViewById(R.id.momentListView);
        key=getActivity().getIntent().getStringExtra("Key");

        moments =new ArrayList<>();
        // moments.add(new ExpenseModel("29 Aug 2:30 pm","Snacks","200"));



        getAndSetAllData();


        return v;
    }


//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//
//
//
//            String eventId=eventList.get(item.getGroupId()).getKey().toString();
//            dbReference=FirebaseDatabase.getInstance().getReference().child("data").child(uid).child("travel_event_list").child(eventId);
//            dbReference.child("Budget").setValue(null);
//            dbReference.child("From Date").setValue(null);
//            dbReference.child("To Date").setValue(null);
//            dbReference.child("Location Name").setValue(null);
//            getAndSetAllData();
//            // eventList.remove(item.getGroupId());
//            Toast.makeText(getActivity(), ""+item.toString()+" "+item.getGroupId(), Toast.LENGTH_SHORT).show();
//
//        return false;
//    }


    private void getAndSetAllData() {
        try {
            uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString().trim();
            dbReference= FirebaseDatabase.getInstance().getReference().child("data").child(uid).child("moments").child(key);


            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    moments.clear();
                    for (DataSnapshot ds:dataSnapshot.getChildren()) {
                        String k=ds.getKey().toString();
                        moments.add(new MomentModel(ds.getValue().toString(),k,uid,key));

                    }

                    try {

                        Collections.sort(moments, new Comparator<MomentModel>() {
                            @Override
                            public int compare(MomentModel o1, MomentModel o2) {
                                try {
                                    return new SimpleDateFormat("dd MMM hh:mm a").parse(o1.getImgName()).compareTo(new SimpleDateFormat("dd MMM hh:mm a").parse(o2.getImgName()));
                                } catch (Exception e) {

                                }

                                return 0;
                            }
                        });
                        Collections.reverse(moments);

                        MomentAdapter adapter=new MomentAdapter(getActivity(), moments);
                       // Toast.makeText(getActivity(), "I am here now", Toast.LENGTH_SHORT).show();
                        momentListView.setAdapter(adapter);


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
       // Toast.makeText(getActivity(), "I am here now", Toast.LENGTH_SHORT).show();


    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main1,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
