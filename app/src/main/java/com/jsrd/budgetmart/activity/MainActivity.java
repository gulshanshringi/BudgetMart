package com.jsrd.budgetmart.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.fragments.CartFragment;
import com.jsrd.budgetmart.fragments.FavoritesFragment;
import com.jsrd.budgetmart.fragments.HomeFragment;
import com.jsrd.budgetmart.fragments.AccountFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private FavoritesFragment favoritesFragment;
    private CartFragment cartFragment;
    private AccountFragment accountFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpBottomNavigationBar();

        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, homeFragment).commit();
    }

    private void setUpBottomNavigationBar(){
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){

                    case R.id.home:
                        homeFragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, homeFragment).commit();
                        break;
                    case R.id.favorites:
                        favoritesFragment = new FavoritesFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, favoritesFragment).commit();
                        break;
                    case R.id.cart:
                        cartFragment = new CartFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, cartFragment).commit();
                        break;
                    case R.id.account:
                        accountFragment = new AccountFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, accountFragment).commit();
                        break;

                }
                return true;
            }
        });
    }



}
