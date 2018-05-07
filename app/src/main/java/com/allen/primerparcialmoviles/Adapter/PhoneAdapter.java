package com.allen.primerparcialmoviles.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder> {
    View v;
    ViewHolder vh;
   ArrayList<ArrayList<String>> list;
    Context context;

    public PhoneAdapter(Context context,ArrayList<ArrayList<String>> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PhoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_list_item, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneAdapter.ViewHolder holder, final int position) {
        final String value = list.get(0).get(position);
        final String type = list.get(1).get(position);
        holder.number.setText(value);
        holder.type.setText(type);
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + value));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });
        holder.sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", value, null)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.get(0).size();
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
