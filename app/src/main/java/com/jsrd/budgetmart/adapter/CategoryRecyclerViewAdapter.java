package com.jsrd.budgetmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.activity.ProductListActivity;

import java.util.ArrayList;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.categoryViewHolder> {

    private Context mContext;
    private ArrayList<String> categoryList;

    public CategoryRecyclerViewAdapter(Context context, ArrayList<String> categoryList) {
        mContext = context;
        this.categoryList = categoryList;
    }


    @Override
    public categoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_category, parent, false);
        return new categoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(categoryViewHolder holder, int position) {
        String category = categoryList.get(position);
        holder.categoryName.setText(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class categoryViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;

        public categoryViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productListIntent = new Intent(mContext, ProductListActivity.class);
                    productListIntent.putExtra("Category",categoryList.get(getAdapterPosition()));
                    mContext.startActivity(productListIntent);
                }
            });

        }
    }
}
