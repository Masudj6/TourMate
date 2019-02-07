package com.example.lazy_programmer.tourmate.NearBy.Adapter;

/**
 * Created by Lazy-Programmer on 10/25/2017.
 */

public class PlaceModel {

    private String placeName,placeAddress,placeIcon,placeId, placeRating;

    public PlaceModel(String placeName, String placeAddress, String placeIcon, String placeId,String placeRating) {
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.placeIcon = placeIcon;
        this.placeId = placeId;
        this.placeRating=placeRating;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlaceIcon() {
        return placeIcon;
    }

    public String getPlaceRating() {
        return placeRating;
    }

    public void setPlaceRating(String placeRating) {
        this.placeRating = placeRating;
    }

    public void setPlaceIcon(String placeIcon) {
        this.placeIcon = placeIcon;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
