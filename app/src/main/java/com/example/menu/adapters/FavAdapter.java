package com.example.menu.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.fragments.var_info;
import com.example.menu.models.Dish;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.favViewHolder> {
    ArrayList<Dish> dishes;
    Context context;
    private FirebaseAuth mAuth;

    public FavAdapter(ArrayList<Dish> dishes) {
        this.dishes=dishes;
    }

    @NonNull
    @Override
    public FavAdapter.favViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_fav,null,false) ;

        FavAdapter.favViewHolder s = new FavAdapter.favViewHolder(v) ;
        return s;
    }

    @Override
    public void onBindViewHolder(@NonNull final FavAdapter.favViewHolder holder, final int position) {
        final Dish d = dishes.get(position);
        holder.name.setText((d.getName()));
        holder.price.setText(d.getPrice()+"");

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
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                //openHelper.deleteProduct(dishes.getId());
                holder.close.setImageResource(R.drawable.heart);
                dishes.remove(position);
                notifyItemRemoved(position);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child(mAuth.getUid()).child("favourite").orderByChild("name").equalTo(d.getName().toString());;

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class favViewHolder extends RecyclerView.ViewHolder {
        TextView name,price;
        ImageView close;
        public favViewHolder(@NonNull View itemView) {
            super(itemView);
            context=itemView.getContext();
            name=itemView.findViewById(R.id.fname);
            price=itemView.findViewById(R.id.fprice);
            close=itemView.findViewById(R.id.fclose);
        }
    }
}
