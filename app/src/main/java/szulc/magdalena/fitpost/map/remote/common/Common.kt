package szulc.magdalena.fitpost.map.remote.common

import szulc.magdalena.fitpost.map.remote.IGoogleAPIService
import szulc.magdalena.fitpost.map.remote.RetrofitClient
import szulc.magdalena.fitpost.map.remote.RetrofitScalarsClient
import szulc.magdalena.fitpost.map.remote.model.Results

object Common {

private const val GOOGLE_API_URL:String = "https://maps.googleapis.com/"
    var currentResult:Results?=null


    val googleApiService:IGoogleAPIService
    get()=RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)

    val googleApiServiceScalars:IGoogleAPIService
        get()=RetrofitScalarsClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)
}