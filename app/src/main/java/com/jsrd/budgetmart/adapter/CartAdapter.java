package com.jsrd.budgetmart.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.jsrd.budgetmart.fragments.CartFragment;
import com.jsrd.budgetmart.interfaces.CartCallBack;
import com.jsrd.budgetmart.interfaces.DataAddedCallBack;
import com.jsrd.budgetmart.model.Cart;
import com.jsrd.budgetmart.utils.FirestoreFirebase;
import com.jsrd.budgetmart.utils.StorageFirebase;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<Cart> cartItems;
    private Context mContext;
    FirestoreFirebase ff;

    public CartAdapter(Context context, ArrayList<Cart> cartItems) {
        mContext = context;
        this.cartItems = cartItems;
        ff = new FirestoreFirebase(mContext);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartItems.get(position);
        StorageFirebase sf = new StorageFirebase(mContext);
        sf.setImageToImageView(cart.getProduct().getImage(), holder.itemImage);
        holder.itemName.setText(cart.getProduct().getName());
        holder.itemPrice.setText(Integer.toString(cart.getProduct().getPrice()) + " Rs Per kg");
        holder.itemWeight.setText(cart.getQuantity());

        if (position == cartItems.size()-1){
            CartFragment.updateBillingDetails(cartItems);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
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

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemprice);
            itemWeight = itemView.findViewById(R.id.itemWeight);
            itemWeightDecrement = itemView.findViewById(R.id.itemWeightDecrement);
            itemWeightIncrement = itemView.findViewById(R.id.itemWeightIncrement);
            itemIncrDecrLayout = itemView.findViewById(R.id.itemIncrDecrLayout);
            itemWeightProgressBar = itemView.findViewById(R.id.itemWeightProgressBar);
            itemAddToCartBtn = itemView.findViewById(R.id.itemAddToCartBtn);

            itemAddToCartBtn.setVisibility(View.GONE);
            itemIncrDecrLayout.setVisibility(View.VISIBLE);

            Sprite fadingCircle = new FadingCircle();
            fadingCircle.setColor(Color.BLUE);
            itemWeightProgressBar.setIndeterminateDrawable(fadingCircle);


            itemWeightIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemWeightProgressBar.setVisibility(View.VISIBLE);
                    Cart cart = cartItems.get(getAdapterPosition());
                    incrementedWeight = String.valueOf(Integer.parseInt(itemWeight.getText().toString()) + 1);
                    ff.addProductToCart(cart.getCartId(), cart.getProduct(), incrementedWeight, new DataAddedCallBack() {
                        @Override
                        public void onSuccess(boolean successful) {
                            if (successful) {
                                itemWeight.setText(incrementedWeight);
                                itemWeightProgressBar.setVisibility(View.GONE);
                                updateCart();
                            }
                        }

                        @Override
                        public void onFailure(boolean failed) {
                            itemWeightProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });

            itemWeightDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemWeightProgressBar.setVisibility(View.VISIBLE);
                    Cart cart = cartItems.get(getAdapterPosition());
                    if (Integer.parseInt(itemWeight.getText().toString()) > 1) {
                        decrementedWeight = String.valueOf(Integer.parseInt(itemWeight.getText().toString()) - 1);
                        ff.addProductToCart(cart.getCartId(), cart.getProduct(), decrementedWeight, new DataAddedCallBack() {
                            @Override
                            public void onSuccess(boolean successful) {
                                itemWeight.setText(decrementedWeight);
                                itemWeightProgressBar.setVisibility(View.GONE);
                                updateCart();
                            }

                            @Override
                            public void onFailure(boolean failed) {
                                itemWeightProgressBar.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        CartFragment.cartLayout.setVisibility(View.GONE);
                        ff.removeProductFromCart(cart.getCartId());
                        cartItems.remove(getAdapterPosition());
                        itemWeightProgressBar.setVisibility(View.GONE);
                        CartAdapter.this.notifyDataSetChanged();
                        updateCart();
                    }
                }
            });

        }
    }
    public void updateCart() {
        ff.getProductsFromCart(new CartCallBack() {
            @Override
            public void onComplete(ArrayList<Cart> cartList) {
                cartItems = cartList;
                notifyDataSetChanged();
                CartFragment.updateBillingDetails(cartItems);
            }
        });
    }
}
