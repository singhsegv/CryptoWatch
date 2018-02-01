package io.github.rajdeep1008.cryptodroid.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import io.github.rajdeep1008.cryptodroid.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);
    }
}
