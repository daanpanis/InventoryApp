package com.daan.android.inventoryapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

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

    private void showBarCodeScanner() {
        showFragment(new BarcodeScannerFragment());
    }

    private void showHome() {
        showFragment(new HomeFragment());
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, fragment); // f1_container is your FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
}
