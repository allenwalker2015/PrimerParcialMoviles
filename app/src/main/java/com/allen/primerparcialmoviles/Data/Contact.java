package com.allen.primerparcialmoviles.Data;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class Contact implements Serializable{
    private String id;
    private String name;
    private ArrayList<String> number,emails,address;
    private String picture;
    private boolean favorite;
    private Date birth;


    public Contact(String id, String name, ArrayList<String> number, ArrayList<String> emails, ArrayList<String> address, String picture, boolean favorite, Date birth) {
        this.name = name;
        this.number = number;
        this.address = address;
        this.picture = picture;
        this.favorite = favorite;
        this.emails = emails;
        this.id = id;
        this.birth = birth;
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

    public ArrayList<String> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<String> address) {
        this.address = address;
    }
}
