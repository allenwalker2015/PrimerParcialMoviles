package com.allen.primerparcialmoviles.Fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allen.primerparcialmoviles.Adapter.InfoAdapter;
import com.allen.primerparcialmoviles.Adapter.PhoneAdapter;
import com.allen.primerparcialmoviles.Data.Contact;
import com.allen.primerparcialmoviles.Data.URIPath;
import com.allen.primerparcialmoviles.R;
import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class ContactInfoFragment extends Fragment {
    ImageView image;
    View v;
    RecyclerView rv;
    android.support.v7.widget.Toolbar cl;
    TabLayout tl;
    PhonesFragment phones_fragment;
    EmailFragment mails_fragment;
    Contact c;

    public static ContactInfoFragment newInstance(Contact c) {

        Bundle args = new Bundle();
        args.putSerializable("Contact",c);
        ContactInfoFragment fragment = new ContactInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contact_info_dialog,container,false);
//        setContentView(R.layout.contact_info_dialog);
        c = (Contact)getArguments().getSerializable("Contact");
        image = v.findViewById(R.id.picture_imageview);
        tl = v.findViewById(R.id.info_tabs);
        rv = v.findViewById(R.id.info_recycler);
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
                        FragmentTransaction transaction = getFragmentManager() .beginTransaction();
                        transaction.replace(R.id.frame_info,phones_fragment);
                        //transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                    case 1:
                        if(mails_fragment==null)mails_fragment = new EmailFragment().newInstance(c);
                        FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
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
//        cl = v.findViewById(R.id.toolbar);
//        if(c.getName().size()>0) {
//            cl.setTitle(c.getName().get(0));
//        }
//        else {
//            if(c.getNumber().size()>0){
//                cl.setTitle((new ArrayList<String>(c.getNumber().values())).get(0));
//            }else if(c.getEmails().size()>0){
//                cl.setTitle(c.getEmails().get(0));
//            }else {
//                cl.setTitle("EMPTY CONTACT");
//            }
//
//        }
        if(c.getPicture()!=null){
            if(c.getPicture().contains("com.android.contacts")){
                image.setImageURI(Uri.parse(c.getPicture()));
            }
            else {


                Uri uri = Uri.parse(c.getPicture());
                Glide.with(this).load(new File(URIPath.getRealPathFromURI(getContext(), uri))).
                        into(image);
            }
        }

        phones_fragment = new PhonesFragment().newInstance(c);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.frame_info,phones_fragment);
        transaction.commit();


        return  v;
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

   rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(infoAdapter);
    }
}
