package szulc.magdalena.fitpost.remote

import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Url
import szulc.magdalena.fitpost.remote.model.MyPlaces

interface IGoogleAPIService {
    @GET
    fun getNearbyPlaces(@Url url:String): Call<MyPlaces>
}