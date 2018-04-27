package com.allen.primerparcialmoviles.Adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.primerparcialmoviles.Data.Contact;
import com.allen.primerparcialmoviles.R;

import java.util.ArrayList;

public abstract class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private ArrayList<Contact>list;
    private View v;
    public ContactAdapter(ArrayList<Contact> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, final int position) {

        holder.name.setText(list.get(position).getName());
        holder.info.setImageResource(android.R.drawable.ic_menu_info_details);
        holder.c = list.get(position);
        Uri image = list.get(position).getPicture();
        imageSelect(holder,image,position);
        listeners(holder,image,position);

    }

    public void imageSelect(ContactAdapter.ViewHolder holder,Uri image,final int position){
        final ImageButton star = holder.star;
        //Si el contacto tiene imagen, se coloca en la tarjeta, sino se coloca la imagen por default
        if(image!=null) {
            holder.picture.setImageURI(image);
            //holder.picture.set
        }else{
            holder.picture.setImageResource(R.drawable.ic_person_black);
        }
        //Colocar la estrella encendida o apagada segun corresponda
        if(list.get(position).isFavorite()){

            star.setImageResource(android.R.drawable.btn_star_big_on);
        }
        else {

            star.setImageResource(android.R.drawable.btn_star_big_off);
        }

    }

    public void listeners(final ContactAdapter.ViewHolder holder,Uri image,final int position){
        final ImageButton star = holder.star;
        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "CLICK EN STAR", Toast.LENGTH_SHORT).show();
                if(list.get(position).isFavorite()){
                    list.get(position).setFavorite(false);
                    star.setImageResource(android.R.drawable.btn_star_big_off);
                }
                else {
                    list.get(position).setFavorite(true);
                    star.setImageResource(android.R.drawable.btn_star_big_on);
                }
                //starOnClickListener(v,list.get(position));

            }
        });

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "CLICK EN INFO", Toast.LENGTH_SHORT).show();
                infoOnClickListener(v,holder);
            }
        });
        //Listeners para los botones
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView picture;
        ImageButton star;
        ImageButton info;
        Contact c;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_textview);
            picture = itemView.findViewById(R.id.picture_imageview);
            star = itemView.findViewById(R.id.button_star);
            info =  itemView.findViewById(R.id.button_info);
        }

    }

    public abstract void infoOnClickListener(View v, ContactAdapter.ViewHolder vh);
    public abstract void starOnClickListener(View v, ContactAdapter.ViewHolder vh);
}
