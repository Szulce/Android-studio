package szulc.magdalena.fitpost.ui.main.fragments

//import android.support.v7.app.AppCompatActivity

import android.Manifest
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
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_tab1.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import szulc.magdalena.fitpost.R
import szulc.magdalena.fitpost.remote.IGoogleAPIService
import szulc.magdalena.fitpost.remote.common.Common
import szulc.magdalena.fitpost.remote.model.MyPlaces

/**
 * Class to manage map fragment
 * */

class MapFragment : SupportMapFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private lateinit var nMap: GoogleMap
    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()
    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    private lateinit var viewOfFragment: View
    lateinit var mService: IGoogleAPIService
    internal lateinit var currentPlace: MyPlaces

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewOfFragment = inflater.inflate(R.layout.fragment_tab1, container, false)

        Log.i("MAP", "Map activity Tab started.")

        //MapsInitializer.initialize(viewOfFragment.context)

        //val mapFragment = fragmentManager.findViewById(R.id.map) as SupportMapView?
       // Log.d("MAP","mapFragment"+mapFragment.toString()+" "+this.activity.toString())

        getMapAsync(this)

        //init service
        mService = Common.googleApiService

        //runtime permision
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("MAP", "Build.VERSION.SDK_INT:" + Build.VERSION.SDK_INT)
            if (checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallBack()

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            }
        } else {
            Log.d("MAP", "NO PERMISSION")
            buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(viewOfFragment.context)
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }

        viewOfFragment.bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_restaurant -> nearByPlace("restaurant")
                R.id.action_gym -> nearByPlace("gym")
                R.id.action_market -> nearByPlace("market")
            }
            true
        }

        return viewOfFragment
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("MAP", "onMapReady")
        nMap = googleMap


        //todo do usuniecia po teście
//        val sydney = LatLng(-34.0, 151.0)
//        nMap.addMarker(MarkerOptions().position(sydney).title("Marker in SYNDEY"))
//        nMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


//        //init service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    viewOfFragment.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                nMap.isMyLocationEnabled = true
            }
        } else {
            nMap.isMyLocationEnabled = true
        }

        //enable zoom control
        nMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun nearByPlace(typePlace: String) {
            //clear markers on map
            nMap.clear()
            //request
            val url = getUrl(latitude, longitude, typePlace)

            mService.getNearbyPlaces(url).enqueue(object : Callback<MyPlaces> {
                override fun onResponse(call: Call<MyPlaces>?, response: Response<MyPlaces>?) {
                    currentPlace = response!!.body()
                    if (response.isSuccessful) {
                        for (i in 0 until response.body()!!.results!!.size) {
                            val markerOptions = MarkerOptions()
                            val googlePlace = response.body()!!.results!![i]
                            val lat = googlePlace.geometry!!.locaion!!.lat
                            val lng = googlePlace.geometry!!.locaion!!.lng
                            val placeName = googlePlace.name
                            val latLng = LatLng(lat, lng)

                            markerOptions.position(latLng)
                            markerOptions.title(placeName)

                            if (typePlace.equals("restaurant")) {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.id.action_restaurant))
                            } else if (typePlace.equals("gym")) {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.id.action_gym))
                            } else if (typePlace.equals("market")) {
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.id.action_market))
                            } else {
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                            }

                            markerOptions.snippet(i.toString()) //index for marker

                            //add marker on map
                            nMap.addMarker(markerOptions)
                        }
                        //move camera
                        nMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(latitude, longitude)))
                        nMap.animateCamera(CameraUpdateFactory.zoomTo(11f))
                    }
                }

                override fun onFailure(call: Call<MyPlaces>?, t: Throwable?) {
                    Toast.makeText(viewOfFragment.context, "" + t!!.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getUrl(latitude: Double, longitude: Double, typePlace: String): String {

        val googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")
        googlePlaceUrl.append("&radius=10000")//10km
        googlePlaceUrl.append("&type=$typePlace")
        googlePlaceUrl.append("&key=AIzaSyBBpxR0F5EW3BQRz7fWJr5BlAx8a_2PhoY")
        Log.d("URL_DEBUG", googlePlaceUrl.toString())
        return googlePlaceUrl.toString()

    }

    private fun buildLocationCallBack() {
        Log.d("MAP", "onLocationResut")
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult?) {
                    mLastLocation = p0!!.lastLocation //p0.locations.get(p0.locations.size-1)
                    if (mMarker != null) {
                        mMarker!!.remove()
                    }
                    latitude = mLastLocation.latitude
                    longitude = mLastLocation.longitude

                    val latLng = LatLng(latitude, longitude)
                    val markerOptions = MarkerOptions().position(latLng).title("Your position")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                    mMarker = nMap.addMarker(markerOptions)

                    //camera
                    nMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                    nMap.animateCamera(CameraUpdateFactory.zoomTo(11f))

                }
            }
    }


    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f

    }

    private fun checkLocationPermission(): Boolean {

        if (ContextCompat.checkSelfPermission(
                viewOfFragment.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    viewOfFragment.context as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    viewOfFragment.context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    viewOfFragment.context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_CODE
                )
            }

            Log.d("MAP", "Permision not granted")
            return false

        }
        Log.d("MAP", "Permision granted")
        return true
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            when (requestCode) {
                MY_PERMISSION_CODE -> {
                    Log.d("MAP", "onresult:" + requestCode)
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(
                                viewOfFragment.context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            if (checkLocationPermission()) {
                                buildLocationRequest()
                                buildLocationCallBack()

                                fusedLocationProviderClient =
                                    LocationServices.getFusedLocationProviderClient(viewOfFragment.context)
                                fusedLocationProviderClient.requestLocationUpdates(
                                    locationRequest,
                                    locationCallback,
                                    Looper.myLooper()
                                )
                                nMap.isMyLocationEnabled = true
                            }
                        }
                    } else {
                        Toast.makeText(viewOfFragment.context, getString(R.string.permision_denied), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        private val MY_PERMISSION_CODE: Int = 1000

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