package com.allen.primerparcialmoviles;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.allen.primerparcialmoviles.Adapter.ContactAdapter;
import com.allen.primerparcialmoviles.Data.Contact;

import java.util.ArrayList;

public class MainActivity extends RuntimePermission {
    private static final int REQUEST_PERMISSION = 10;
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


        requestAppPermissions(new String[]{
                        Manifest.permission.READ_CONTACTS,
                      },
                R.string.msg,REQUEST_PERMISSION);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        //Do anything when permisson granted
        Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();

        findContacts();

        rv = findViewById(R.id.list_container);
        ca = new ContactAdapter(contactlist) {
            @Override
            public void infoOnClickListener(View v, ViewHolder vh) {
                AlertDialog.Builder mBuild = new AlertDialog.Builder(MainActivity.this);
                View v2 = getLayoutInflater().inflate(R.layout.contact_card,null);
                mBuild.setView(v2);
                AlertDialog dialog = mBuild.create();
                dialog.show();
            }

            @Override
            public void starOnClickListener(View v, ViewHolder vh) {

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

            String nav = phones.getString(
                    phones.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
            if(nav != null){
                image = Uri.parse(phones.getString(
                        phones.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI)));
            }
            else image = null;


            boolean fav = (phones.getString(
                    phones.getColumnIndex(ContactsContract.Data.STARRED))).equals("1")?true:false;

            contactlist.add(new Contact(name,number,image, fav));
        }
        phones.close();
       // Log.d("TAM", "findContacts: "+ contactlist.size());
    }

}
