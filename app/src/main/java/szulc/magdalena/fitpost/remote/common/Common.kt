package szulc.magdalena.fitpost.remote.common

import retrofit2.Retrofit
import szulc.magdalena.fitpost.remote.IGoogleAPIService
import szulc.magdalena.fitpost.remote.RetrofitClient

object Common {

private val GOOGLE_API_URL:String = "https://maps.googleapis.com/"

    val googleApiService:IGoogleAPIService
    get()=RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)
}