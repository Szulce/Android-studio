package szulc.magdalena.fitpost.ui.main.fragments

import android.app.Activity
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import szulc.magdalena.fitpost.R

/**
 * Class to manage map fragment
 * */

class MapFragment : Fragment(), OnMapReadyCallback {


    private val ERROR_DIALOG_REQUEST = 90001
    private lateinit var nMap: GoogleMap
    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()
    private lateinit var mLastLocation: Location
    private var mMarer: Marker? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    private lateinit var viewOfFragment: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewOfFragment = inflater.inflate(R.layout.fragment_tab1, container, false)

        checkService()

        Log.i("MAP", "Map activity Tab started.")

        val mapFragment = fragmentManager?.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

        //runtime permision
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkLocationPermission()
            buildLocationRequest()
            buildLocationCallBack()
        }

        return viewOfFragment
    }

    private fun buildLocationCallBack() {
        
    }

    private fun buildLocationRequest() {

    }

    private fun checkLocationPermission() {

    }

    fun checkService(): Boolean {

        val avaliable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(viewOfFragment.context)

        if (avaliable == ConnectionResult.SUCCESS) {
            Log.i("MapFragment", "Services are working")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(avaliable)) {
            Log.w("MapFragment", "Unexpected error while connecting Google services. ")
            val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(viewOfFragment.context as Activity?, avaliable, ERROR_DIALOG_REQUEST)
        }

        Log.w("MapFragment", "Services connection result not successful")
        return false

    }

    override fun onMapReady(p0: GoogleMap) {
        nMap = p0
        //todo do usuniecia po teście
        val sydney = LatLng(-34.0, 151.0)
        nMap.addMarker(MarkerOptions().position(sydney).title("Marker in SYNDEY"))
        nMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

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