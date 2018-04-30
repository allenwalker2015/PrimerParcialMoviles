package com.allen.primerparcialmoviles;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import com.allen.primerparcialmoviles.Data.Contact;

import java.util.ArrayList;

public class ContactsProvider {
    private AppCompatActivity activity;

    public ContactsProvider(AppCompatActivity activity) {
            this.activity = activity;
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
        Cursor phones = activity.getContentResolver().query(uri, null, null, null, sort);
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
        Cursor cur1 = activity.getContentResolver().query(
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
        Cursor pCur = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
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
        Cursor postal_cursor = activity.getContentResolver().query(postal_uri, null, ContactsContract.Data.CONTACT_ID + "=" + id, null, null);
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
