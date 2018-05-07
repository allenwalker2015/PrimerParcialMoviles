package com.allen.primerparcialmoviles.Data;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class Contact implements Serializable{
    private String id;
    //private String name;
    private ArrayList<String> emails,name;
    private ArrayList<ArrayList<String>> number;
    private String picture,address;
    private boolean favorite;
    private Date birth;

    public Contact() {
    }

    public Contact(String id, ArrayList<String> name, ArrayList<ArrayList<String>> number, ArrayList<String> emails, String address, String picture, boolean favorite, Date birth) {
        this.name = name;
        this.number = number;
        this.address = address;
        this.picture = picture;
        this.favorite = favorite;
        this.emails = emails;
        this.id = id;
        this.birth = birth;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public ArrayList<ArrayList<String>> getNumber() {
        return number;
    }

    public void setNumber(ArrayList<ArrayList<String>> number) {
        this.number = number;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
