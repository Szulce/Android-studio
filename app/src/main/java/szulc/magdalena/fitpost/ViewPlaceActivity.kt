package szulc.magdalena.fitpost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_place.*
import retrofit2.Call
import retrofit2.Response
import szulc.magdalena.fitpost.remote.IGoogleAPIService
import szulc.magdalena.fitpost.remote.common.Common
import szulc.magdalena.fitpost.remote.model.PlaceDetail

class ViewPlaceActivity : AppCompatActivity() {

    internal lateinit var mService: IGoogleAPIService
    var mPlace: PlaceDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_place)
        //init service
        mService = Common.googleApiService
        //clear testViews
        place_name.text = ""
        place_adress.text = ""
        open_hour.text = ""
        btn_show_map.setOnClickListener {
            //openMapIntent to view
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mPlace!!.result!!.url))
            startActivity(mapIntent)
        }
        //load photo
        if (Common.currentResult!!.photos != null && Common.currentResult!!.photos!!.isNotEmpty()) {
            Picasso.get()//.with(this)
                .load(getPhotoOfPlace(Common.currentResult!!.photos!![0].photo_reference!!, 1000))
                .into(photo)
        }
        //rating
        if (Common.currentResult!!.rating != null) {
            Log.d("MAP","rating:"+Common.currentResult!!.rating)
            rating_bar.rating = Common.currentResult!!.rating.toFloat()
        } else {
            rating_bar.visibility = View.GONE
        }

        //open hours
        if (Common.currentResult!!.operning_hours != null) {
            open_hour.text="Open now:"+ Common.currentResult!!.operning_hours!!.open_now
        }else
        {
            open_hour.visibility = View.GONE
        }

        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult!!.place_id!!)).enqueue(object :
            retrofit2.Callback<PlaceDetail>{
            override fun onFailure(call: Call<PlaceDetail>?, t: Throwable?) {
                Toast.makeText(baseContext,""+t!!.message,Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<PlaceDetail>, response: Response<PlaceDetail>) {
                Log.d("MAP"," Response successfull:"+response.body()!!.status+" "+response.body()!!.result)

                mPlace = response!!.body()
                place_adress.text = mPlace!!.result!!.formatted_address
                place_name.text = mPlace!!.result!!.name
            }

        })
    }

    private fun getPlaceDetailUrl(place_id: String): String {
        val url  = StringBuilder("https://maps.googleapis.com/maps/api/place/details/json")
        url.append("?place_id=$place_id")
        url.append("&key=AIzaSyBBpxR0F5EW3BQRz7fWJr5BlAx8a_2PhoY")
        Log.d("URL_DEBUG", url.toString())
        return url.toString()

    }

}


    private fun getPhotoOfPlace(photo_reference: String, maxWidth: Int):String {
        val url  = StringBuilder("https://maps.googleapis.com/maps/api/place/photo")
        url.append("?maxwidth=$maxWidth")
        url.append("&photoreference=$photo_reference")
        url.append("&key=AIzaSyBBpxR0F5EW3BQRz7fWJr5BlAx8a_2PhoY")
        Log.d("URL_DEBUG", url.toString())
        return url.toString()
    }

