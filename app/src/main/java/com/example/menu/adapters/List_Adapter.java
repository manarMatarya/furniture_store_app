package com.example.menu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.models.Dish;

import java.util.ArrayList;

public class List_Adapter extends RecyclerView.Adapter<List_Adapter.listViewHolder>  {
    ArrayList<Dish> dishes;

    public List_Adapter(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }
    @NonNull
    @Override
    public listViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list,null,false) ;

        List_Adapter.listViewHolder s = new listViewHolder(v);
        return s;
    }

    @Override
    public void onBindViewHolder(@NonNull listViewHolder holder, int position) {
//        dish d = dishes.get(position);
//        holder.name.setText((d.getDname()));
//        holder.desc.setText((d.getDdescription()));
//        holder.price.setText((d.getDprice()));
//        holder.img.setImageResource(d.getDimg());
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    class listViewHolder extends RecyclerView.ViewHolder
    {
        TextView name ;
        TextView desc ;
        TextView price ;
        ImageView img ;
        public listViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.lname) ;
            desc = itemView.findViewById(R.id.ldesc) ;
            price = itemView.findViewById(R.id.lprice) ;
            img = itemView.findViewById(R.id.limg) ;
        }
    }
}
