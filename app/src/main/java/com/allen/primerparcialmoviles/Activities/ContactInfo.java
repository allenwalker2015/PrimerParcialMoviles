package com.allen.primerparcialmoviles.Activities;


import android.support.v4.app.FragmentTransaction;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
    android.support.v7.widget.Toolbar cl;
    android.support.design.widget.TabLayout tl;
    PhonesFragment phones_fragment;
    EmailFragment mails_fragment;
    Contact c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info_dialog);
        c = (Contact)getIntent().getSerializableExtra("Contact");
        image = findViewById(R.id.picture_imageview);
        tl = findViewById(R.id.info_tabs);
        rv = findViewById(R.id.info_recycler);
        rv.setNestedScrollingEnabled(false);
        rv.setHasFixedSize(true);
        setInfo();

//        lv = findViewById(R.id.basic_info);
//
//
//        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
//
//            Map<String, String> datum = new HashMap<String, String>(2);
//            datum.put("title", "Nombre" );
//            datum.put("subtitle",c.getName());
//            data.add(datum);
//            if(c.getBirth()!=null) {
//                Map<String, String> datum2 = new HashMap<String, String>(2);
//                datum2.put("title", "Cumpleaños");
//                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                datum2.put("subtitle", format.format(c.getBirth()));
//                data.add(datum2);
//            }
//
//        SimpleAdapter adapter = new SimpleAdapter(this, data,
//                android.R.layout.simple_list_item_2,
//                new String[] {"title", "subtitle"},
//                new int[] {android.R.id.text1,
//                        android.R.id.text2});
//        lv.setAdapter(adapter);

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
        cl = findViewById(R.id.toolbar);
        if(c.getName().size()>0) {
            cl.setTitle(c.getName().get(0));
        }
        else {
            if(c.getNumber().size()>0){
                cl.setTitle((new ArrayList<String>(c.getNumber().values())).get(0));
            }else if(c.getEmails().size()>0){
                cl.setTitle(c.getEmails().get(0));
            }else {
                cl.setTitle("EMPTY CONTACT");
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

        phones_fragment = new PhonesFragment().newInstance(c);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_info,phones_fragment);
        transaction.commit();

        //name.setText(c.getName());
//        RecyclerView rv = findViewById(R.id.phones_recycler);
//        LinearLayoutManager lm = new LinearLayoutManager(getBaseContext());
//        rv.setLayoutManager(lm);
//        Log.d("NUMERO_EN_INFO", "onCreate: "+c.getNumber().size());
//        PhoneAdapter pa = new PhoneAdapter(this,c.getNumber());
//        Log.d("LISTA_EN_INFO", "onCreate: "+pa.getItemCount());
//        rv.setAdapter(pa);
       // overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
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
    }
}
