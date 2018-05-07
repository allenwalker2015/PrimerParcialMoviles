package com.allen.primerparcialmoviles.Activities;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.allen.primerparcialmoviles.Adapter.InfoAdapter;
import com.allen.primerparcialmoviles.Data.Contact;
import com.allen.primerparcialmoviles.Data.URIPath;
import com.allen.primerparcialmoviles.Fragments.EmailFragment;
import com.allen.primerparcialmoviles.Fragments.PhonesFragment;
import com.allen.primerparcialmoviles.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactInfo extends AppCompatActivity {
    ImageView image;
    RecyclerView rv;
    int index;
    android.support.v7.widget.Toolbar cl;
    android.support.design.widget.TabLayout tl;
    ImageButton edit,delete;
    PhonesFragment phones_fragment;
    EmailFragment mails_fragment;
    Contact c;
    final int EDIT_CONTACT_RESULT=30;
    final int REMOVE_CONTACT_RESULT = 31;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info_dialog);
        c = (Contact)getIntent().getSerializableExtra("Contact");
        index = getIntent().getIntExtra("index",0);
        image = findViewById(R.id.picture_imageview);
        image.setOnClickListener(null);
        tl = findViewById(R.id.info_tabs);
        rv = findViewById(R.id.info_recycler);
        rv.setNestedScrollingEnabled(false);
        rv.setHasFixedSize(true);
        edit= findViewById(R.id.button_edit_contact);
        delete = findViewById(R.id.button_remove_contact);
        setInfo();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("delete_contact_index", index);
                setResult(REMOVE_CONTACT_RESULT, returnIntent);
                finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ContactInfo.this, "HAS HECHO CLICK", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(),EditContact.class);
                intent.putExtra("Contact", c);
                startActivityForResult(intent,EDIT_CONTACT_RESULT);
            }
        });

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        if(phones_fragment==null)phones_fragment = new PhonesFragment().newInstance(c);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_info,phones_fragment);
                        //transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case 1:
                        if(mails_fragment==null)mails_fragment = new EmailFragment().newInstance(c);
                        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                        transaction1.replace(R.id.frame_info,mails_fragment);
                        //transaction1.addToBackStack(null);
                        transaction1.commit();
                        break;
                    case 2:
                        break;
                }

            }



            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        phones_fragment = new PhonesFragment().newInstance(c);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_info,phones_fragment);
        transaction.commit();
    }



    public void setInfo(){
        HashMap<Integer,Pair> m1 = new HashMap();
        int i=0;
        if(c.getName().size()>0) {
            m1.put(i++, new Pair(R.string.info_name, c.getName().get(1)));
            if (c.getName().get(2) != null)
                m1.put(i++, new Pair(R.string.last_name, c.getName().get(2)));
        }
        if(c.getAddress()!=null) {
            m1.put(i++, new Pair(R.string.info_address, c.getAddress()));
        }
       // Log.d("CUMPLE",c.getBirth().toString());
        if(c.getBirth()!=null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            m1.put(i++, new Pair(R.string.info_birthday, format.format(c.getBirth())));
        }
        InfoAdapter infoAdapter = new InfoAdapter(m1);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(infoAdapter);

        cl = findViewById(R.id.toolbar);
        if(cl!=null) {
            if (c.getName().size() > 0) {
                cl.setTitle(c.getName().get(0));
            } else {
                if (c.getNumber().size() > 0) {
                    cl.setTitle((new ArrayList<String>(c.getNumber().values())).get(0));
                } else if (c.getEmails().size() > 0) {
                    cl.setTitle(c.getEmails().get(0));
                } else {
                    cl.setTitle("EMPTY CONTACT");
                }

            }
        }
        if(c.getPicture()!=null){
            if(c.getPicture().contains("com.android.contacts")){
                image.setImageURI(Uri.parse(c.getPicture()));
            }
            else {


                Uri uri = Uri.parse(c.getPicture());
                Glide.with(this).load(new File(URIPath.getRealPathFromURI(this, uri))).
                        into(image);
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_CONTACT_RESULT) {

            if (resultCode == RESULT_OK) {
                c = (Contact)data.getSerializableExtra("new_contact");
                setInfo();
                phones_fragment = new PhonesFragment().newInstance(c);
            }

        }

    }
}
