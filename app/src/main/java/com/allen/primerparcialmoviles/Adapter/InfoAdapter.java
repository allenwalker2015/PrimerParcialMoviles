package com.allen.primerparcialmoviles.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allen.primerparcialmoviles.R;

import java.util.ArrayList;
import java.util.HashMap;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder>{
    HashMap<Integer,Pair> map;

    public InfoAdapter(HashMap<Integer,Pair> map){
        this.map = map;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_list_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.prop.setText((int)map.get(position).first);
        holder.value.setText((String)map.get(position).second);
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView prop,value;

        public ViewHolder(View itemView) {
            super(itemView);
            prop = itemView.findViewById(R.id.prop_textview);
            value = itemView.findViewById(R.id.value_textview);
        }
    }
}
