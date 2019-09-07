package szulc.magdalena.fitpost.remote

import android.webkit.WebStorage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import szulc.magdalena.fitpost.remote.model.MyPlaces
import szulc.magdalena.fitpost.remote.model.PlaceDetail

interface IGoogleAPIService {
    @GET
    fun getNearbyPlaces(@Url url:String): Call<MyPlaces>

    @GET
    fun getDetailPlace(@Url url:String):Call<PlaceDetail>

    @GET("maps/api/directions/json")
    fun getDirections(@Query("origin") origin: String,@Query("destination") destination:String):Call<String>
}