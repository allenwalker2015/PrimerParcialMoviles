package com.allen.primerparcialmoviles.Data;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contact implements Serializable{
    private String id;
    private String name;
    private ArrayList<String> number,emails;
    private Uri picture;
    private boolean favorite;

    public Contact(String id,String name, ArrayList<String> number,ArrayList<String> emails, Uri picture, boolean favorite) {
        this.name = name;
        this.number = number;
        this.picture = picture;
        this.favorite = favorite;
        this.emails = emails;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getNumber() {
        return number;
    }

    public void setNumber(ArrayList<String> number) {
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

    public ArrayList<String> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }
}
