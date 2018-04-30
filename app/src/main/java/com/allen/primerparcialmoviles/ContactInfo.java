package com.allen.primerparcialmoviles;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.primerparcialmoviles.Adapter.PhoneAdapter;
import com.allen.primerparcialmoviles.Data.Contact;


public class ContactInfo extends AppCompatActivity {
    ImageView image;
    TextView  name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info_dialog);
        Contact c = (Contact)getIntent().getSerializableExtra("Contact");
        image = findViewById(R.id.picture_imageview);
        name = findViewById(R.id.name_textview);
        if(c.getPicture()!=null)image.setImageURI(Uri.parse(c.getPicture()));

        name.setText(c.getName());
        RecyclerView rv = findViewById(R.id.phones_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(lm);
        Log.d("NUMERO_EN_INFO", "onCreate: "+c.getNumber().size());
        PhoneAdapter pa = new PhoneAdapter(this,c.getNumber());
        Log.d("LISTA_EN_INFO", "onCreate: "+pa.getItemCount());
        rv.setAdapter(pa);
       // overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
    }
}
