package com.jsrd.budgetmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.activity.ProductDetailsActivity;
import com.jsrd.budgetmart.interfaces.CartCallBack;
import com.jsrd.budgetmart.interfaces.DataAddedCallBack;
import com.jsrd.budgetmart.model.Cart;
import com.jsrd.budgetmart.model.Product;
import com.jsrd.budgetmart.utils.FirestoreFirebase;
import com.jsrd.budgetmart.utils.StorageFirebase;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductRecyclerViewGridAdapter extends RecyclerView.Adapter<ProductRecyclerViewGridAdapter.ProductViewHolder> {

    private Context mContext;
    private ArrayList<Product> productList;
    private ArrayList<Cart> cartArrayList;
    FirestoreFirebase ff;

    public ProductRecyclerViewGridAdapter(Context context, ArrayList<Product> productList, ArrayList<Cart> cartList) {
        mContext = context;
        ff = new FirestoreFirebase(mContext);
        this.productList = productList;
        this.cartArrayList = cartList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {

        Product product = productList.get(position);
        StorageFirebase sf = new StorageFirebase(mContext);
        sf.setImageToImageView(product.getImage(), holder.productImage);
        holder.productName.setText(product.getName());
        holder.productPrice.setText("Rs " + product.getPrice());

        if (cartArrayList.size() > 0) {
            for (Cart cart : cartArrayList) {
                if (product.getId() == cart.getProduct().getId()) {
                    holder.productAddToCartBtn.setVisibility(View.GONE);
                    holder.productIncrDecrLayout.setVisibility(View.VISIBLE);
                    holder.productWeight.setText(cart.getQuantity());
                    break;
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName;

        private TextView productPrice;
        private TextView productWeight;
        private TextView productWeightDecrement;
        private TextView productWeightIncrement;
        private RelativeLayout productIncrDecrLayout;
        private TextView productAddToCartBtn;
        private ProgressBar productWeightProgressBar;
        private String incrementedWeight;
        private String decrementedWeight;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);

            productPrice = itemView.findViewById(R.id.productPrice);
            productWeight = itemView.findViewById(R.id.productWeight);
            productWeightDecrement = itemView.findViewById(R.id.productWeightDecrement);
            productWeightIncrement = itemView.findViewById(R.id.productWeightIncrement);
            productIncrDecrLayout = itemView.findViewById(R.id.productIncrDecrLayout);
            productAddToCartBtn = itemView.findViewById(R.id.productAddToCartBtn);
            productWeightProgressBar = itemView.findViewById(R.id.productWeightProgressBar);


            Sprite fadingCircle = new FadingCircle();
            fadingCircle.setColor(Color.BLUE);
            productWeightProgressBar.setIndeterminateDrawable(fadingCircle);


            productAddToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = productList.get(getAdapterPosition());
                    ff.addProductToCart(null, product, "1", new DataAddedCallBack() {
                        @Override
                        public void onSuccess(boolean successful) {
                            if (successful) {

                                getProductFromCart();
                            }
                        }

                        @Override
                        public void onFailure(boolean failed) {

                        }
                    });
                }
            });

            productWeightIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productWeightProgressBar.setVisibility(View.VISIBLE);
                    Product product = productList.get(getAdapterPosition());
                    for (Cart cart : cartArrayList) {
                        if (product.getId() == cart.getProduct().getId()) {
                            incrementedWeight = String.valueOf(Integer.parseInt(productWeight.getText().toString()) + 1);
                            ff.addProductToCart(cart.getCartId(), product, incrementedWeight, new DataAddedCallBack() {
                                @Override
                                public void onSuccess(boolean successful) {
                                    if (successful) {
                                        productWeight.setText(incrementedWeight);
                                        productWeightProgressBar.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onFailure(boolean failed) {
                                    productWeightProgressBar.setVisibility(View.GONE);
                                }
                            });
                            break;
                        }
                    }
                }
            });

            productWeightDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productWeightProgressBar.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(productWeight.getText().toString()) > 1) {
                        Product product = productList.get(getAdapterPosition());
                        for (Cart cart : cartArrayList) {
                            if (product.getId() == cart.getProduct().getId()) {
                                decrementedWeight = String.valueOf(Integer.parseInt(productWeight.getText().toString()) - 1);
                                ff.addProductToCart(cart.getCartId(), product, decrementedWeight, new DataAddedCallBack() {
                                    @Override
                                    public void onSuccess(boolean successful) {
                                        productWeight.setText(decrementedWeight);
                                        productWeightProgressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(boolean failed) {
                                        productWeightProgressBar.setVisibility(View.GONE);
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
                                productAddToCartBtn.setVisibility(View.VISIBLE);
                                productIncrDecrLayout.setVisibility(View.GONE);
                                productWeightProgressBar.setVisibility(View.GONE);
                                break;
                            }
                        }

                    }
                }
            });

        }

        private void getProductFromCart() {
            ff.getProductsFromCart(new CartCallBack() {
                @Override
                public void onComplete(ArrayList<Cart> cartList) {
                    cartArrayList = cartList;
                    productAddToCartBtn.setVisibility(View.GONE);
                    productIncrDecrLayout.setVisibility(View.VISIBLE);
                }
            });
        }
    }


}
