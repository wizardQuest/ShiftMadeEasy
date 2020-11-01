package com.pq.shiftmadeeasy.ui.settingsfragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.pq.shiftmadeeasy.R
import com.pq.shiftmadeeasy.base.hideBottomAppBarFAB
import com.pq.shiftmadeeasy.base.showToolbar

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        activity?.apply {
            hideBottomAppBarFAB()
            showToolbar("Settings")
        }
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
    }
}