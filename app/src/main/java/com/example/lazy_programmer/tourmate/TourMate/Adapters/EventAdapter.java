package com.example.lazy_programmer.tourmate.TourMate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.lazy_programmer.tourmate.R;
import com.example.lazy_programmer.tourmate.TourMate.EventDetailsActivity;
import com.example.lazy_programmer.tourmate.TourMate.TourMateHomePage;

import java.net.ConnectException;
import java.util.ArrayList;

/**
 * Created by Lazy-Programmer on 10/22/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    private ArrayList<EventModel> list;
    private Context context;

    public EventAdapter(ArrayList<EventModel> list, Context context) {
        this.context=context;
        this.list = list;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tourmate_eventlist_row_design,parent,false));
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        final EventModel event=list.get(position);

        holder.eventPlacesName.setText(event.getLocationName());
        holder.customFromDate.setText("From: "+event.getFromDate());
        holder.customToDate.setText("To: "+event.getToDate());
        holder.customBudget.setText("Budget: "+event.getBudget()+" tk");

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(holder.getAdapterPosition(),0,0,"Edit");
                menu.add(holder.getAdapterPosition(),1,1,"Delete");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, EventDetailsActivity.class);
                i.putExtra("Location",event.getLocationName());
                i.putExtra("Key",event.getKey());
                i.putExtra("FromDate",event.getFromDate());
                i.putExtra("ToDate",event.getToDate());
                i.putExtra("Budget",event.getBudget());
                i.putExtra("Remaining",event.getBalanceRemaining());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();

    }




    class EventViewHolder extends RecyclerView.ViewHolder{
        private TextView eventPlacesName,customFromDate,customToDate,customBudget;
        public EventViewHolder(View itemView) {
            super(itemView);

            eventPlacesName= (TextView) itemView.findViewById(R.id.locationNameTv);
            customFromDate= (TextView) itemView.findViewById(R.id.fromDateTv);
            customToDate= (TextView) itemView.findViewById(R.id.toDateTv);
            customBudget= (TextView) itemView.findViewById(R.id.budgetTv);

        }
    }


}

