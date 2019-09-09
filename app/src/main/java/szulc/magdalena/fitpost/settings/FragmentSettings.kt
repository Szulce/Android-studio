package szulc.magdalena.fitpost.settings


import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import szulc.magdalena.fitpost.R
import java.io.File


class FragmentSettings : PreferenceFragmentCompat(),SharedPreferences.OnSharedPreferenceChangeListener {


    override fun onCreatePreferences(savedInstanceState: Bundle?,rootKey:String?){
         setPreferencesFromResource(R.xml.mastodon_preferences,rootKey)
        }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

        print("delete access file")
        var filename = context?.filesDir?.absolutePath
        val file = File(filename+"/credl.xml")
        file.delete()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }



}

