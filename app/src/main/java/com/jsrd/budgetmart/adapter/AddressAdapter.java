package com.jsrd.budgetmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.model.Address;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    Context mContext;
    ArrayList<Address> addresses;
    public int selectedAddress = 0;

    public AddressAdapter(Context context, ArrayList<Address> addresses) {
        mContext = context;
        this.addresses = addresses;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.address_list_item, parent, false);

        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addresses.get(position);
        holder.addressRadioBtn.setText(address.toString());

        if (selectedAddress >= 0){
            holder.addressRadioBtn.setChecked(position == selectedAddress);
        }

    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }



    public class AddressViewHolder extends RecyclerView.ViewHolder {
        private RadioButton addressRadioBtn;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);

            addressRadioBtn = itemView.findViewById(R.id.addressRadioButton);


            addressRadioBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedAddress = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });


        }
    }

}
