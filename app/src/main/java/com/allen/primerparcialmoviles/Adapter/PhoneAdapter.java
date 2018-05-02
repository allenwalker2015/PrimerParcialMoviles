package com.allen.primerparcialmoviles.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allen.primerparcialmoviles.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder>{
    View v;
    ViewHolder vh;
    LinkedHashMap<String,String> list;
    Context context;
    public PhoneAdapter(Context context, LinkedHashMap<String,String> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PhoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_list_item,parent,false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneAdapter.ViewHolder holder,final int position) {
        final String value = (new ArrayList<String>(list.values())).get(position);
        final String type = (new ArrayList<String>(list.keySet())).get(position);
        holder.number.setText(value);
        holder.type.setText(type);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", value, null));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView number,type;
        ImageButton call,sms;
        public ViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.phone_type);
            number = itemView.findViewById(R.id.phone_number);
            call = itemView.findViewById(R.id.button_call);
            sms = itemView.findViewById(R.id.button_sms);
        }
    }
}
