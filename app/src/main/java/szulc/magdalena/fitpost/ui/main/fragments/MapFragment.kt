package szulc.magdalena.fitpost.ui.main.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import szulc.magdalena.fitpost.R

/**
 * Class to manage map fragment
 * */

class MapFragment : Fragment() {


    private val  ERROR_DIALOG_REQUEST = 90001


    private lateinit var viewOfFragment:View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfFragment =  inflater.inflate(R.layout.fragment_tab1,container,false)

        checkService()

        Log.i("MAP","Map activity Tab started.")

        return viewOfFragment
    }

    fun checkService(): Boolean {

        val avaliable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(viewOfFragment.context)

        if (avaliable == ConnectionResult.SUCCESS){
            Log.i("MapFragment","Services are working")
            return true
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(avaliable)){
            Log.w("MapFragment","Unexpected error while connecting Google services. ")
            val dialog = GoogleApiAvailability.getInstance().getErrorDialog(viewOfFragment.context as Activity?,avaliable,ERROR_DIALOG_REQUEST)
        }

          Log.w("MapFragment","Services connection result not successful")
         return false

    }


    companion object {

        @JvmStatic
        fun newInstance(sectionNumber: Int): MapFragment {
            return MapFragment().apply {
                arguments = Bundle().apply {
                    putInt("1", sectionNumber)
                }
            }
        }
    }

}