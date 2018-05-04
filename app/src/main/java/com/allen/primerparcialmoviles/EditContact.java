package com.allen.primerparcialmoviles;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.allen.primerparcialmoviles.Data.Contact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

public class EditContact extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    EditText nombre,apellido,email,direccion,birthday;
    String picture;
    com.mikhaellopez.circularimageview.CircularImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        nombre = findViewById(R.id.edit_text_name);
        apellido = findViewById(R.id.edit_text_last_name);
        email = findViewById(R.id.edit_text_email);
        direccion = findViewById(R.id.edit_text_address);
        image = findViewById(R.id.create_imageview_descrpition);
        birthday = findViewById(R.id.edit_text_birthday);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                String id = "0";
                String name = nombre.getText().toString();
                LinkedHashMap<String,String> numbers = new LinkedHashMap<String,String>();
                ArrayList<String>  emails = new ArrayList<String>();
                emails.add(email.getText().toString());
                ArrayList<String> address = new ArrayList<String>();
                address.add(direccion.getText().toString());
                Boolean favorite = false;

                Contact c = new Contact(id,name,numbers,emails,address,picture,favorite,new Date());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("new_contact",c);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
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
                //DatePickerFragment datePickerFragment = new DatePickerFragment();
                //datePickerFragment.show(getSupportFragmentManager(), "datePicker");
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
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
