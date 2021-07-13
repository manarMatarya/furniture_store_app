package com.example.menu.adapters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.activities.uploadInfo;
import com.example.menu.fragments.Cart;
import com.example.menu.fragments.List;
import com.example.menu.models.Dish;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.dishViewHolder> {
    ArrayList<Dish> dishes;
    Context context;
    private FirebaseAuth mAuth;



    public DishAdapter(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    @NonNull
    @Override
    public dishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list,null,false) ;

        DishAdapter.dishViewHolder s = new dishViewHolder(v) ;
        return s;
    }

    @Override
    public void onBindViewHolder(@NonNull final dishViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference(mAuth.getUid()).child("ordered");
        final Dish d = dishes.get(position);
        holder.name.setText((d.getName()));
        holder.desc.setText((d.getDescription()));
        holder.price.setText(d.getPrice()+"");
        Picasso.get()
                .load(d.getImage())
                .placeholder(R.drawable.ic_living_room)
                .fit()
                .centerCrop()
               .into(holder.img);

        holder.a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = Integer.valueOf(holder.ltotal.getText().toString());
                total++;
                float p=Float.valueOf(holder.price.getText().toString());
                holder.ltotal.setText(total+"");
                holder.price.setText((total*p)+"");
            }
        });

        holder.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = Integer.valueOf(holder.ltotal.getText().toString());
                if(total > 1)
                total--;
                float p=Float.valueOf(holder.price.getText().toString());
                holder.ltotal.setText(total+"");
                holder.price.setText((total*p)+"");
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                int total = Integer.valueOf(holder.ltotal.getText().toString());
                final float price = (d.getPrice()) * (total);

                Dish dish = (new Dish(d.getName().toString(),price));
                ref.push().setValue(dish);

            }
            });
        }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    class dishViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView desc;
        TextView price;
        ImageView img;
        Button a,b;
        TextView ltotal;
        Button add;
        public dishViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.lname);
            desc = itemView.findViewById(R.id.ldesc);
            price = itemView.findViewById(R.id.lprice);
            img = itemView.findViewById(R.id.limg);
            a = itemView.findViewById(R.id.a);
            b = itemView.findViewById(R.id.b);
            ltotal = itemView.findViewById(R.id.ltotal);
            add = itemView.findViewById(R.id.lbtnAdd);

        }
    }
    }

