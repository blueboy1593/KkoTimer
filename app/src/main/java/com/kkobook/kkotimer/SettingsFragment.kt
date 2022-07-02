package com.kkobook.kkotimer

import android.os.Bundle
import android.view.MenuItem
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return true
    }
}