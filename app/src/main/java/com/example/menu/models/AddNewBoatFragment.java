package com.example.menu.models;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddNewBoatFragment extends Fragment
{
    private static final String ARGUMENT_BOAT_ID = "ARGUMENT_BOAT_ID";
    private static final String ARGUMENT_OWNER_ID = "ARGUMENT_OWNER_ID";

    public static AddNewBoatFragment newInstance(int boatId, String ownerId) {
        Bundle args = new Bundle();
        // Save data here
        args.putInt(ARGUMENT_BOAT_ID, boatId);
        args.putString(ARGUMENT_OWNER_ID, ownerId);
        AddNewBoatFragment fragment = new AddNewBoatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        // Retrieve the data here
        int boatId = getArguments().getInt(ARGUMENT_BOAT_ID);
        String ownerId = getArguments().getString(ARGUMENT_OWNER_ID);
        return view;
    }
}
