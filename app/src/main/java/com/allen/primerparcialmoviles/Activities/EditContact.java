package com.allen.primerparcialmoviles.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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
import com.allen.primerparcialmoviles.Data.URIPath;
import com.allen.primerparcialmoviles.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

public class EditContact extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    public static final int TAKE_IMAGE = 2;

    Uri photoURI;
    //Elementos de la view
    EditText given_name, family_name, direction, birthday, new_mail, new_phone;
    com.mikhaellopez.circularimageview.CircularImageView image;
    //ImageView image;
    int index;
    Button add_button, add_email_button;
    Spinner type;
    FloatingActionButton fab;

    //Variables necesarias para recursos que son dinamicos
//    LinkedHashMap<String, String> phones;
//    ArrayList<String> emails;
    //String picture;
    Contact c;
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
        fab = findViewById(R.id.fab);
        if(getIntent().getSerializableExtra("Contact")!=null){
            c = (Contact)getIntent().getSerializableExtra("Contact");
            setInfo();
        }
        if(getIntent().getIntExtra("index",-1)>-1){
           index = getIntent().getIntExtra("index",-1);
        }
        else c= new Contact();


        if (savedInstanceState == null) {
            if (c.getNumber() == null) {
                ArrayList<ArrayList<String>> lista = new ArrayList<ArrayList<String>>();
                lista.add(new ArrayList<String>());
                lista.add(new ArrayList<String>());
                c.setNumber(lista);

            }

            if (c.getEmails() == null) {
                c.setEmails(new ArrayList<String>());

            }

            confAdapters();
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            confListeners();
        } else {
            restorePreviousState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //Se guarda el estado de los recycler view ( la posicion)
        savedInstanceState.putParcelable("phones_recycler_state", phones_recycler.getLayoutManager().onSaveInstanceState());
        savedInstanceState.putParcelable("email_recycler_state", email_recycler.getLayoutManager().onSaveInstanceState());
        // Se guardan las listas de los adapters
        //savedInstanceState.putSerializable("phone_list", pa.getList());
        //savedInstanceState.putSerializable("email_list", ma.getList());
        //Se guarda la uri de la imagen seleccionada
        //savedInstanceState.putString("profile_picture", picture);
        //Se guarda el contacto
        c.setNumber(pa.getList());
        savedInstanceState.putSerializable("new_contact", c);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void restorePreviousState(Bundle savedInstanceState) {
        c = (Contact) savedInstanceState.getSerializable("new_contact");
//        phones = (LinkedHashMap<String, String>) savedInstanceState.getSerializable("phone_list");
//        emails = (ArrayList<String>) savedInstanceState.getSerializable("email_list");
        confAdapters();
        phones_recycler.getLayoutManager().onRestoreInstanceState(savedInstanceState.
                getParcelable("phones_recycler_state"));
        email_recycler.getLayoutManager().onRestoreInstanceState(savedInstanceState.
                getParcelable("email_recycler_state"));
        //picture = savedInstanceState.getString("profile_picture");
        if (c.getPicture() != null) {
            if (c.getPicture().contains("com.android.contacts")) {
                image.setImageURI(Uri.parse(c.getPicture()));
            } else {
                Uri uri = Uri.parse(c.getPicture());
                Glide.with(this).load(new File(URIPath.getRealPathFromURI(this, uri))).apply(RequestOptions.overrideOf(150, 150)).into(image);
            }
        }
        confListeners();


    }

    //Configura y setea los adapters a sus respectivos elementos.
    public void confAdapters() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.phone_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        pa = new PhoneEditAdapter(this, c.getNumber());
        phones_recycler.setLayoutManager(new LinearLayoutManager(this));
        phones_recycler.setAdapter(pa);

        ma = new MailEditAdapter(this, c.getEmails());
        email_recycler.setLayoutManager(new LinearLayoutManager(this));
        email_recycler.setAdapter(ma);
    }

    public void confListeners() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                String id = "0";
                ArrayList<String> name = new ArrayList<>();
                name.add(given_name.getText().toString() + " " + family_name.getText().toString());
                name.add(given_name.getText().toString());
                name.add(family_name.getText().toString());
                //LinkedHashMap<String,String> numbers = new LinkedHashMap<String,String>();
                ArrayList<String> emails = new ArrayList<String>();
                //emails.add(email.getText().toString());
                String address = direction.getText().toString();
                Boolean favorite = false;
                c.setName(name);
                c.setAddress(address);
                //Contact c = new Contact(id, name, pa.getList(), ma.getList(), address, picture, favorite, new Date());
                Intent returnIntent = new Intent();
                ArrayList<ArrayList<String>> l = pa.getList();
                c.setNumber(l);
                returnIntent.putExtra("new_contact", c);
                returnIntent.putExtra("index", index);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!new_phone.getText().toString().matches("")) {
                    pa.addElement(new_phone.getText().toString(), type.getSelectedItem().toString());
                }
                //Toast.makeText(EditContact.this, "CLICK", Toast.LENGTH_SHORT).show();
            }
        });
        add_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!new_mail.getText().toString().matches("")) {
                    ma.addElement(new_mail.getText().toString());
                }
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction( MEDIA.);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Calendar fechaActual = Calendar.getInstance();
                if(c.getBirth()!=null) {
                    fechaActual.setTime(c.getBirth());
                }
                int anio = fechaActual.get(Calendar.YEAR);
                int mes = fechaActual.get(Calendar.MONTH);
                int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            birthday.setText( dayOfMonth+ "/" + (monthOfYear+1) + "/" +   year);
                            c.setBirth(format.parse(dayOfMonth+ "/" + (monthOfYear+1) + "/" +  year));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                };
                DatePickerDialog picker = new DatePickerDialog(EditContact.this, date, anio, mes, dia);
                picker.show();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = {getString(R.string.photo_picker_take_photo),
                getString(R.string.photo_picker_gallery),
                getString(R.string.photo_picker_cancel)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.photo_picker_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.photo_picker_take_photo))) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Ensure that there's a camera activity to handle the intent
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File...
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            photoURI = FileProvider.getUriForFile(EditContact.this,
                                    "com.allen.primerparcialmoviles.provider",photoFile);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePictureIntent, TAKE_IMAGE);
                        }

                    }



                } else if (items[item].equals(getString(R.string.photo_picker_gallery))) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            PICK_IMAGE);
                } else if (items[item].equals(getString(R.string.photo_picker_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void setInfo(){
        if(c.getName().size()>1) {
            given_name.setText(c.getName().get(1));
        }
        if(c.getName().size()>2) {
            family_name.setText(c.getName().get(2));
        }
        if(c.getAddress()!=null){
            direction.setText(c.getAddress());
        }
        if(c.getBirth()!=null){
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            birthday.setText(format.format(c.getBirth()));
        }
        if (c.getPicture() != null) {
            if (c.getPicture().contains("com.android.contacts")) {
                image.setImageURI(Uri.parse(c.getPicture()));
            } else {
                Uri uri = Uri.parse(c.getPicture());
                Glide.with(this).load( uri).apply(RequestOptions.overrideOf(150, 150)).into(image);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri selectedImageURI = data.getData();
            if(selectedImageURI!=null) {
                c.setPicture(selectedImageURI.toString());

                Glide.with(this).load(new File(URIPath.getRealPathFromURI(this, selectedImageURI))).apply(RequestOptions.overrideOf(150, 150)).into(image);
                image.setImageURI(selectedImageURI);
               // Toast.makeText(this, "URI:" + selectedImageURI.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == TAKE_IMAGE && resultCode == RESULT_OK) {
            if (photoURI != null) {
                c.setPicture(photoURI.toString());

                Glide.with(this).load(photoURI).apply(RequestOptions.overrideOf(150, 150)).into(image);
                image.setImageURI(photoURI);
                //Toast.makeText(this, "URI:" + photoURI.toString(), Toast.LENGTH_SHORT).show();

            }
        }

    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }


}
