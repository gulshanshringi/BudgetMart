package com.jsrd.budgetmart.interfaces;

import com.jsrd.budgetmart.model.Product;

import java.util.ArrayList;

public interface ProductCallBack {

    void onComplete(ArrayList<Product> products);

}
