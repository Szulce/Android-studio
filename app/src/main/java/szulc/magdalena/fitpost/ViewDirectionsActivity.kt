package szulc.magdalena.fitpost

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.Layout
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dmax.dialog.SpotsDialog
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import szulc.magdalena.fitpost.Helper.DirectionsJSONParser
import szulc.magdalena.fitpost.remote.IGoogleAPIService
import szulc.magdalena.fitpost.remote.common.Common
import szulc.magdalena.fitpost.ui.main.fragments.MapFragment
import java.lang.StringBuilder
import java.util.ArrayList
import javax.security.auth.callback.Callback

class ViewDirectionsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var mLastLocation: Location
    lateinit var mCurrentMarker: Marker
    lateinit var mService: IGoogleAPIService
     var polyline: Polyline?=null

    companion object {

        private val MY_PERMISSION_CODE: Int = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_directions)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //init service
        mService = Common.googleApiServiceScalars

        //runtime permision
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("MAP", "Build.VERSION.SDK_INT:" + Build.VERSION.SDK_INT)
            if (checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallBack()

                fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )
            }
        } else {
            Log.d("MAP", "NO PERMISSION")
            buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {location ->
            mLastLocation = location

            val markerOptions = MarkerOptions().position(
                LatLng(
                    mLastLocation.latitude,
                    mLastLocation.longitude
                )
            ).title(
                getString(
                    R.string.your_position
                )
            ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            mCurrentMarker = mMap!!.addMarker(markerOptions)
            mMap.moveCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        mLastLocation.latitude,
                        mLastLocation.longitude
                    )
                )
            )
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12f))
            //marker for destination
            val destinationLatlng = LatLng(
                Common.currentResult!!.geometry?.location!!.lat,
                Common.currentResult!!.geometry?.location!!.lng
            )
            mMap.addMarker(
                MarkerOptions().position(destinationLatlng).title(Common.currentResult!!.name).icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)
                )
            )

            //path
            drawPath(mLastLocation, Common.currentResult!!.geometry!!.location!!)

        }

    }

    private fun checkLocationPermission(): Boolean {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this as Activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this as Activity,
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

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f

    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    private fun buildLocationCallBack() {
        Log.d("MAP", "onLocationResut")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.lastLocation
                val markerOptions = MarkerOptions().position(
                    LatLng(
                        mLastLocation.latitude,
                        mLastLocation.longitude
                    )
                ).title(
                    getString(
                        R.string.your_position
                    )
                ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                mCurrentMarker = mMap!!.addMarker(markerOptions)
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLng(
                        LatLng(
                            mLastLocation.latitude,
                            mLastLocation.longitude
                        )
                    )
                )
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12f))
                //marker for destination
                val destinationLatlng = LatLng(
                    Common.currentResult!!.geometry?.location!!.lat,
                    Common.currentResult!!.geometry?.location!!.lng
                )
                mMap.addMarker(
                    MarkerOptions().position(destinationLatlng).title(Common.currentResult!!.name).icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)
                    )
                )

                //path
                drawPath(mLastLocation, Common.currentResult!!.geometry!!.location!!)


            }
        }
    }

    private fun drawPath(
        mLastLocation: Location?,
        location: szulc.magdalena.fitpost.remote.model.Location
    ) {
        if (polyline != null) {
            polyline!!.remove()
        }
        val origin = StringBuilder(mLastLocation!!.latitude.toString()).append(",")
            .append(mLastLocation.longitude.toString()).toString()
        val destination =
            StringBuilder(location.lat.toString()).append(",").append(location.lng.toString())
                .toString()

        mService.getDirections(origin, destination).enqueue(object : retrofit2.Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("MAP", ""+t.message)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("MAP", "response$response")
                ParseTask().execute(response.body()!!.toString())
            }

        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_CODE -> {
                Log.d("MAP", "onresult:" + requestCode)
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        if (checkLocationPermission()) {
                            buildLocationRequest()
                            buildLocationCallBack()

                            fusedLocationProviderClient =
                                LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.myLooper()
                            )
                            mMap.isMyLocationEnabled = true
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.permision_denied),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    inner class ParseTask : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {
        private val waitingDialog: AlertDialog = SpotsDialog(this@ViewDirectionsActivity)
        override fun doInBackground(vararg p0: String?): List<List<HashMap<String, String>>> {
            var jsonObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null
            try {
                jsonObject = JSONObject(p0[0])
                val parser = DirectionsJSONParser()
                routes = parser.parse(jsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return routes!!
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            super.onPostExecute(result)
            var points: ArrayList<LatLng>? = null
            var polylineOptions: PolylineOptions? = null

            Log.d("MAP","result:"+result!!.size+"result"+result!!)
            for (i in result!!.indices) {
                points = ArrayList()
                polylineOptions = PolylineOptions()
                val path = result[i]

                for (j in path.indices) {
                    points.add(LatLng(path[j]["lat"]!!.toDouble(), path[j]["lng"]!!.toDouble()))
                }

                polylineOptions.addAll(points)
                polylineOptions.width(12f)
                polylineOptions.color(Color.MAGENTA)
                polylineOptions.geodesic(true)

            }
            if(polylineOptions!=null){
            polyline = mMap.addPolyline(polylineOptions)
            waitingDialog.dismiss()
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            waitingDialog.show()
            waitingDialog.setMessage(getString(R.string.please_wait))
        }
    }

}


