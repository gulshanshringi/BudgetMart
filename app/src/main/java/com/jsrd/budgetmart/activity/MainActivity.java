package com.jsrd.budgetmart.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jsrd.budgetmart.R;
import com.jsrd.budgetmart.fragments.AccountFragment;
import com.jsrd.budgetmart.fragments.CartFragment;
import com.jsrd.budgetmart.fragments.HomeFragment;
import com.jsrd.budgetmart.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private CartFragment cartFragment;
    private AccountFragment accountFragment;
    public Toolbar activityMainToolbar;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainToolbar = (Toolbar) findViewById(R.id.activityMainToolbar);
        activityMainToolbar.setTitle("Budget Mart");
        setSupportActionBar(activityMainToolbar);

        setUpBottomNavigationBar();
        retriveCurrentRegistrationToken();
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, homeFragment).commit();
    }


    private void retriveCurrentRegistrationToken() {
        token = FirebaseInstanceId.getInstance().getToken();
    }

    private void setUpBottomNavigationBar() {
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        homeFragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, homeFragment).commit();
                        break;
                    case R.id.search:
                        searchFragment = new SearchFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout, searchFragment).commit();
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
