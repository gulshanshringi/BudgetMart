package com.jsrd.budgetmart.interfaces;

import com.jsrd.budgetmart.model.Cart;

import java.util.ArrayList;

public interface CartCallBack {

    void onComplete(ArrayList<Cart> cartList);

}
