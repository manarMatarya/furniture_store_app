package com.example.menu.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.activities.MainScreen;
import com.example.menu.activities.uploadInfo;
import com.example.menu.fragments.List;
import com.example.menu.models.AddNewBoatFragment;
import com.example.menu.models.Dish;
import com.example.menu.models.varieties;

import java.util.ArrayList;

public class VarietiesAdapter extends RecyclerView.Adapter< VarietiesAdapter.varietiesViewHolder> {
    ArrayList<varieties> varieties;

    public VarietiesAdapter(ArrayList<com.example.menu.models.varieties> varieties) {
        this.varieties = varieties;
    }

    @NonNull
    @Override
    public varietiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_varieties, null, false);

        varietiesViewHolder s = new varietiesViewHolder(v);
        return s;
    }

    @Override
    public void onBindViewHolder(@NonNull final varietiesViewHolder holder, int position) {
        final varieties v = varieties.get(position);
        holder.txt.setText(v.getName());
         final String title=(v.getName());
        holder.img.setImageResource(v.getImg());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List list = new List();
                FragmentTransaction fragmentManager =((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("title",title); //key and value
                list.setArguments(bundle);
                fragmentManager.replace(R.id.main_container, list);
                fragmentManager.addToBackStack(null);
                fragmentManager.commit();
//                AppCompatActivity activity = (AppCompatActivity)v.getContext();
//                List list = new List();
//
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_container,list).addToBackStack(null).commit();


            }
        });
    }

    @Override
    public int getItemCount() {
        return varieties.size();
    }

    Context context;

    class varietiesViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ImageView img;

        public varietiesViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            txt = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.img);
        }
    }
}