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
import java.util.LinkedHashMap;

public class ContactsProvider {
    private AppCompatActivity activity;

    public ContactsProvider(AppCompatActivity activity) {
            this.activity = activity;
    }

    public ArrayList<Contact> findContacts() {
        String addresses;
        ArrayList<String> emails;
        LinkedHashMap<String,String> numbers;
        ArrayList<Contact> contactlist = new ArrayList<>();
        String id;

        // CURSOR PARAMS

        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //SET CURSOR
        Cursor phones = activity.getContentResolver().query(uri, null, null, null, sort);
        //MOVING
        while (phones.moveToNext()) {

           // name = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//            String last_name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
  //          Log.d("LAST_NAME",last_name);
            id = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
            String nav = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.Photo.PHOTO_URI));

            numbers = getPhoneNumbers(id);

            emails = getEmails(id);
            ArrayList<String> names = getNames(id);
            if(names.size()==0){
                if(numbers.size()>0) {
                    String num = (new ArrayList<String>(numbers.values())).get(0);
                    names.add(num);
                }else
                if(emails.size()>0){
                    String first_email = emails.get(0);
                    names.add(first_email);
                }
            }
            addresses = getAddress(id);



            //Si la columna starred tiene 1 es que el contacto del telefono es favorito
            boolean fav = (phones.getString(phones.getColumnIndex(ContactsContract.Contacts.STARRED))).equals("1");
            contactlist.add(new Contact(id,names , numbers, emails, addresses, nav, fav,getBirthday(id)));

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

    public LinkedHashMap<String,String> getPhoneNumbers(String id) {
        LinkedHashMap<String,String> numbers = new LinkedHashMap<>();
        Cursor pCur = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[] { id }, null);
        while (pCur.moveToNext()) {
            int type = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            String s_type = String.valueOf(ContactsContract.CommonDataKinds.Phone.getTypeLabel(activity.getResources(),type,""));
            String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            numbers.put(s_type,contactNumber);
            //Log.d("NUMBER_SIZE_INTERNO", "findContacts: " + numbers.size());
            //break;
        }
        //Log.d("NUMBER_SIZE", "findContacts: " + numbers.size());
        pCur.close();
        return numbers;
    }

    public ArrayList<String> getNames(String id){
        ArrayList<String> names = new ArrayList<>();
        String whereName = whereName = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
        String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE ,id};
        Cursor nameCur = activity.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
        while (nameCur.moveToNext()) {
            String given = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
            String family = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
            String display = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
           // Log.d("NAME",given.toString());
           // Log.d("LAST_NAME",family.toString());
           // Log.d("FULL NAME",display.toString());

            names.add(display);
            names.add(given);
            names.add(family);

        }
        nameCur.close();
        return names;
    }

    public String getAddress(String id) {
        Uri postal_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
        Cursor postal_cursor = activity.getContentResolver().query(postal_uri, null, ContactsContract.Data.CONTACT_ID + "=" + id, null, null);
        String addresses=null;
        while (postal_cursor.moveToNext()) {
            String address = "";
            String street = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
            String city = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            String country = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
            if(street!=null){
                address += street;
            }
            if(city!=null){
                address+= "," + city;
            }
            if(country!=null){
                address+=',' + country;
            }
            addresses = address;
        }
        postal_cursor.close();
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

       // Log.d("CUMPLE", "getBirthday: "+birthday);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = format.parse(birthday);
            //Log.d("FECHA",date.toString());
            return date;
        } catch (ParseException e) {
            //e.printStackTrace();
           // Log.d("ERROR", "getBirthday: ERROR");
            return null;
        }

    }


}
