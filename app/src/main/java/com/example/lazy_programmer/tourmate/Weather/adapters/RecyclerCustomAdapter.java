package com.example.lazy_programmer.tourmate.Weather.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lazy_programmer.tourmate.R;
import com.example.lazy_programmer.tourmate.Weather.DownloadImage;

import java.util.ArrayList;

/**
 * Created by lazy-programmer on 9/21/17.
 */


public class RecyclerCustomAdapter extends RecyclerView.Adapter<RecyclerCustomAdapter.WeatherViewHolder> {

    private Context context;
    private ArrayList<RecyclerWeather> weathers;

    public RecyclerCustomAdapter(Context context, ArrayList<RecyclerWeather> weathers) {
        this.context = context;
        this.weathers = weathers;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.weather_recycler_row,parent,false);
        return new WeatherViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        holder.localTimeTv.setText(weathers.get(position).getLocalTime());
        holder.humidityTv.setText(weathers.get(position).getHumidity());
        new DownloadImage(holder.icon).execute(weathers.get(position).getIcon());
        //holder.icon.setImageResource(R.drawable.up_arrow);

    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder{

        static ImageView icon;
        static TextView localTimeTv;
        static TextView humidityTv;

        public WeatherViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.recyclerImageView);
            localTimeTv = (TextView) itemView.findViewById(R.id.recyclerTimeTv);
            humidityTv = (TextView) itemView.findViewById(R.id.recyclerTempTv);

        }
    }

}
