package com.allen.primerparcialmoviles;

import android.Manifest;
import android.content.Intent;

import android.os.Bundle;
import android.os.Parcelable;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.allen.primerparcialmoviles.Adapter.ContactAdapter;
import com.allen.primerparcialmoviles.ContentProviders.ContactsProvider;
import com.allen.primerparcialmoviles.Data.Contact;

import java.util.ArrayList;

public class MainActivity extends RuntimePermission {
    private static final int REQUEST_PERMISSION = 10;
    RecyclerView rv;
    GridLayoutManager gl;
    ContactAdapter ca;
    SearchView sv;
    ContactsProvider cp;
    private ArrayList<Contact> contactlist;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            requestAppPermissions(new String[]{
                            Manifest.permission.READ_CONTACTS,
                    },
                    R.string.msg, REQUEST_PERMISSION);
        } else {
            restorePreviousState(savedInstanceState); // Restore data found in the Bundle
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        Parcelable listState = rv.getLayoutManager().onSaveInstanceState();
        // Se guarda el estado de la lista
        savedInstanceState.putParcelable("RV", listState);
        // Se guardan los items
        savedInstanceState.putSerializable("contact_list", contactlist);
        super.onSaveInstanceState(savedInstanceState);
    }


    public void restorePreviousState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        LinearLayoutManager.SavedState mListState = savedInstanceState.getParcelable("RV");
        // Se obtienen los items de la lista
        contactlist = (ArrayList<Contact>) savedInstanceState.getSerializable("contact_list");
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        // Restoring adapter items
        if(navigation.getSelectedItemId() == R.id.navigation_favorites){
            ca = new ContactAdapter(getFavs()) {
                @Override
                public void infoOnClickListener(Contact c) {
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Intent intent = new Intent(getBaseContext(), ContactInfo.class);
                    intent.putExtra("Contact", c);
                    startActivity(intent);
                }
            };
        }
        else {
            ca = new ContactAdapter(contactlist) {
                @Override
                public void infoOnClickListener(Contact c) {
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Intent intent = new Intent(getBaseContext(), ContactInfo.class);
                    intent.putExtra("Contact", c);
                    startActivity(intent);
                }
            };
        }
        confSearch();
        confNavigator(navigation);
        rv = findViewById(R.id.list_container);
        rv.setAdapter(ca);
        rv.setLayoutManager(new GridLayoutManager(this,3));
        // Restoring recycler view position
        rv.getLayoutManager().onRestoreInstanceState(mListState);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        //Do anything when permisson granted
        Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
        cp = new ContactsProvider(this);
        contactlist = cp.findContacts();
        rv = findViewById(R.id.list_container);
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        ca = new ContactAdapter(contactlist) {
            @Override
            public void infoOnClickListener(Contact c) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Intent intent = new Intent(getBaseContext(), ContactInfo.class);
                intent.putExtra("Contact", c);
                startActivity(intent);
            }
        };

        gl = new GridLayoutManager(this, 3);
        rv.setLayoutManager(gl);
        rv.setAdapter(ca);
        confNavigator(navigation);
        confSearch();


    }

    public void confNavigator(BottomNavigationView navigation ){

        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        ca.list = contactlist;
                        ca.notifyDataSetChanged();


                        Toast.makeText(getBaseContext(), "CLICK EN EL HOME", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_favorites:

                        ca.list = getFavs();
                        ca.notifyDataSetChanged();
                        Toast.makeText(getBaseContext(), "CLICK EN EL FAVS", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void confSearch(){
        sv = findViewById(R.id.prim_search_bar);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                ca.getFilter().filter(query);
                return false;
            }
        });
    }
    public ArrayList<Contact> getFavs(){
        ArrayList<Contact> favs= new ArrayList<>();
        for(Contact contact:contactlist){
            if(contact.isFavorite()){
                favs.add(contact);
            }
        }
        return favs;
    }


}
