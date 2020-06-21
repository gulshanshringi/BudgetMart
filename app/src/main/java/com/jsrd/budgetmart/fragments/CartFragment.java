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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.jsrd.budgetmart.activity.CheckoutActivity;
import com.jsrd.budgetmart.interfaces.CartCallBack;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.adapter.CartAdapter;
import com.jsrd.budgetmart.model.Address;
import com.jsrd.budgetmart.model.Cart;
import com.jsrd.budgetmart.model.Product;
import com.jsrd.budgetmart.utils.FirestoreFirebase;

import java.util.ArrayList;


public class CartFragment extends Fragment {

    private RecyclerView cartItemRecyclerView;
    private static LinearLayout billAmountLayout, emptyCartLayout;
    private static TextView cartItemPrice, cartItemDiscount, cartItemDeliveryFee, cartItemTotalAmount;
    public static ShimmerFrameLayout cartShimmerContainer;
    private LinearLayout addressLayout;
    private ArrayList<Address> addresses;
    public static LinearLayout cartLayout;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        billAmountLayout = getView().findViewById(R.id.billAmountLayout);
        cartItemPrice = getView().findViewById(R.id.cartItemPrice);
        cartItemDiscount = getView().findViewById(R.id.cartItemDiscount);
        cartItemDeliveryFee = getView().findViewById(R.id.cartItemDeliveryFee);
        cartItemTotalAmount = getView().findViewById(R.id.cartItemTotalAmount);
        cartItemRecyclerView = getView().findViewById(R.id.productListRecyclerView);
        emptyCartLayout = getView().findViewById(R.id.emptyCartLayout);
        cartShimmerContainer = getView().findViewById(R.id.cartShimmerContainer);
        addressLayout = getView().findViewById(R.id.addressLayout);
        cartLayout = getView().findViewById(R.id.cartLayout);

        Button checkoutBtn = getView().findViewById(R.id.chekoutBtn);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCheckoutActivity();
            }
        });

        cartLayout.setVisibility(View.GONE);
        cartShimmerContainer.setVisibility(View.VISIBLE);
        cartShimmerContainer.startShimmerAnimation();

        cartItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartItemRecyclerView.setHasFixedSize(true);

        FirestoreFirebase ff = new FirestoreFirebase(getContext());
        ff.getProductsFromCart(new CartCallBack() {
            @Override
            public void onComplete(ArrayList<Cart> cartList) {
                if (cartList.size() > 0) {
                    CartAdapter adapter = new CartAdapter(getContext(), cartList);
                    cartItemRecyclerView.setAdapter(adapter);
                    cartLayout.setVisibility(View.VISIBLE);
                } else {
                    cartShimmerContainer.setVisibility(View.GONE);
                    cartShimmerContainer.stopShimmerAnimation();
                    cartLayout.setVisibility(View.GONE);
                    emptyCartLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public static void updateBillingDetails(ArrayList<Cart> cartItems) {
        if (cartItems.size() > 0) {
            int itemPriceTotal = 0;
            int discount;
            int totalPayable;
            for (Cart cart : cartItems) {
                itemPriceTotal = itemPriceTotal + (Integer.parseInt(cart.getQuantity()) * cart.getProduct().getPrice());
            }
            discount = (itemPriceTotal * (10 / 100));
            totalPayable = itemPriceTotal - discount;
            cartItemPrice.setText(Integer.toString(itemPriceTotal));
            cartItemDiscount.setText(Integer.toString(discount));
            cartItemDeliveryFee.setText("0");
            cartItemTotalAmount.setText(Integer.toString(totalPayable));
            cartLayout.setVisibility(View.VISIBLE);
            emptyCartLayout.setVisibility(View.GONE);
        } else {
            cartLayout.setVisibility(View.GONE);
            emptyCartLayout.setVisibility(View.VISIBLE);
        }
        cartShimmerContainer.setVisibility(View.GONE);
        cartShimmerContainer.stopShimmerAnimation();

    }

    public void startCheckoutActivity() {
        Intent checkoutIntent = new Intent(getContext(), CheckoutActivity.class);
        startActivity(checkoutIntent);

    }
}
