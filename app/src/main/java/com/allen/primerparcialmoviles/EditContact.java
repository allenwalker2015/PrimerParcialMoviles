package com.allen.primerparcialmoviles;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.allen.primerparcialmoviles.Adapter.MailEditAdapter;
import com.allen.primerparcialmoviles.Adapter.PhoneEditAdapter;
import com.allen.primerparcialmoviles.Data.Contact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

public class EditContact extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;

    //Elementos de la view
    EditText given_name, family_name, direction,birthday,new_mail,new_phone;
    com.mikhaellopez.circularimageview.CircularImageView image;
    Button add_button,add_email_button;
    Spinner type;
    FloatingActionButton fab;

    //Variables necesarias para recursos que son dinamicos
    LinkedHashMap<String,String> phones;
    ArrayList<String> emails;
    String picture;

    //Adaptadores para sus respectivos recycler
    PhoneEditAdapter pa;
    MailEditAdapter ma;
    //Recycler views para el listado de telefonos y de emails
    RecyclerView phones_recycler, email_recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        //Se buscan los elementos en la view
        given_name = findViewById(R.id.edit_text_name);
        type = findViewById(R.id.phone_type_spinner);
        family_name = findViewById(R.id.edit_text_last_name);
        direction = findViewById(R.id.edit_text_address);
        image = findViewById(R.id.contact_picture);
        birthday = findViewById(R.id.edit_text_birthday);
        phones_recycler = findViewById(R.id.recycler_phone);
        email_recycler = findViewById(R.id.recycler_email);
        add_button = findViewById(R.id.add_new_contact_button);
        add_email_button = findViewById(R.id.add_new_email_button);
        new_phone = findViewById(R.id.new_phone_number);
        new_mail = findViewById(R.id.new_email);


        if(phones==null){
            phones = new LinkedHashMap<>();

        }

        if(emails==null){
            emails = new ArrayList<>();

        }

        confAdapters();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab =  findViewById(R.id.fab);
        confListeners();

    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//                savedInstanceState.putParcelable("phones_recycler_state", phones_recycler.getLayoutManager().onSaveInstanceState());
//                // Se guardan los items
//                savedInstanceState.putSerializable("contact_list", pa.getList());
//                //savedInstanceState.putSerializable("contact_adapter",ca);
//                // if(fca!=null) {
//
//                //    savedInstanceState.putSerializable("filtered_adapter", fca);
//                //}
//            }
//        }
//        super.onSaveInstanceState(savedInstanceState);
//    }
//
//    public void restorePreviousState(Bundle savedInstanceState) {
//


//    }
    // Configura y setea los adapters a sus respectivos elementos.
    public void confAdapters(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.phone_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        pa = new PhoneEditAdapter(this,phones);
        phones_recycler.setLayoutManager(new LinearLayoutManager(this));
        phones_recycler.setAdapter(pa);

        ma = new MailEditAdapter(this,emails);
        email_recycler.setLayoutManager(new LinearLayoutManager(this));
        email_recycler.setAdapter(ma);
    }
    public void confListeners(){

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                String id = "0";
                ArrayList<String> name = new ArrayList<>();
                name.add(given_name.getText().toString() +" "+ family_name.getText().toString());
                name.add(given_name.getText().toString());
                name.add(family_name.getText().toString());
                //LinkedHashMap<String,String> numbers = new LinkedHashMap<String,String>();
                ArrayList<String>  emails = new ArrayList<String>();
                //emails.add(email.getText().toString());
                String address = direction.getText().toString();
                Boolean favorite = false;

                Contact c = new Contact(id,name,pa.getList(),ma.getList(),address,picture,favorite,new Date());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("new_contact",c);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !new_phone.getText().toString().matches("")) {
                    pa.addElement(new_phone.getText().toString(), type.getSelectedItem().toString());
                }
                //Toast.makeText(EditContact.this, "CLICK", Toast.LENGTH_SHORT).show();
            }
        });
        add_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( ! new_mail.getText().toString().matches("")) {
                    ma.addElement(new_mail.getText().toString());
                }
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar fechaActual = Calendar.getInstance();

                int anio = fechaActual.get(Calendar.YEAR);
                int mes = fechaActual.get(Calendar.MONTH);
                int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        birthday.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
                    }

                };
                DatePickerDialog picker = new DatePickerDialog(EditContact.this, date, anio, mes, dia);
                picker.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImageURI = data.getData();
            picture = selectedImageURI.toString();
            image.setImageURI(selectedImageURI);
            Toast.makeText(this, "URI:"+selectedImageURI.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    

}
