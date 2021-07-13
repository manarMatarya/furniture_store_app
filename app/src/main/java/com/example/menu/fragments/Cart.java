package com.example.menu.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menu.R;
import com.example.menu.adapters.CartAdapter;
import com.example.menu.adapters.DishAdapter;
import com.example.menu.models.Dish;
import com.example.menu.models.varieties;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Cart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cart extends Fragment {
    ImageView left;
    RecyclerView cartRec;
    TextView cartTotal;
    ArrayList<Dish> dishList;
    private CartAdapter adapter;
    private DatabaseReference mDatabaseRef;
    public static float total = 0;
    Button order,cont;
    private FirebaseAuth mAuth;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static String ARG_PARAM1 = "name";
    private static String ARG_PARAM2 = "price";

    private String name="";
    private float price=0;

    public Cart() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cart.
     */
    // TODO: Rename and change types and number of parameters
    public static Cart newInstance(String param1, String param2) {
        Cart fragment = new Cart();
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
            price = getArguments().getFloat(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        left = v.findViewById(R.id.cartleft);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new main_fragment()).commit();
            }
        });
        cartRec = v.findViewById(R.id.cartRec);
        cartTotal = v.findViewById(R.id.carttotal);
        order = v.findViewById(R.id.cartbtn);
        cont = v.findViewById(R.id.cartbtnCont);

        dishList = new ArrayList<>();
        cartRec.setHasFixedSize(true);
        cartRec.setLayoutManager(new LinearLayoutManager(getContext()));
        dishList = new ArrayList<>();


        //عرض البيانات حسب الاسم القادم من الريسايكلر
        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(mAuth.getUid()).child("ordered");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Dish upload = postSnapshot.getValue(Dish.class);
                    dishList.add(upload);
                }
                adapter = new CartAdapter( dishList);

                cartRec.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference(mAuth.getUid());
        DatabaseReference unicaribeRef = rootRef.child("ordered");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                float value=(ds.child("price").getValue(Float.class));
                    total = total + value;
                    cartTotal.setText(total +"");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        unicaribeRef.addListenerForSingleValueEvent(eventListener);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Your order arrives soon.. ", Toast.LENGTH_LONG).show();
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_fragment fragment = new main_fragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_container, fragment);
                ft.commit();
            }
        });
            return v;
        }
    }