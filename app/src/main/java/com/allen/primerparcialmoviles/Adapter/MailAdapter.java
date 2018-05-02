package com.allen.primerparcialmoviles.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allen.primerparcialmoviles.R;

import java.util.ArrayList;

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.ViewHolder>{
    View v;
    ViewHolder vh;
    ArrayList<String> list;
    Context context;
    public MailAdapter(Context context, ArrayList<String> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_list_item,parent,false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MailAdapter.ViewHolder holder, final int position) {
        holder.email.setText(list.get(position));
        holder.sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",list.get(position), null));

                context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView email;
        ImageButton sendmail;
        public ViewHolder(View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.email_address);
            sendmail = itemView.findViewById(R.id.send_mail);

        }
    }
}
