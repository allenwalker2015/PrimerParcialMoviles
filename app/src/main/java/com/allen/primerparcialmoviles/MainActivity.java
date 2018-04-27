package com.allen.primerparcialmoviles;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.allen.primerparcialmoviles.Adapter.ContactAdapter;
import com.allen.primerparcialmoviles.Data.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Contact> contactlist;
    RecyclerView rv;
    GridLayoutManager gl;
    ContactAdapter ca;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    Toast.makeText(getBaseContext(),"CLICK EN EL HOME",Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.navigation_favorites:
//                    mTextMessage.setText(R.string.title_favorites);
                    Toast.makeText(getBaseContext(),"CLICK EN EL FAVS",Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        findContacts();

        rv = findViewById(R.id.list_container);
        ca = new ContactAdapter(contactlist) {
            @Override
            public void infoOnClickListener(View v, ViewHolder vh, Contact c) {

            }

            @Override
            public void starOnClickListener(View v, ViewHolder vh, Contact c) {

            }
        };
        gl = new GridLayoutManager(this,3);
        rv.setLayoutManager(gl);
        rv.setAdapter(ca);


    }

    public void findContacts(){
        contactlist = new ArrayList<>();
        Cursor phones = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC"
        );


        String name,number;
        Uri image;

        while (phones.moveToNext())
        {
            name = phones.getString(
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            number = phones.getString(
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.d("IMAGEN", "findContacts: "+phones.getString(
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)));
            String nav = phones.getString(
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
            if(nav != null){
                image = Uri.parse(phones.getString(
                        phones.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)));
            }
            else image = null;


            boolean fav = (phones.getString(
                    phones.getColumnIndex(ContactsContract.Data.STARRED))).equals("1")?false:true;
            Log.d("FAVORITO",""+fav);
            contactlist.add(new Contact(name,number,image, fav));
        }
        phones.close();
        Log.d("TAM", "findContacts: "+ contactlist.size());
    }

}
