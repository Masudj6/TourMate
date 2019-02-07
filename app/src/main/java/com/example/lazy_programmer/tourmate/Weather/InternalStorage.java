package com.example.lazy_programmer.tourmate.Weather;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by lazy-programmer on 9/23/17.
 */

public final class InternalStorage{

    private InternalStorage() {}

    public static void writeObject(Context context, String key, Object weatherModel) {
        try {
            FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(weatherModel);
            oos.close();
            fos.close();
        }catch(Exception e){

        }

    }

    public static Object readObject(Context context, String key) {
        Object data=null;
        try {
            FileInputStream fis = context.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = ois.readObject();


        }catch(Exception e){

        }
        return data;

    }
}