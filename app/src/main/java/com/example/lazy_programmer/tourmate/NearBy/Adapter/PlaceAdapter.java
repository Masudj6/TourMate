package com.example.lazy_programmer.tourmate.NearBy.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazy_programmer.tourmate.NearBy.PlaceDetailsActivity;
import com.example.lazy_programmer.tourmate.R;
import com.example.lazy_programmer.tourmate.TourMate.Adapters.MomentModel;
import com.example.lazy_programmer.tourmate.Weather.DownloadImage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lazy-Programmer on 10/25/2017.
 */

public class PlaceAdapter extends ArrayAdapter<PlaceModel> {

    private Context context;
    private ArrayList<PlaceModel> places;

    public PlaceAdapter(Context context, ArrayList<PlaceModel> places) {
        super(context, R.layout.nearby_place_row_design, places);
      //  Toast.makeText(context, "I am here now", Toast.LENGTH_SHORT).show();
        this.context=context;
        this.places = places;
    }
    private static class ViewHolder{
        TextView nameTv,addressTv,ratingTv;
        ImageView img;
    }

    ViewHolder viewHolder;
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.nearby_place_row_design, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.nameTv = (TextView) convertView.findViewById(R.id.placeNameTv);
                viewHolder.addressTv = (TextView) convertView.findViewById(R.id.placeAddressTv);
                viewHolder.ratingTv = (TextView) convertView.findViewById(R.id.placeRatingTv);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.placeIcon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.nameTv.setText(places.get(position).getPlaceName());
            viewHolder.addressTv.setText("Address: " + places.get(position).getPlaceAddress());
            viewHolder.ratingTv.setText("Rating: " + places.get(position).getPlaceRating());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(context, PlaceDetailsActivity.class);
                i.putExtra("id",places.get(position).getPlaceId());
                i.putExtra("name",places.get(0).getPlaceName());
                i.putExtra("address",places.get(0).getPlaceAddress());
                context.startActivity(i);

            }
        });
//            ImageDownload image = new ImageDownload(context, viewHolder.img);
//            image.execute(places.get(position).getPlaceIcon());

        Picasso.with(context) // Context
                .load(places.get(position).getPlaceIcon()) // URL or file
                .into(viewHolder.img);



        //Toast.makeText(context, ""+places.get(position).getPlaceIcon(), Toast.LENGTH_SHORT).show();



        return convertView;
    }
}
