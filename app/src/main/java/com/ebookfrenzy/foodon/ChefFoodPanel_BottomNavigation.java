package com.ebookfrenzy.foodon;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ebookfrenzy.foodon.chefFoodPannel.ChefHomeFragment;
import com.ebookfrenzy.foodon.chefFoodPannel.ChefOrderFragment;
import com.ebookfrenzy.foodon.chefFoodPannel.ChefPendingOrderFragment;
import com.ebookfrenzy.foodon.chefFoodPannel.ChefPendingOrdersFragment;
import com.ebookfrenzy.foodon.chefFoodPannel.ChefProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.KeyStore;


public class ChefFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private KeyStore FirebaseInstanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_food_panel__bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.chef_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        String name = getIntent().getStringExtra("PAGE");
        if (name != null) {
            if (name.equalsIgnoreCase("Orderpage")) {
                loadcheffragment(new ChefPendingOrderFragment());
            } else if (name.equalsIgnoreCase("Confirmpage")) {
                loadcheffragment(new ChefOrderFragment());
            } else if (name.equalsIgnoreCase("AcceptOrderpage")) {
                loadcheffragment(new ChefHomeFragment());
            } else if (name.equalsIgnoreCase("Deliveredpage")) {
                loadcheffragment(new ChefHomeFragment());
            }
        } else {
            loadcheffragment(new ChefHomeFragment());
        }
    }


    private boolean loadcheffragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.chefHome:
                fragment = new ChefHomeFragment();
                break;

            case R.id.pendingorder:
                fragment = new ChefPendingOrdersFragment();
                break;

            case R.id.orders:
                fragment = new ChefOrderFragment();
                break;
            case R.id.chefprofile:
                fragment = new ChefProfileFragment();
                break;
        }
        return loadcheffragment(fragment);
    }
}
