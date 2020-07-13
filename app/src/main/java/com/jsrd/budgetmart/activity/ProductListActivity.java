package com.jsrd.budgetmart.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.jsrd.budgetmart.adapter.ProductRecyclerViewListAdapter;
import com.jsrd.budgetmart.interfaces.CartCallBack;
import com.jsrd.budgetmart.interfaces.ProductCallBack;
import com.jsrd.budgetmart.model.Cart;
import com.jsrd.budgetmart.model.Product;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.utils.FirestoreFirebase;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView productListRecyclerView;
    private ShimmerFrameLayout productListShimmerContainer;
    private Toolbar productListToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        productListToolbar = findViewById(R.id.productListToolbar);
        productListToolbar.setTitle("Product List");
        setSupportActionBar(productListToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        productListRecyclerView = findViewById(R.id.productListRecyclerView);
        productListRecyclerView.setHasFixedSize(true);
        productListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productListShimmerContainer = findViewById(R.id.productListShimmerContainer);
        productListShimmerContainer.startShimmerAnimation();


        final FirestoreFirebase ff = new FirestoreFirebase(this);
        String collection = "Grocery";

        ff.getProductFromFirbase(collection, new ProductCallBack() {
            @Override
            public void onComplete(final ArrayList<Product> products) {
                ff.getProductsFromCart(new CartCallBack() {
                    @Override
                    public void onComplete(ArrayList<Cart> cartList) {
                        ProductRecyclerViewListAdapter productAdapter =
                                new ProductRecyclerViewListAdapter(ProductListActivity.this, products, cartList);
                        productListRecyclerView.setAdapter(productAdapter);
                        productListShimmerContainer.stopShimmerAnimation();
                        productListShimmerContainer.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}