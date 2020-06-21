package com.jsrd.budgetmart.activity;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.adapter.CartAdapter;
import com.jsrd.budgetmart.interfaces.CartCallBack;
import com.jsrd.budgetmart.model.Cart;
import com.jsrd.budgetmart.utils.FirestoreFirebase;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private Button proceedBtn;
    private static TextView cartItemPrice, cartItemDiscount, cartItemDeliveryFee, cartItemTotalAmount;
    private RelativeLayout checkoutLayout;
    private ProgressBar checkoutProgressBar;

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

        checkoutLayout.setVisibility(View.GONE);
        Sprite threeBounce = new ThreeBounce();
        threeBounce.setColor(Color.RED);
        checkoutProgressBar.setIndeterminateDrawable(threeBounce);


        FirestoreFirebase ff = new FirestoreFirebase(this);
        ff.getProductsFromCart(new CartCallBack() {
            @Override
            public void onComplete(ArrayList<Cart> cartList) {
                updateBillingDetails(cartList);
            }
        });


        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPaymentActivity();
            }
        });


    }

    private void startPaymentActivity() {
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        startActivity(paymentIntent);
    }

    private void updateBillingDetails(ArrayList<Cart> cartItems) {
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
            checkoutProgressBar.setVisibility(View.GONE);
            checkoutLayout.setVisibility(View.VISIBLE);
        }

    }


}