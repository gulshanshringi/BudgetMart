package com.jsrd.budgetmart.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.adapter.CartAdapter;
import com.jsrd.budgetmart.fragments.AddressBottomSheetDialogFragment;
import com.jsrd.budgetmart.interfaces.AddressCallBack;
import com.jsrd.budgetmart.interfaces.CartCallBack;
import com.jsrd.budgetmart.model.Address;
import com.jsrd.budgetmart.model.Cart;
import com.jsrd.budgetmart.utils.FirestoreFirebase;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    FirestoreFirebase ff;
    private Button proceedBtn;
    private static TextView cartItemPrice, cartItemDiscount, cartItemDeliveryFee, cartItemTotalAmount;
    private RelativeLayout checkoutLayout;
    private ProgressBar checkoutProgressBar;

    private LinearLayout addressLayout;
    private TextView addressTxt;
    private Button addressBtn, changeAddressBtn;

    BottomSheetBehavior bottomSheetBehavior;
    RelativeLayout addressBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        proceedBtn = findViewById(R.id.proceedBtn);
        cartItemPrice = findViewById(R.id.cartItemPrice);
        cartItemDiscount = findViewById(R.id.cartItemDiscount);
        cartItemDeliveryFee = findViewById(R.id.cartItemDeliveryFee);
        cartItemTotalAmount = findViewById(R.id.cartItemTotalAmount);
        checkoutLayout = findViewById(R.id.checkoutLayout);
        checkoutProgressBar = findViewById(R.id.checkoutProgressBar);

        addressLayout = findViewById(R.id.addressLayout);
        addressTxt = findViewById(R.id.addressTxt);
        changeAddressBtn = findViewById(R.id.changeAddressBtn);
        addressBtn = findViewById(R.id.addAddressBtn);
        addressLayout.setVisibility(View.GONE);
        addressBottomSheet = findViewById(R.id.addressBottomSheet);


        checkoutLayout.setVisibility(View.GONE);
        Sprite threeBounce = new ThreeBounce();
        threeBounce.setColor(Color.RED);
        checkoutProgressBar.setIndeterminateDrawable(threeBounce);

        updateBillingDetails();
        setAddress(0);
        setBottomSheetBehavior();


        addressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressintent = new Intent(CheckoutActivity.this, AddressActivity.class);
                startActivity(addressintent);
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPaymentActivity();
            }
        });

        changeAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }

                */
               showBottomSheet();
            }
        });

    }


    public void setAddress(final int selectedAddress) {
        ff.getAddressFromFirebase(new AddressCallBack() {
            @Override
            public void onComplete(ArrayList<Address> addresses) {
                if (addresses.size() > 0) {
                    addressTxt.setText(addresses.get(selectedAddress).toString());
                    addressLayout.setVisibility(View.VISIBLE);
                    addressBtn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void startPaymentActivity() {
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        startActivity(paymentIntent);
    }

    private void updateBillingDetails() {
        ff = new FirestoreFirebase(this);
        ff.getProductsFromCart(new CartCallBack() {
            @Override
            public void onComplete(ArrayList<Cart> cartList) {
                int itemPriceTotal = 0;
                int discount;
                int totalPayable;
                for (Cart cart : cartList) {
                    itemPriceTotal = itemPriceTotal + (Integer.parseInt(cart.getQuantity()) * cart.getProduct().getPrice());
                }
                discount = (itemPriceTotal * (10 / 100));
                totalPayable = itemPriceTotal - discount;
                cartItemPrice.setText(Integer.toString(itemPriceTotal));
                cartItemDiscount.setText(Integer.toString(discount));
                cartItemDeliveryFee.setText("0");
                cartItemTotalAmount.setText(Integer.toString(totalPayable));
                checkoutProgressBar.setVisibility(View.GONE);
                checkoutLayout.setVisibility(View.VISIBLE);

            }
        });


    }

    private void setBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(addressBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:

                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        showBottomSheet();
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    public void showBottomSheet() {
        AddressBottomSheetDialogFragment addressBottomSheetDialogFragment = new AddressBottomSheetDialogFragment();
        addressBottomSheetDialogFragment.show(getSupportFragmentManager(),"AddressBottomSheetDialogFragment");
    }
}