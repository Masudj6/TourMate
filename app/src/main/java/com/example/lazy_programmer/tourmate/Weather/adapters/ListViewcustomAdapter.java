package com.example.lazy_programmer.tourmate.Weather.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lazy_programmer.tourmate.R;
import com.example.lazy_programmer.tourmate.Weather.DownloadImage;


import java.util.ArrayList;

public class ListViewcustomAdapter extends ArrayAdapter<ListViewWeather> {
    private Context context;
    private ArrayList<ListViewWeather> weatherlist;


    public ListViewcustomAdapter(@NonNull Context context, ArrayList<ListViewWeather> weatherlist) {
        super(context, R.layout.weather_listview_row,weatherlist );
        this.context=context;
        this.weatherlist=weatherlist;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder v;

        if(convertView==null){

            LayoutInflater layoutInflater= LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.weather_listview_row,parent,false);
            v=new ViewHolder();
            v.day= (TextView) convertView.findViewById(R.id.listViewDayTv);
            v.max= (TextView) convertView.findViewById(R.id.listViewmaxTv);
            v.min= (TextView) convertView.findViewById(R.id.listViewMinTv);
            v.icon= (ImageView) convertView.findViewById(R.id.listViewImageView);


            convertView.setTag(v);

        }else{
            v= (ViewHolder) convertView.getTag();
        }

        new DownloadImage(v.icon).execute(weatherlist.get(position).getIcon());
        v.day.setText(weatherlist.get(position).getDay());
        v.max.setText(weatherlist.get(position).getMaxTemp());
        v.min.setText(weatherlist.get(position).getMinTemp());

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i=new Intent(context,DetailsActivity.class);
//                i.putExtra("Image",songList.get(position).getImageId());
//                i.putExtra("Title",songList.get(position).getSongTitle());
//                i.putExtra("Singer",songList.get(position).getSingerName());
//                i.putExtra("D",songList.get(position).getDuration());
//                context.startActivity(i);
//            }
//        });

        return convertView;
    }


    private static class ViewHolder{
        static ImageView icon;

        static TextView day,max,min;
    }

}