package szulc.magdalena.fitpost.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import szulc.magdalena.fitpost.R

/**
 * Class to manage map fragment
 * */

class MapFragment {


    private lateinit var viewOfFragment:View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfFragment =  inflater.inflate(R.layout.fragment_tab3,container,false)


        Log.i("MAP","Map activity Tab started.")

        return viewOfFragment
    }



    companion object {

        @JvmStatic
        fun newInstance(sectionNumber: Int): TimerFragment {
            return TimerFragment().apply {
                arguments = Bundle().apply {
                    putInt("1", sectionNumber)
                }
            }
        }
    }

}