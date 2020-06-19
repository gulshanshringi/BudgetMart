package com.jsrd.budgetmart.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsrd.budgetmart.model.Product;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.utils.FirestoreFirebase;
import com.jsrd.budgetmart.utils.StorageFirebase;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView itemImage;
    private TextView itemName;
    private TextView itemPrice;
    private TextView itemWeight;
    private TextView itemWeightDecrement;
    private TextView itemWeightIncrement;
    private Product product;
    private Button addToCartBtn;
    private StorageFirebase sf;
    private FirestoreFirebase ff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        product = null;
        Bundle bundle = getIntent().getBundleExtra("Item");
        if (bundle != null) {
            product = (Product) bundle.getSerializable("Item");
        }

        itemImage = findViewById(R.id.detailsItemImage);
        itemName = findViewById(R.id.detailsItemName);
        itemPrice = findViewById(R.id.detailsItemprice);
        itemWeight = findViewById(R.id.detailsItemWeight);
        itemWeightDecrement = findViewById(R.id.detailsItemWeightDecrement);
        itemWeightIncrement = findViewById(R.id.detailsItemWeightIncrement);
        addToCartBtn = findViewById(R.id.addToCartBtn);

        showProduct();


        itemWeightIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int incrementedWeight = Integer.parseInt(itemWeight.getText().toString()) + 1;
                itemWeight.setText(Integer.toString(incrementedWeight));
            }
        });

        itemWeightDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(itemWeight.getText().toString()) > 0) {
                    int decrementedWeight = Integer.parseInt(itemWeight.getText().toString()) - 1;
                    itemWeight.setText(Integer.toString(decrementedWeight));
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ff = new FirestoreFirebase(ProductDetailsActivity.this);
                //ff.addProductToCart(product,itemWeight.getText().toString());
            }
        });

    }

    private void showProduct() {
        sf = new StorageFirebase(this);
        sf.setImageToImageView(product.getImage(),itemImage);
        itemName.setText(product.getName());
        itemPrice.setText(Integer.toString(product.getPrice()));
    }
}
