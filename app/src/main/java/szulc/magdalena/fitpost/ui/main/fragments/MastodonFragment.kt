package szulc.magdalena.fitpost.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import szulc.magdalena.fitpost.R


/**
 * Class to manage mastodon fragment
 * */

class MastodonFragment : Fragment() {

    private lateinit var viewOfFragment:View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfFragment =  inflater.inflate(R.layout.fragment_tab2,container,false)

        Log.i("Mastodon","Post activity Tab started.")



        return viewOfFragment
    }

    companion object {

        @JvmStatic
        fun newInstance(sectionNumber: Int): MastodonFragment {
            return MastodonFragment().apply {
                arguments = Bundle().apply {
                    putInt("2", sectionNumber)
                }
            }
        }
    }

}