package com.allen.primerparcialmoviles.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.primerparcialmoviles.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MailEditAdapter extends RecyclerView.Adapter<MailEditAdapter.ViewHolder> {
    public ArrayList<String> list;
    View v;
    ViewHolder vh;
    Context context;

    public MailEditAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MailEditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_list_edit_item, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MailEditAdapter.ViewHolder holder, final int position) {
        final String value = list.get(position);


        holder.email.setText(value);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);

                notifyItemRemoved(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addElement(String value){
        this.list.add(value);
        notifyItemInserted(list.size());
        notifyDataSetChanged();
    }

    public ArrayList<String> getList() {
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView email,type;
        ImageView remove;

        public ViewHolder(View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.email_address);
            remove = itemView.findViewById(R.id.remove_button);
        }
    }

}
