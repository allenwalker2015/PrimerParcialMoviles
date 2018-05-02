package com.allen.primerparcialmoviles.ContentProviders;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.allen.primerparcialmoviles.Data.Contact;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ContactsProvider {
    private AppCompatActivity activity;

    public ContactsProvider(AppCompatActivity activity) {
            this.activity = activity;
    }

    public ArrayList<Contact> findContacts() {
        String name;
        ArrayList<String> emails, numbers, addresses,birthday;
        ArrayList<Contact> contactlist = new ArrayList<>();
        Uri image;
        String id;

        // CURSOR PARAMS

        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //SET CURSOR
        Cursor phones = activity.getContentResolver().query(uri, null, null, null, sort);
        //MOVING
        while (phones.moveToNext()) {

            name = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            id = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
            String nav = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO_URI));

            numbers = getPhoneNumbers(id);

            emails = getEmails(id);

            addresses = getAddress(id);



            //Si la columna starred tiene 1 es que el contacto del telefono es favorito
            boolean fav = (phones.getString(phones.getColumnIndex(ContactsContract.Contacts.STARRED))).equals("1");
            contactlist.add(new Contact(id, name, numbers, emails, addresses, nav, fav,getBirthday(id)));

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
        Cursor pCur = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[] { id }, null);
        while (pCur.moveToNext()) {
            int type = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            String s_type = String.valueOf(ContactsContract.CommonDataKinds.Phone.getTypeLabel(activity.getResources(),type,""));
            String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            numbers.add(contactNumber);
            //Log.d("NUMBER_SIZE_INTERNO", "findContacts: " + numbers.size());
            //break;
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
            Log.d("getAddress: ", address + "");
        }
        postal_cursor.close();
        Log.d("getAddress: ", addresses.size() + "");
        return addresses;
    }


    public Date getBirthday(String id){
        String columns[] = {
                ContactsContract.CommonDataKinds.Event.START_DATE,
                ContactsContract.CommonDataKinds.Event.TYPE,
                ContactsContract.CommonDataKinds.Event.MIMETYPE,
        };
        String birthday= "";
        String where = ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' and "
                + ContactsContract.Data.CONTACT_ID + " = " + id;

        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

        Cursor birthdayCur = activity.getContentResolver().query(ContactsContract.Data.CONTENT_URI, columns, where, selectionArgs, sortOrder);
        if (birthdayCur.getCount() > 0) {
            while (birthdayCur.moveToNext()) {
                birthday = birthdayCur.getString(birthdayCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
            }
        }
        birthdayCur.close();

        Log.d("CUMPLE", "getBirthday: "+birthday);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = format.parse(birthday);
            Log.d("FECHA",date.toString());
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("ERROR", "getBirthday: ERROR");
            return null;
        }

    }


}
