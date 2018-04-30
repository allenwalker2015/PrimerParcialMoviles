package com.allen.primerparcialmoviles.Filter;

import android.widget.Filter;

import com.allen.primerparcialmoviles.Adapter.ContactAdapter;
import com.allen.primerparcialmoviles.Data.Contact;

import java.util.ArrayList;

public class ContactFilter extends Filter{
    ContactAdapter adapter;
    ArrayList<Contact> filterList;
    Boolean onlyfavs;

    public ContactFilter(ArrayList<Contact> filterList, ContactAdapter adapter, Boolean onlyfavs)
    {
        this.adapter=adapter;
        this.filterList=filterList;
        this.onlyfavs = onlyfavs;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Contact> filteredContacts=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK IF ONLY FAVS
                if(onlyfavs){
                    if (filterList.get(i).getName().toUpperCase().contains(constraint) && filterList.get(i).isFavorite()) {
                        //ADD PLAYER TO FILTERED PLAYERS
                        filteredContacts.add(filterList.get(i));
                    }
                }
                //CHECK
                else {
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        //ADD PLAYER TO FILTERED PLAYERS
                        filteredContacts.add(filterList.get(i));
                    }
                }
            }

            results.count=filteredContacts.size();
            results.values=filteredContacts;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.list= (ArrayList<Contact>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }

    public Boolean getOnlyfavs() {
        return onlyfavs;
    }

    public void setOnlyfavs(Boolean onlyfavs) {
        this.onlyfavs = onlyfavs;
    }
}
