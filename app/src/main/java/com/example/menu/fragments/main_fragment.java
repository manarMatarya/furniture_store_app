package com.example.menu.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menu.activities.MainScreen;
import com.example.menu.activities.uploadInfo;
import com.example.menu.adapters.CartAdapter;
import com.example.menu.adapters.DishAdapter;
import com.example.menu.R;
import com.example.menu.activities.Registeration;
import com.example.menu.adapters.MainAdapter;
import com.example.menu.adapters.VarietiesAdapter;
import com.example.menu.models.Dish;
import com.example.menu.models.User;
import com.example.menu.models.varieties;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link main_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class main_fragment extends Fragment {
    private ArrayList<Dish> list2 ,list3 ;
    private RecyclerView rec1 , rec2 , rec3;
    private MainAdapter adapter2 , adapter3;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    EditText search;
    ImageView mainmore;
    TextView tvsearch;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public main_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment main_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static main_fragment newInstance(String param1, String param2) {
        main_fragment fragment = new main_fragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         final View v= inflater.inflate(R.layout.fragment_main_fragment, container, false);


         mainmore = v.findViewById(R.id.mainmore);
         mainmore.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 PopupMenu pm = new PopupMenu(getContext(), v.findViewById(R.id.mainmore));
                 pm.getMenuInflater().inflate(R.menu.upload_menu, pm.getMenu());
                 pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     @Override
                     public boolean onMenuItemClick(MenuItem item) {
                         switch (item.getItemId()) {
                             case R.id.popUpload:
                                 boolean validPassword =false;
                                 final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                 alert.setTitle("Admin Login");
                                 alert.setMessage("Enter your password");
                                 final EditText input = new EditText(getContext());
                                 alert.setView(input);
                                 alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int whichButton) {
                                         String value = input.getText().toString();
                                         if (value.equals("admin")){
                                             startActivity(new Intent(getContext(), uploadInfo.class));
                                         }else
                                             Toast.makeText(getContext(), "invalid password", Toast.LENGTH_SHORT).show();
                                     }
                                 });
                                 alert.show();
                                 break;
                         }
                         return true;
                     }
                 });
                 pm.show();
             }
         });
         rec1=v.findViewById(R.id.R1);
         rec2=v.findViewById(R.id.R2);
         rec3=v.findViewById(R.id.R3);
         search=v.findViewById(R.id.search);
         tvsearch=v.findViewById(R.id.tvsearch);
         tvsearch.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 List list = new List();
                 Bundle bundle = new Bundle();
                 bundle.putString("title", search.getText().toString()); //key and value
                list.setArguments(bundle);
                FragmentTransaction fragmentManager =((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.main_container, list);
                fragmentManager.addToBackStack(null);
                fragmentManager.commit();
             }

                 });



         final ArrayList<varieties> varieties = new ArrayList<>();

        varieties.add(new varieties("Chairs",R.drawable.ic_chair));
        varieties.add(new varieties("Sofes",R.drawable.ic_sofa)) ;
        varieties.add(new varieties("Mirrors",R.drawable.ic_mirror)) ;
        varieties.add(new varieties("Beds",R.drawable.ic_bed)) ;
        varieties.add(new varieties("Cupboards",R.drawable.ic_cupboard)) ;
        varieties.add(new varieties("Tables",R.drawable.ic_table)) ;

        VarietiesAdapter adapter1 = new VarietiesAdapter(varieties) ;
        rec1.setAdapter(adapter1);
        final LinearLayoutManager horizontalLayoutManagaer1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rec1.setLayoutManager(horizontalLayoutManagaer1);
        rec1.setVisibility(View.VISIBLE);

        list2 = new ArrayList<>();
        String[] names={"Chairs","Sofes","Mirrors","Beds","Cupboards","Tables"};
        for(int i=0 ; i<6;i++) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(names[i]);

            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Dish upload = postSnapshot.getValue(Dish.class);
                        list2.add(upload);
                    }
                    adapter2 = new MainAdapter(list2);
                    rec2.setAdapter(adapter2);
                    LinearLayoutManager horizontalLayoutManagaer2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    rec2.setLayoutManager(horizontalLayoutManagaer2);
                    rec2.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        list3= new ArrayList<>();
        String[] main={"Sofes","Beds","Mirrors"};
        for(int i=0 ; i<3;i++) {
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(main[i]);
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Dish upload = postSnapshot.getValue(Dish.class);
                        list3.add(upload);
                    }
                    adapter3 = new MainAdapter(list3);
                    rec3.setAdapter(adapter3);
                    LinearLayoutManager horizontalLayoutManagaer3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    rec3.setLayoutManager(horizontalLayoutManagaer3);
                    rec3.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return v;
    }

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link profile#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class profile extends Fragment {
        TextView profilename, profileemail, profileaddress;
        private FirebaseAuth fAuth;

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public profile() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment profile.
         */
        // TODO: Rename and change types and number of parameters
        public static profile newInstance(String param1, String param2) {
            profile fragment = new profile();
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
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }


        @Override
        public void onStart() {
            super.onStart();
            if (fAuth.getCurrentUser() == null) {
                startActivity(new Intent(getContext(), Registeration.class));
            }
        }
    }

}