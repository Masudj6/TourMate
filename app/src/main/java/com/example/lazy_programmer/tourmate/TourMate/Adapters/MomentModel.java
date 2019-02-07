package com.example.lazy_programmer.tourmate.TourMate.Adapters;

/**
 * Created by Lazy-Programmer on 10/24/2017.
 */

public class MomentModel {
     private  String imgCaption,imgName,uid,key;

    public MomentModel(String imgCaption,String imgName, String uid, String key) {
        this.imgCaption = imgCaption;
        this.uid = uid;
        this.key = key;
        this.imgName=imgName;
    }

    public MomentModel() {
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgCaption() {
        return imgCaption;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setImgCaption(String imgCaption) {
        this.imgCaption = imgCaption;
    }
}
