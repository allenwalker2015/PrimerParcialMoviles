package com.allen.primerparcialmoviles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.allen.primerparcialmoviles.Adapter.PhoneAdapter;
import com.allen.primerparcialmoviles.Data.Contact;

import java.util.ArrayList;

public class ContactInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info_dialog);
        Contact c = (Contact)getIntent().getSerializableExtra("Contact");
        RecyclerView rv = findViewById(R.id.phones_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(lm);
        rv.setAdapter(new PhoneAdapter(this,c.getNumber()));

    }



}
