package com.allen.primerparcialmoviles.Data;

import android.net.Uri;

public class Contact {
    private String name;
    private String number;
    private Uri picture;
    private boolean favorite;

    public Contact(String name, String number, Uri picture, boolean favorite) {
        this.name = name;
        this.number = number;
        this.picture = picture;
        this.favorite = favorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Uri getPicture() {
        return picture;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
