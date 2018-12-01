package com.daan.android.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.daan.android.inventoryapp.settings.SettingsActivity;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    BottomNavigationFragment bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        showHome();

        bottomNavigation = (BottomNavigationFragment) getSupportFragmentManager().findFragmentById(R.id.frag_bottom_navbar);
        if (bottomNavigation != null) {
            bottomNavigation.bottomNavigation.setOnNavigationItemSelectedListener((item) -> {
                if (item.getItemId() == R.id.action_nav_barcode_scanner) {
                    showBarCodeScanner();
                } else if (item.getItemId() == R.id.action_nav_home) {
                    showHome();
                }
                return true;
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBarCodeScanner() {
        showFragment(new BarcodeScannerFragment());
    }

    private void showHome() {
        showFragment(new HomeFragment());
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
}
