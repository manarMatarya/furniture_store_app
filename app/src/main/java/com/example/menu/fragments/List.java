package com.example.menu.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menu.adapters.DishAdapter;
import com.example.menu.adapters.List_Adapter;
import com.example.menu.R;
import com.example.menu.models.Dish;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link List#newInstance} factory method to
 * create an instance of this fragment.
 */
public class List extends Fragment {

    ImageView left;
    TextView ltitle;
    RecyclerView lrec;
    ArrayList<Dish> dishList;
    private DishAdapter adapter;
    private DatabaseReference mDatabaseRef;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String title;
    private String mParam2;

    public List() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment List.
     */
    // TODO: Rename and change types and number of parameters
    public static List newInstance(String param1, String param2) {
        List fragment = new List();
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
            title = getArguments().getString("title");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_list, container, false);

        left=v.findViewById(R.id.listleft);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new main_fragment()).commit();
            }
        });

        ltitle=v.findViewById(R.id.ltitle);
        ltitle.setText("List Of "+title);
        lrec=v.findViewById(R.id.lrec);
        lrec.setHasFixedSize(true);
        lrec.setLayoutManager(new LinearLayoutManager(getContext()));
        dishList = new ArrayList<>();


        //عرض البيانات حسب الاسم القادم من الريسايكلر
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(title);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Dish upload = postSnapshot.getValue(Dish.class);
                    dishList.add(upload);
                }
                adapter = new DishAdapter( dishList);
               lrec.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}