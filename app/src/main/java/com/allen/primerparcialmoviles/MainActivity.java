package com.allen.primerparcialmoviles;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.allen.primerparcialmoviles.Adapter.ContactAdapter;
import com.allen.primerparcialmoviles.ContentProviders.ContactsProvider;
import com.allen.primerparcialmoviles.Data.Contact;

import java.util.ArrayList;

public class MainActivity extends RuntimePermission {
    private static final int REQUEST_PERMISSION = 10;
    static final int NEW_CONTACT_REQUEST = 1;
    RecyclerView rv;
    GridLayoutManager gl;
    ContactAdapter ca;
    ContactAdapter fca;
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
                            Manifest.permission.READ_CONTACTS,Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    R.string.msg, REQUEST_PERMISSION);
        } else {
            restorePreviousState(savedInstanceState); // Restore data found in the Bundle
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            Parcelable listState = rv.getLayoutManager().onSaveInstanceState();
            // Se guarda el estado de la lista
            savedInstanceState.putParcelable("RV", listState);
            // Se guardan los items
            savedInstanceState.putSerializable("contact_list", contactlist);
            //savedInstanceState.putSerializable("contact_adapter",ca);
            // if(fca!=null) {

            //    savedInstanceState.putSerializable("filtered_adapter", fca);
            //}
        }
    }
        super.onSaveInstanceState(savedInstanceState);
    }


    public void restorePreviousState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        LinearLayoutManager.SavedState mListState = savedInstanceState.getParcelable("RV");
        // Se obtienen los items de la lista
        contactlist = (ArrayList<Contact>) savedInstanceState.getSerializable("contact_list");
        BottomNavigationView navigation =  findViewById(R.id.navigation);
        // Restoring adapter items

        confSearch();

        rv = findViewById(R.id.list_container);


        if(navigation.getSelectedItemId() == R.id.navigation_favorites){

           //fca = (ContactAdapter)savedInstanceState.getSerializable("filtered_adapter");
            fca = new ContactAdapter(getFavs()) {
                @Override
                public void infoOnClickListener(Contact c) {
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    Intent intent = new Intent(getBaseContext(), ContactInfo.class);
                    intent.putExtra("Contact", c);
                    startActivity(intent);
                }
            };

            rv.setAdapter(fca);
            fca.getFilter().filter(sv.getQuery());
            fca.notifyDataSetChanged();
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
            rv.setAdapter(ca);
            ca.getFilter().filter(sv.getQuery());
            ca.notifyDataSetChanged();
        }
        setNewContactListener();
        confNavigator(navigation);
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
        confSearch();
        setNewContactListener();
        confNavigator(navigation);



    }

    public void confNavigator(BottomNavigationView navigation ){
        sv = findViewById(R.id.prim_search_bar);
        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
//                        ca.list = contactlist;
//                        ca.notifyDataSetChanged();
                        rv.swapAdapter(ca,false);
                        if(ca==null){
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
                        ca.getFilter().filter(sv.getQuery());

//                        Toast.makeText(getBaseContext(), "CLICK EN EL HOME", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.navigation_favorites:

//                        ca.list = getFavs();
//                        ca.notifyDataSetChanged();
//                        if(fca==null) {
                            fca = new ContactAdapter(getFavs()) {
                                @Override
                                public void infoOnClickListener(Contact c) {
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    Intent intent = new Intent(getBaseContext(), ContactInfo.class);
                                    intent.putExtra("Contact", c);
                                    startActivity(intent);
                                }
                            };
//                        }
//                        else {
//
//                            fca.list = getFavs();
//                            fca.notifyDataSetChanged();
//
//                        }
                        fca.getFilter().filter(sv.getQuery());
                        fca.notifyDataSetChanged();
                        rv.swapAdapter(fca,true);
                        //Toast.makeText(getBaseContext(), "CLICK EN EL FAVS", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        };
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    public void confSearch(){
        final BottomNavigationView navigation =  findViewById(R.id.navigation);
        sv = findViewById(R.id.prim_search_bar);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                switch (navigation.getSelectedItemId()){
                    case R.id.navigation_home:
                        ca.getFilter().filter(query);
                    break;
                    case R.id.navigation_favorites:
                        fca.getFilter().filter(query);
                        break;
                }


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


    public void setNewContactListener(){
        ImageButton newcontact = findViewById(R.id.add_contact);
        newcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),EditContact.class);
                startActivityForResult(intent,NEW_CONTACT_REQUEST);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == NEW_CONTACT_REQUEST) {

            if (resultCode == RESULT_OK) {
                Contact c = (Contact)data.getSerializableExtra("new_contact");
                contactlist.add(c);
                ca.notifyItemInserted(contactlist.size());
                //ca.notifyDataSetChanged();
                //fca.notifyDataSetChanged();
            }
        }
    }
}
