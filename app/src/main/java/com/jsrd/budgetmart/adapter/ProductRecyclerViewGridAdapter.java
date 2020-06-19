package com.jsrd.budgetmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.activity.ProductDetailsActivity;
import com.jsrd.budgetmart.model.Product;
import com.jsrd.budgetmart.utils.StorageFirebase;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductRecyclerViewGridAdapter extends RecyclerView.Adapter<ProductRecyclerViewGridAdapter.ProductViewHolder> {

    private Context mContext;
    private ArrayList<Product> productList;

    public ProductRecyclerViewGridAdapter(Context context, ArrayList<Product> productList) {
        mContext = context;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {

        Product product = productList.get(position);
        StorageFirebase sf = new StorageFirebase(mContext);
        sf.setImageToImageView(product.getImage(),holder.productImage);
        holder.productName.setText(product.getName());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itemDetailsIntent = new Intent(mContext, ProductDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Item", (Serializable) productList.get(getAdapterPosition()));
                    itemDetailsIntent.putExtra("Item", bundle);
                    mContext.startActivity(itemDetailsIntent);
                }
            });

        }
    }

}
