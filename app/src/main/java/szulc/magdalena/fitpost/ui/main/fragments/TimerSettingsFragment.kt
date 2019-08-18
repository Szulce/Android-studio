package szulc.magdalena.fitpost.ui.main.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import szulc.magdalena.fitpost.R

class TimerSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}