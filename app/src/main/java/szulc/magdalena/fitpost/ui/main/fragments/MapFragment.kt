package szulc.magdalena.fitpost.ui.main.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_tab1.*
import szulc.magdalena.fitpost.R
import java.util.jar.Manifest

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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallBack()

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(viewOfFragment.context)
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            }
        }else{
            buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(viewOfFragment.context)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }

        botton_navigation_view.setOnNavigationItemReselectedListerer{ item ->
            when(item.itemId){
                R.id.action_restaurant -> nearByPlace("restaurant")
                R.id.action_gym-> nearByPlace("gym")
                R.id.action_market -> nearByPlace("market")
            }
        }

        return viewOfFragment
    }

    private fun nearByPlace(typePlace: String) {

    }

    private fun buildLocationCallBack() {
        locationCallback = object:LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.lastLocation //p0.locations.get(p0.locations.size-1)
                if(mMarer != null){
                    mMarer!!.remove()
                }
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                val latLng = LatLng(latitude,longitude)
                val markerOptions = MarkerOptions().position(latLng).title("Your position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                mMarer = nMap!!.addMarker(markerOptions)

                //camera
                nMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                nMap.animateCamera(CameraUpdateFactory.zoomTo(11f))

            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f

    }

    private fun checkLocationPermission() :Boolean{

        if(ContextCompat.checkSelfPermission(viewOfFragment.context,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    viewOfFragment.context as Activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    viewOfFragment.context as Activity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    viewOfFragment.context as Activity,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_CODE
                )
            }

            return false

        }
        return true
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

            //init service
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(viewOfFragment.context,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                nMap!!.isMyLocationEnabled = true
            }
        }else{
            nMap!!.isMyLocationEnabled = true
        }

        //enable zoom control
        nMap.uiSettings.isZoomControlsEnabled = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
      when(requestCode){
          MY_PERMISSION_CODE->{
              if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
              {
                  if(ContextCompat.checkSelfPermission(viewOfFragment.context,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                      if(checkLocationPermission()){
                          buildLocationRequest()
                          buildLocationCallBack()

                          fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(viewOfFragment.context)
                          fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                          nMap!!.isMyLocationEnabled = true
                      }
                  }
              }else{
                  Toast.makeText(viewOfFragment.context,getString(R.string.permision_denied),Toast.LENGTH_SHORT).show()
              }
          }
      }
    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }


    companion object {

        private val MY_PERMISSION_CODE:Int = 1000

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