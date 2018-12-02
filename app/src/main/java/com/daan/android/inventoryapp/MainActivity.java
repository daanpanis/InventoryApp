package com.daan.android.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.daan.android.inventoryapp.firebase.ItemStoreService;
import com.daan.android.inventoryapp.settings.SettingsActivity;

import butterknife.ButterKnife;

import static com.daan.android.inventoryapp.utils.Constants.ACTION_SEARCH;
import static com.daan.android.inventoryapp.utils.Constants.BARCODE_EXTRA;
import static com.daan.android.inventoryapp.utils.Constants.BARCODE_FORMAT_EXTRA;

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
        BarcodeScannerFragment barcodeScanner = new BarcodeScannerFragment();
        barcodeScanner.setScannerListener((content, format) -> {
            Intent intent = new Intent(ACTION_SEARCH);
            intent.putExtra(BARCODE_EXTRA, content);
            intent.putExtra(BARCODE_FORMAT_EXTRA, format);
            startActivity(intent);
        });
        showFragment(barcodeScanner);
    }

    private void showHome() {
        ItemListFragment fragment = new ItemListFragment();
        fragment.setItemsObservable(ItemStoreService.getInstance().getUserItems());
        showFragment(fragment);
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
}
