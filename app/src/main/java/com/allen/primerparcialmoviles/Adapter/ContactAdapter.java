package com.allen.primerparcialmoviles.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.primerparcialmoviles.Data.Contact;
import com.allen.primerparcialmoviles.Data.URIPath;
import com.allen.primerparcialmoviles.Filter.ContactFilter;
import com.allen.primerparcialmoviles.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements Filterable,Serializable {

    public ArrayList<Contact> list,filterList;
    public ContactFilter filter;
    private View v;
    private Context context;
    private Boolean favs=false;



    public ContactAdapter(ArrayList<Contact> list, Context context) {
        this.list = list;
        this.filterList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, final int position) {
        if(list.get(position).getName().size()>0) {
            holder.name.setText(list.get(position).getName().get(0));
        }
        else {
            if(list.get(position).getNumber().size()>0){

            }else if(list.get(position).getEmails().size()>0){
                holder.name.setText(list.get(position).getEmails().get(0));
            }else {
                holder.name.setText("EMPTY CONTACT");
            }
        }
        //holder.info.setImageResource(android.R.drawable.ic_menu_info_details);
        holder.c = list.get(position);
        Log.d("URI", "onBindViewHolder: "+list.get(position).getPicture());
        if(list.get(position).getPicture()!=null) {
            if(list.get(position).getPicture().contains("com.android.contacts")){
                holder.picture.setImageURI(Uri.parse(list.get(position).getPicture()));
            }
            else {
                Uri uri = Uri.parse(list.get(position).getPicture());
                Glide.with(context).load(new File(URIPath.getRealPathFromURI(context, uri))).
                        into(holder.picture);
            }
           // holder.picture.setImageURI(Uri.parse(list.get(position).getPicture()));
        }else  holder.picture.setImageResource(R.drawable.userwhite_min);
        imageSelect(holder, position);
        listeners(holder, position);

    }

    public void imageSelect(ContactAdapter.ViewHolder holder, final int position) {
        final ImageButton star = holder.star;
        //Si el contacto tiene imagen, se coloca en la tarjeta, sino se coloca la imagen por default

        //Colocar la estrella encendida o apagada segun corresponda
        if (list.get(position).isFavorite()) {

            star.setImageResource(android.R.drawable.btn_star_big_on);
        } else {

            star.setImageResource(android.R.drawable.btn_star_big_off);
        }

    }

    public void listeners(final ContactAdapter.ViewHolder holder, final int position) {
        final ImageButton star = holder.star;
        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "CLICK EN STAR", Toast.LENGTH_SHORT).show();
                if (list.get(position).isFavorite()) {
                    list.get(position).setFavorite(false);
                    star.setImageResource(android.R.drawable.btn_star_big_off);
                } else {
                    list.get(position).setFavorite(true);
                    star.setImageResource(android.R.drawable.btn_star_big_on);
                }
                //starOnClickListener(v,list.get(position));

            }
        });

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "CLICK EN INFO", Toast.LENGTH_SHORT).show();
                infoOnClickListener(list.get(position));
            }
        });
        //Listeners para los botones
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public abstract void infoOnClickListener(Contact c);



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView picture;
        ImageButton star;
        View info;
        Contact c;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_textview);
            picture = itemView.findViewById(R.id.picture_imageview);
            star = itemView.findViewById(R.id.button_star);
            info = itemView.findViewById(R.id.button_info);
        }
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new ContactFilter(list,this,favs);
        }
        return filter;
    }

//
//    public void setFilter(ContactFilter filter) {
//
//            this.filter = filter;
//
//    }

//    public Boolean getFavs() {
//        return favs;
//    }
//
//    public void setFavs(Boolean favs) {
//        this.favs = favs;
//    }


}
