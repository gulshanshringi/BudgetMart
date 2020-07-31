package com.jsrd.budgetmart.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.interfaces.OrderCallBack;
import com.jsrd.budgetmart.utils.FirestoreFirebase;

public class PaymentActivity extends AppCompatActivity {

    private Toolbar paymentActivityToolbar;
    private RadioButton cashRadioBtn;
    private Button placeOrderBtn;
    private RelativeLayout orderSuccessLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //setting up toolbar
        paymentActivityToolbar = findViewById(R.id.paymentActivityToolbar);
        paymentActivityToolbar.setTitle("Payment");
        setSupportActionBar(paymentActivityToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        cashRadioBtn = findViewById(R.id.cashRadioBtn);
        placeOrderBtn = findViewById(R.id.placeOrderBtn);
        orderSuccessLayout = findViewById(R.id.orderSuccessLayout);
        progressBar = findViewById(R.id.progressBar_paymentAct);

        Sprite fadingCircle = new FadingCircle();
        fadingCircle.setColor(Color.BLUE);
        progressBar.setIndeterminateDrawable(fadingCircle);


        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cashRadioBtn.isChecked()) {
                    progressBar.setVisibility(View.VISIBLE);
                    FirestoreFirebase ff = new FirestoreFirebase(PaymentActivity.this);
                    ff.placeOrder(new OrderCallBack() {
                        @Override
                        public void onComplete(String orderId) {
                            if (orderId.length() > 0) {
                                TextView orderIdTxt = findViewById(R.id.orderIdTxt);
                                orderIdTxt.setText("Your Order ID is : " + orderId);
                                progressBar.setVisibility(View.GONE);
                                orderSuccessLayout.setVisibility(View.VISIBLE);
                                placeOrderBtn.setVisibility(View.GONE);
                            }
                        }
                    });


                } else {
                    Toast.makeText(PaymentActivity.this, "Please Select a Payment Method", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}