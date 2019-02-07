package com.example.lazy_programmer.tourmate.TourMate.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazy_programmer.tourmate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lazy-Programmer on 10/25/2017.
 */

public class MomentAdapter extends ArrayAdapter<MomentModel> {

    private Context context;
    private ArrayList<MomentModel> moments;

    public MomentAdapter(Context context, ArrayList<MomentModel> moments) {
        super(context, R.layout.tourmate_moment_row_design, moments);
      //  Toast.makeText(context, "I am here now", Toast.LENGTH_SHORT).show();
        this.context=context;
        this.moments = moments;
    }
    private static class ViewHolder{
        TextView caption,imgDate;
        ImageView img;
    }
    private File localFile = null;
    private StorageReference storageReference;
    ViewHolder viewHolder;
    ProgressDialog load;
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        load=new ProgressDialog(context);
        load.setMessage("loading");
        load.show();
        if(convertView==null){
            LayoutInflater layoutInflater=LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.tourmate_moment_row_design,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.caption= (TextView) convertView.findViewById(R.id.momentDetailsTv);
            viewHolder.imgDate= (TextView) convertView.findViewById(R.id.imageDateTv);
            viewHolder.img= (ImageView) convertView.findViewById(R.id.momentImgView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.caption.setText(moments.get(position).getImgCaption());
        viewHolder.imgDate.setText(moments.get(position).getImgName());



        try {
            localFile = File.createTempFile("moments","jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            storageReference=FirebaseStorage.getInstance().getReference().child("moments").child(moments.get(position).getUid()).child(moments.get(position).getKey()).child(moments.get(position).getImgName()+".jpg");

            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap photo= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            viewHolder.img.setImageBitmap(photo);
                            load.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    load.dismiss();
                    // Toast.makeText(context, "Ready to open", Toast.LENGTH_SHORT).show();
                    // ...
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        convertView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0,1,1,"Delete");
            }
        });



        return convertView;
    }
}
