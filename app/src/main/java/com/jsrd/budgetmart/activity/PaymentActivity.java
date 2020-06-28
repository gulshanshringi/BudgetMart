package com.jsrd.budgetmart.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jsrd.budgetmart.R;

public class PaymentActivity extends AppCompatActivity {

    private Toolbar paymentActivityToolbar;
    private RadioButton cashRadioBtn;
    private Button placeOrderBtn;
    private RelativeLayout orderSuccessLayout;

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

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cashRadioBtn.isChecked()) {
                    orderSuccessLayout.setVisibility(View.VISIBLE);
                    placeOrderBtn.setVisibility(View.GONE);
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