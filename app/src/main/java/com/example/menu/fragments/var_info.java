package com.example.menu.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menu.R;
import com.example.menu.adapters.CartAdapter;
import com.example.menu.models.Dish;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link var_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class var_info extends Fragment {
    TextView title;
    ImageView image,left;
    TextView desc,total,price;
    Button a,b,add;
    RatingBar rating;

    private CartAdapter adapter;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String name;
    private String mParam2;

    public var_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment var_info.
     */
    // TODO: Rename and change types and number of parameters
    public static var_info newInstance(String param1, String param2) {
        var_info fragment = new var_info();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_var_info, container, false);

        left = v.findViewById(R.id.infoleft);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new main_fragment()).commit();
            }
        });

        title = v.findViewById(R.id.vartitle);
        image = v.findViewById(R.id.varimg);
        desc = v.findViewById(R.id.vardesc);
        rating = v.findViewById(R.id.varrating);
        price = v.findViewById(R.id.varprice);
        total = v.findViewById(R.id.vartotal);
        a = v.findViewById(R.id.vara);
        b = v.findViewById(R.id.varb);
        add = v.findViewById(R.id.varbtn);

        DatabaseReference ref;
        String[] names={"Chairs","Sofes","Mirrors","Beds","Cupboards","Tables"};
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final DatabaseReference reference = database.getReference(mAuth.getUid()).child("ordered");

        for(int i=0;i<6;i++){
            //عرض البيانات حسب الاسم القادم من الريسايكلر
            ref = FirebaseDatabase.getInstance().getReference(names[i]);
            ref.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        System.out.println(userSnapshot.getKey());
                        title.setText(userSnapshot.child("name").getValue(String.class));
                        Picasso.get()
                                .load(userSnapshot.child("image").getValue(String.class))
                                .placeholder(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(image);
                        price.setText((userSnapshot.child("price").getValue(Float.class))+"");
                        desc.setText(userSnapshot.child("description").getValue(String.class));
                        rating.setRating(userSnapshot.child("rating").getValue(Float.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });
        }


        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(total.getText().toString());
                num++;
                float p=Float.valueOf(price.getText().toString());
                price.setText((num*p)+"");
                total.setText(num+"");
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(total.getText().toString());
                if(num > 1)
                num--;
                float p=Float.valueOf(price.getText().toString());
                price.setText((num*p)+"");
                total.setText(num+"");
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.valueOf(total.getText().toString());
                float p=Float.valueOf(price.getText().toString());
                Dish dish = (new Dish(title.getText().toString(),p));
                reference.push().setValue(dish);
            }
        });
        return v;
    }
}