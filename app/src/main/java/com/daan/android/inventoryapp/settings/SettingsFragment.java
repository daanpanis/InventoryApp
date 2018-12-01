package com.daan.android.inventoryapp.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.daan.android.inventoryapp.LoginActivity;
import com.daan.android.inventoryapp.R;
import com.daan.android.inventoryapp.firebase.AuthenticationService;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.settings);

        Preference preference = findPreference(getString(R.string.pref_signout_key));
        preference.setOnPreferenceClickListener(pref -> {
            AuthenticationService.of(getActivity()).logout();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        });
    }
}
