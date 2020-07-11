package com.jsrd.budgetmart.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jsrd.budgetmart.activity.MainActivity;
import com.jsrd.budgetmart.activity.ProductListActivity;
import com.jsrd.budgetmart.adapter.CategoryRecyclerViewAdapter;
import com.jsrd.budgetmart.adapter.ProductRecyclerViewGridAdapter;
import com.jsrd.budgetmart.adapter.ProductRecyclerViewListAdapter;
import com.jsrd.budgetmart.interfaces.CartCallBack;
import com.jsrd.budgetmart.interfaces.ProductCallBack;
import com.jsrd.budgetmart.model.Cart;
import com.jsrd.budgetmart.model.Product;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.utils.FirestoreFirebase;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView categoryRecyclerView;
    private RecyclerView productRecyclerView;
    private ArrayList<String> categoryList;
    private ShimmerFrameLayout homeShimmerContainer;
    private LinearLayout mainLayout;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ((MainActivity) getActivity()).activityMainToolbar.setTitle("Budget Mart");

        mainLayout = getView().findViewById(R.id.mainLayout);
        homeShimmerContainer = getView().findViewById(R.id.homeShimmerContainer);
        homeShimmerContainer.startShimmerAnimation();

        categoryList = new ArrayList<>();
        categoryList.add("Beverages");
        categoryList.add("Veggies");
        categoryList.add("Bread & Bakery");
        categoryList.add("Frozen Foods");


        categoryRecyclerView = getView().findViewById(R.id.catagoryRecyclerView);
        productRecyclerView = getView().findViewById(R.id.productRecyclerView);

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        CategoryRecyclerViewAdapter categoryAdapter = new CategoryRecyclerViewAdapter(getContext(), categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        String collection = "Products/0/Grocery/";
        final FirestoreFirebase ff = new FirestoreFirebase(getContext());
        ff.getProductFromFirbase(collection, new ProductCallBack() {
            @Override
            public void onComplete(final ArrayList<Product> products) {
                ff.getProductsFromCart(new CartCallBack() {
                    @Override
                    public void onComplete(ArrayList<Cart> cartList) {
                        ProductRecyclerViewGridAdapter productAdapter = new ProductRecyclerViewGridAdapter(getContext(), products, cartList);
                        productRecyclerView.setAdapter(productAdapter);
                        homeShimmerContainer.setVisibility(View.GONE);
                        homeShimmerContainer.stopShimmerAnimation();
                        mainLayout.setVisibility(View.VISIBLE);
                    }
                });


            }
        });
    }


}
