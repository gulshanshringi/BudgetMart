package com.jsrd.budgetmart.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.interfaces.OrderCallBack;
import com.jsrd.budgetmart.utils.FirestoreFirebase;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity {

    private Toolbar paymentActivityToolbar;
    private RadioButton cashRadioBtn;
    private Button placeOrderBtn;
    private RelativeLayout orderSuccessLayout;
    private ProgressBar progressBar;

    private String serverKey = "key=" + "AAAAgWzstw4:APA91bEzC35pup-YxqrautyG2R-edQ-rnBEUQP6GU5HM0OYX85WnhWOBmslEb1xG1DG-e8DLYJU3In0zNq0L1JDFBUcYJb1Jo9-nWspobMTvKBDw5C9z8-QfRgV3PmgnLJCbnHW4V012";
    final private String FCM_API_URL = "https://fcm.googleapis.com/fcm/send";

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
                                sendNotification(orderId);
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

    private void sendNotification(final String orderId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    MediaType JSON
                            = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", "Order ID :" + orderId);
                    dataJson.put("title", "New Order Received");
                    json.put("notification", dataJson);
                    //json.put("to", regToken);
                    json.put("to", "/topics/orderPlaced");
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", serverKey)
                            .url(FCM_API_URL)
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                } catch (Exception e) {
                    //Log.d(TAG,e+"");
                }
                return null;
            }
        }.execute();

    }


}