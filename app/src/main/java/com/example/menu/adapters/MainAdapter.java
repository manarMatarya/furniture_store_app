package com.example.menu.adapters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.activities.uploadInfo;
import com.example.menu.fragments.Favourite;
import com.example.menu.fragments.List;
import com.example.menu.fragments.var_info;
import com.example.menu.models.Dish;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.mainViewHolder> {
    ArrayList<Dish> dishes;
    Context context;
    private FirebaseAuth mAuth;
  
    public MainAdapter(ArrayList<Dish> dishes) {
        this.dishes = dishes;
    }

    @NonNull
    @Override
    public mainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dish,null,false) ;

        MainAdapter.mainViewHolder s= new MainAdapter.mainViewHolder(v) ;
        return s;
    }

    @Override
    public void onBindViewHolder(@NonNull final mainViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference(mAuth.getUid()).child("favourite");
        final Dish d = dishes.get(position);
        holder.name.setText((d.getName()));
        holder.price.setText(d.getPrice()+"");
        Picasso.get()
                .load(d.getImage())
                .placeholder(R.drawable.ic_living_room)
                .fit()
                .centerCrop()
                .into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var_info info = new var_info();
                Bundle bundle = new Bundle();
                bundle.putString("name", d.getName()); //key and value
                info.setArguments(bundle);
                FragmentTransaction fragmentManager =((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.main_container, info);
                fragmentManager.addToBackStack(null);
                fragmentManager.commit();
            }
        });
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.fav.setImageResource(R.drawable.redheart);

                Dish dish = (new Dish(d.getName().toString(),d.getPrice()));
                ref.push().setValue(dish);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }
    public class mainViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView fav;
        ImageView image;
        public mainViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = itemView.findViewById(R.id.dishname);
            price = itemView.findViewById(R.id.dishprice);
            image = itemView.findViewById(R.id.dishimg);
            fav = itemView.findViewById(R.id.dfav);
        }
    }
    }
