package com.allen.primerparcialmoviles.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.primerparcialmoviles.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PhoneEditAdapter extends RecyclerView.Adapter<PhoneEditAdapter.ViewHolder> {
    public ArrayList<String> values,key;
    View v;
    ViewHolder vh;
    Context context;

    public PhoneEditAdapter(Context context, LinkedHashMap<String, String> list) {
        this.context = context;
        this.values = new ArrayList<>(list.values());
        this.key = new ArrayList<>(list.keySet());
    }

    @NonNull
    @Override
    public PhoneEditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_list_edit_item, parent, false);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneEditAdapter.ViewHolder holder, final int position) {
        final String value = values.get(position);
        final String type = key.get(position);

        holder.number.setText(value);
        holder.type.setText(type);
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values.remove(position);
                key.remove(position);
                notifyItemRemoved(position);
            }
        });
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
//                R.array.phone_types, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        holder.type.setAdapter(adapter);
//        holder.type.
//        holder.type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                key.set(position,parent.getSelectedItem().toString());
//                Log.d("Selected Item", "onItemSelected: " + key.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        holder.remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public void addElement(String value, String key){
        this.values.add(value);
        this.key.add(key);
        notifyItemInserted(values.size());
        notifyDataSetChanged();
    }

    public LinkedHashMap<String,String> getList(){
        LinkedHashMap<String,String> list = new LinkedHashMap<>();
        for(int i=0;i<values.size();i++){
            list.put(key.get(i),values.get(i));
        }
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView number,type;
        ImageView remove;

        public ViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.phone_type);
            number = itemView.findViewById(R.id.phone_number);
            remove = itemView.findViewById(R.id.remove_button);
        }
    }
}
