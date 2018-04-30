package com.allen.primerparcialmoviles;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.allen.primerparcialmoviles.Adapter.ContactAdapter;
import com.allen.primerparcialmoviles.Data.Contact;

import java.util.ArrayList;

public class MainActivity extends RuntimePermission {
    private static final int REQUEST_PERMISSION = 10;
    RecyclerView rv;
    GridLayoutManager gl;
    ContactAdapter ca;
    private ArrayList<Contact> contactlist;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    Toast.makeText(getBaseContext(), "CLICK EN EL HOME", Toast.LENGTH_SHORT).show();

                    return true;

                case R.id.navigation_favorites:
//                    mTextMessage.setText(R.string.title_favorites);
                    Toast.makeText(getBaseContext(), "CLICK EN EL FAVS", Toast.LENGTH_SHORT).show();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
        // putting recyclerview position
        savedInstanceState.putParcelable("RV", listState);
        // putting recyclerview items
        savedInstanceState.putSerializable("contact_list", contactlist);
        super.onSaveInstanceState(savedInstanceState);
    }


    public void restorePreviousState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        LinearLayoutManager.SavedState mListState = savedInstanceState.getParcelable("RV");
        // getting recyclerview items
        contactlist = (ArrayList<Contact>) savedInstanceState.getSerializable("contact_list");
        // Restoring adapter items
        ca = new ContactAdapter(contactlist) {
            @Override
            public void infoOnClickListener(Contact c) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                Intent intent = new Intent(getBaseContext(), ContactInfo.class);
                intent.putExtra("Contact", c);
                startActivity(intent);
            }
        };
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
        contactlist = findContacts();
        rv = findViewById(R.id.list_container);
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
    }

    public ArrayList<Contact> findContacts() {
        String name;
        ArrayList<String> emails, numbers, addresses;
        ArrayList<Contact> contactlist = new ArrayList<>();
        Uri image;
        String id;

        // CURSOR PARAMS

        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        //SET CURSOR
        Cursor phones = getContentResolver().query(uri, null, null, null, sort);
        //MOVING
        while (phones.moveToNext()) {

            name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            String nav = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));

            numbers = getPhoneNumbers(id);

            emails = getEmails(id);

            addresses = getAddress(id);

            //Si la columna starred tiene 1 es que el contacto del telefono es favorito
            boolean fav = (phones.getString(phones.getColumnIndex(ContactsContract.Data.STARRED))).equals("1");
            contactlist.add(new Contact(id, name, numbers, emails, nav, fav));

            //Log.d("TAM", "findContacts: "+ contactlist.size());
        }
        phones.close();
        return contactlist;
    }

    public ArrayList<String> getEmails(String id) {
        ArrayList<String> emails = new ArrayList<>();
        Cursor cur1 = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{id}, null);
        while (cur1.moveToNext()) {
            //to get the contact names
            // String email2 = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DISPLAY_NAME));
            String email = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

            emails.add(email);
        }
        // Log.d( "emails-size: " , emails.size() + "");
        cur1.close();
        return emails;
    }

    public ArrayList<String> getPhoneNumbers(String id) {
        ArrayList<String> numbers = new ArrayList<>();
        Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
        while (pCur.moveToNext()) {
            String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            numbers.add(contactNumber);
            //Log.d("NUMBER_SIZE_INTERNO", "findContacts: " + numbers.size());
            break;
        }
        //Log.d("NUMBER_SIZE", "findContacts: " + numbers.size());
        pCur.close();
        return numbers;
    }


    public ArrayList<String> getAddress(String id) {
        Uri postal_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
        Cursor postal_cursor = getContentResolver().query(postal_uri, null, ContactsContract.Data.CONTACT_ID + "=" + id, null, null);
        ArrayList<String> addresses = new ArrayList<>();
        while (postal_cursor.moveToNext()) {
            String address = "";
            String street = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
            String city = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            String country = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
            address = street + "," + city + ',' + country;
            addresses.add(address);

        }
        postal_cursor.close();
        //Log.d("getAddress: ", addresses.size() + "");
        return addresses;
    }


}
