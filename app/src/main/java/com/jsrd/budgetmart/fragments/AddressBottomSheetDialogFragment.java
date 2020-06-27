package com.jsrd.budgetmart.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.activity.AddressActivity;
import com.jsrd.budgetmart.activity.CheckoutActivity;
import com.jsrd.budgetmart.adapter.AddressAdapter;
import com.jsrd.budgetmart.interfaces.AddressCallBack;
import com.jsrd.budgetmart.model.Address;
import com.jsrd.budgetmart.utils.FirestoreFirebase;

import java.util.ArrayList;


public class AddressBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private TextView addNewAddress;
    private RecyclerView addressRecyclerView;

    public AddressBottomSheetDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.address_bottom_sheet_dialog, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addressRecyclerView = getView().findViewById(R.id.addressRecyclerView);
        addNewAddress = getView().findViewById(R.id.addNewAddress);

        addressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addressRecyclerView.setHasFixedSize(true);
        FirestoreFirebase ff = new FirestoreFirebase(getContext());
        ff.getAddressFromFirebase(new AddressCallBack() {
            @Override
            public void onComplete(ArrayList<Address> addresses) {
                AddressAdapter adapter = new AddressAdapter(getContext(), addresses);
                addressRecyclerView.setAdapter(adapter);
            }
        });

        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressintent = new Intent(getContext(), AddressActivity.class);
                startActivity(addressintent);
            }
        });
    }

}