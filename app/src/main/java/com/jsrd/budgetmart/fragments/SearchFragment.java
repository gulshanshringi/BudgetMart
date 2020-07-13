package com.jsrd.budgetmart.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.activity.MainActivity;
import com.jsrd.budgetmart.adapter.ProductRecyclerViewGridAdapter;
import com.jsrd.budgetmart.interfaces.CartCallBack;
import com.jsrd.budgetmart.interfaces.ProductCallBack;
import com.jsrd.budgetmart.model.Cart;
import com.jsrd.budgetmart.model.Product;
import com.jsrd.budgetmart.utils.FirestoreFirebase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    private RecyclerView searchRecyclerView;
    private EditText searchtxt;
    private ProgressBar progressBar;
    private LinearLayout blankLayout, emptySearchLayout;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).activityMainToolbar.setTitle("Search Products");

        searchRecyclerView = getView().findViewById(R.id.searchRecyclerView);
        searchtxt = getView().findViewById(R.id.searchTxt);
        progressBar = getView().findViewById(R.id.progressBarSearchFragment);
        emptySearchLayout = getView().findViewById(R.id.emptySearchLayout);
        blankLayout = getView().findViewById(R.id.blankLayout);

        Sprite fadingCircle = new FadingCircle();
        fadingCircle.setColor(Color.YELLOW);
        progressBar.setIndeterminateDrawable(fadingCircle);


        searchRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        searchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String productName = s.toString();
                if (productName.length() > 2) {
                    blankLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    final FirestoreFirebase ff = new FirestoreFirebase(getContext());
                    ff.searchProductFromFirbase(productName, new ProductCallBack() {
                        @Override
                        public void onComplete(final ArrayList<Product> products) {
                            if (products.size() > 0) {
                                ff.getProductsFromCart(new CartCallBack() {
                                    @Override
                                    public void onComplete(ArrayList<Cart> cartList) {
                                        ProductRecyclerViewGridAdapter productAdapter = new ProductRecyclerViewGridAdapter(getContext(), products, cartList);
                                        searchRecyclerView.setAdapter(productAdapter);
                                        searchRecyclerView.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        emptySearchLayout.setVisibility(View.GONE);
                                    }
                                });

                            } else {
                                searchRecyclerView.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                emptySearchLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else if (productName.length() == 0) {
                    searchRecyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    blankLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}
