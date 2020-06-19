package com.jsrd.budgetmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.activity.ProductDetailsActivity;
import com.jsrd.budgetmart.interfaces.CartCallBack;
import com.jsrd.budgetmart.interfaces.ProductAddedCallBack;
import com.jsrd.budgetmart.model.Cart;
import com.jsrd.budgetmart.model.Product;
import com.jsrd.budgetmart.utils.FirestoreFirebase;
import com.jsrd.budgetmart.utils.StorageFirebase;

import java.io.Serializable;
import java.util.ArrayList;


public class ProductRecyclerViewListAdapter extends RecyclerView.Adapter<ProductRecyclerViewListAdapter.RecyclerViewHolder> {


    private Context mContext;
    private ArrayList<Product> productList;
    private ArrayList<Cart> cartArrayList;
    FirestoreFirebase ff;

    public ProductRecyclerViewListAdapter(Context context, ArrayList<Product> productList, ArrayList<Cart> cartList) {
        mContext = context;
        ff = new FirestoreFirebase(mContext);
        this.productList = productList;
        this.cartArrayList = cartList;

    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Product product = productList.get(position);
        StorageFirebase sf = new StorageFirebase(mContext);
        sf.setImageToImageView(product.getImage(), holder.itemImage);
        holder.itemName.setText(product.getName());
        holder.itemPrice.setText(Integer.toString(product.getPrice()) + " Rs Per kg");

        if (cartArrayList.size() > 0) {
            for (Cart cart : cartArrayList) {
                if (product.getId() == cart.getProduct().getId()) {
                    holder.itemAddToCartBtn.setVisibility(View.GONE);
                    holder.itemIncrDecrLayout.setVisibility(View.VISIBLE);
                    holder.itemWeight.setText(cart.getQuantity());
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemName;
        private TextView itemPrice;
        private TextView itemWeight;
        private TextView itemWeightDecrement;
        private TextView itemWeightIncrement;
        private RelativeLayout itemIncrDecrLayout;
        private TextView itemAddToCartBtn;
        private ProgressBar itemWeightProgressBar;
        private String incrementedWeight;
        private String decrementedWeight;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemprice);
            itemWeight = itemView.findViewById(R.id.itemWeight);
            itemWeightDecrement = itemView.findViewById(R.id.itemWeightDecrement);
            itemWeightIncrement = itemView.findViewById(R.id.itemWeightIncrement);
            itemIncrDecrLayout = itemView.findViewById(R.id.itemIncrDecrLayout);
            itemAddToCartBtn = itemView.findViewById(R.id.itemAddToCartBtn);
            itemWeightProgressBar = itemView.findViewById(R.id.itemWeightProgressBar);


            Sprite fadingCircle = new FadingCircle();
            fadingCircle.setColor(Color.BLUE);
            itemWeightProgressBar.setIndeterminateDrawable(fadingCircle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent itemDetailsIntent = new Intent(mContext, ProductDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Item", (Serializable) productList.get(getAdapterPosition()));
                    itemDetailsIntent.putExtra("Item", bundle);
                    mContext.startActivity(itemDetailsIntent);
                }
            });


            itemAddToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = productList.get(getAdapterPosition());
                    ff.addProductToCart(null, product, "1", new ProductAddedCallBack() {
                        @Override
                        public void onSuccess(boolean successful) {
                            if (successful) {
                                itemAddToCartBtn.setVisibility(View.GONE);
                                itemIncrDecrLayout.setVisibility(View.VISIBLE);
                                //getProductFromCart();
                            }
                        }

                        @Override
                        public void onFailure(boolean failed) {

                        }
                    });
                }
            });

            itemWeightIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemWeightProgressBar.setVisibility(View.VISIBLE);
                    Product product = productList.get(getAdapterPosition());
                    for (Cart cart : cartArrayList) {
                        if (product.getId() == cart.getProduct().getId()) {
                            incrementedWeight = String.valueOf(Integer.parseInt(itemWeight.getText().toString()) + 1);
                            ff.addProductToCart(cart.getCartId(), product, incrementedWeight, new ProductAddedCallBack() {
                                @Override
                                public void onSuccess(boolean successful) {
                                    if (successful) {
                                        itemWeight.setText(incrementedWeight);
                                        itemWeightProgressBar.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onFailure(boolean failed) {
                                    itemWeightProgressBar.setVisibility(View.GONE);
                                }
                            });
                            break;
                        }
                    }
                }
            });

            itemWeightDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemWeightProgressBar.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(itemWeight.getText().toString()) > 1) {
                        Product product = productList.get(getAdapterPosition());
                        for (Cart cart : cartArrayList) {
                            if (product.getId() == cart.getProduct().getId()) {
                                decrementedWeight = String.valueOf(Integer.parseInt(itemWeight.getText().toString()) - 1);
                                ff.addProductToCart(cart.getCartId(), product, decrementedWeight, new ProductAddedCallBack() {
                                    @Override
                                    public void onSuccess(boolean successful) {
                                        itemWeight.setText(decrementedWeight);
                                        itemWeightProgressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(boolean failed) {
                                        itemWeightProgressBar.setVisibility(View.GONE);
                                    }
                                });
                                break;
                            }
                        }
                    } else {
                        Product product = productList.get(getAdapterPosition());
                        for (Cart cart : cartArrayList) {
                            if (product.getId() == cart.getProduct().getId()) {
                                ff.removeProductFromCart(cart.getCartId());
                                itemAddToCartBtn.setVisibility(View.VISIBLE);
                                itemIncrDecrLayout.setVisibility(View.GONE);
                                itemWeightProgressBar.setVisibility(View.GONE);
                                break;
                            }
                        }

                    }
                }
            });


        }
    }


}
