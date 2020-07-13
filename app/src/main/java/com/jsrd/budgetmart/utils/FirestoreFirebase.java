package com.jsrd.budgetmart.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jsrd.budgetmart.interfaces.AddressCallBack;
import com.jsrd.budgetmart.interfaces.CartCallBack;
import com.jsrd.budgetmart.interfaces.DataAddedCallBack;
import com.jsrd.budgetmart.interfaces.ProductCallBack;
import com.jsrd.budgetmart.model.Address;
import com.jsrd.budgetmart.model.Cart;
import com.jsrd.budgetmart.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirestoreFirebase {

    private final String TAG = this.getClass().getName();
    private FirebaseFirestore db;
    private Context mContext;

    public FirestoreFirebase(Context context) {
        mContext = context;
        db = FirebaseFirestore.getInstance();
    }


    public void getProductFromFirbase(final String productCategory, final ProductCallBack callBack) {
        final ArrayList<Product> products = new ArrayList<>();
        db.collection("Products").whereGreaterThanOrEqualTo("Category", productCategory).
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int productId = Integer.parseInt(document.getId());
                                String name = (String) document.get("Name");
                                String price = (String) document.get("Price");
                                String image = (String) document.get("Image");
                                String category = (String) document.get("Category");
                                Product product = new Product(productId, name, Integer.parseInt(price), image, category);
                                if (product.getCategory().contains(productCategory))
                                    products.add(product);
                            }
                            callBack.onComplete(products);
                        }
                    }
                });
    }

    public void putUserInfoOnFirestore(FirebaseUser user) {
        // Create a new user with a first and last name
        Map<String, Object> userData = new HashMap<>();
        userData.put("Number", user.getPhoneNumber());

        // Add a new document with a generated ID
        db.collection("users").document(user.getPhoneNumber())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void addProductToCart(String docId, Product product, String quantity, final DataAddedCallBack callBack) {
        Map<String, String> cartData = new HashMap<>();
        cartData.put("ProductId", String.valueOf(product.getId()));
        cartData.put("Quantity", quantity);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference collRef;
        if (docId != null) {
            collRef = db.collection("users").
                    document(user.getPhoneNumber()).
                    collection("Cart").
                    document(docId);
        } else {
            collRef = db.collection("users").
                    document(user.getPhoneNumber()).
                    collection("Cart").
                    document();
        }

        collRef.set(cartData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack.onSuccess(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailure(true);
            }
        });
    }

    public void getProductsFromCart(final CartCallBack callBack) {
        final ArrayList<Cart> cartList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users/" + user.getPhoneNumber() + "/Cart").
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final String cartId = document.getId();
                                final String productId = (String) document.get("ProductId");
                                final String quantity = (String) document.get("Quantity");


                                db.collection("Products").document(productId).
                                        get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                String id = document.getId();
                                                String name = (String) document.get("Name");
                                                String price = (String) document.get("Price");
                                                String image = (String) document.get("Image");
                                                String category = (String) document.get("Category");
                                                Product product = new Product(Integer.parseInt(id), name, Integer.parseInt(price), image,category);
                                                Cart cart = new Cart(cartId, product, quantity);
                                                cartList.add(cart);
                                            }
                                            callBack.onComplete(cartList);
                                        }


                                    }
                                });
                            }
                            if (task.getResult().isEmpty()) {
                                callBack.onComplete(cartList);
                            }
                        }
                    }
                });
    }

    public void removeProductFromCart(String docId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference collRef = db.collection("users").
                document(user.getPhoneNumber()).
                collection("Cart").
                document(docId);
        collRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "Product removed from cart", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addAddressIntoFirebase(String docId, Address address, final DataAddedCallBack callBack) {
        Map<String, String> addressData = new HashMap<>();
        addressData.put("Name", address.getName());
        addressData.put("Address", address.getAddress());
        addressData.put("Pincode", address.getPincode());
        addressData.put("City", address.getCity());
        addressData.put("State", address.getState());
        addressData.put("Country", address.getCountry());
        addressData.put("Mobile Number", address.getMobileNo());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference collRef;
        if (docId != null) {
            collRef = db.collection("users").
                    document(user.getPhoneNumber()).
                    collection("Addresses").
                    document(docId);
        } else {
            collRef = db.collection("users").
                    document(user.getPhoneNumber()).
                    collection("Addresses").
                    document();
        }

        collRef.set(addressData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack.onSuccess(true);
                Toast.makeText(mContext, "Address saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAddressFromFirebase(final AddressCallBack callBack) {
        final ArrayList<Cart> cartList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users/" + user.getPhoneNumber() + "/Addresses").
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Address> addresses = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String addressId = document.getId();
                                String name = (String) document.get("Name");
                                String add = (String) document.get("Address");
                                String pincode = (String) document.get("Pincode");
                                String city = (String) document.get("City");
                                String state = (String) document.get("State");
                                String country = (String) document.get("Country");
                                String mobileNo = (String) document.get("Mobile Number");

                                Address address = new Address(addressId, name, add, pincode, city, state, country, mobileNo);
                                addresses.add(address);
                            }

                            callBack.onComplete(addresses);
                        }
                    }
                });
    }

    public void searchProductFromFirbase(final String productName, final ProductCallBack callBack) {
        final ArrayList<Product> products = new ArrayList<>();
        db.collection("Products").whereGreaterThanOrEqualTo("Name", productName).
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                int productId = Integer.parseInt(document.getId());
                                String name = (String) document.get("Name");
                                String price = (String) document.get("Price");
                                String image = (String) document.get("Image");
                                String category = (String) document.get("Category");
                                Product product = new Product(productId, name, Integer.parseInt(price), image,category);
                                if (product.getName().contains(productName)) {
                                    products.add(product);
                                }
                            }
                            callBack.onComplete(products);
                        }
                    }
                });
    }


}
