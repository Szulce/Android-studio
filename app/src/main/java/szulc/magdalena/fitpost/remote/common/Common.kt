package szulc.magdalena.fitpost.remote.common

import szulc.magdalena.fitpost.remote.IGoogleAPIService
import szulc.magdalena.fitpost.remote.RetrofitClient
import szulc.magdalena.fitpost.remote.RetrofitScalarsClient
import szulc.magdalena.fitpost.remote.model.Results

object Common {

private const val GOOGLE_API_URL:String = "https://maps.googleapis.com/"
    var currentResult:Results?=null


    val googleApiService:IGoogleAPIService
    get()=RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)

    val googleApiServiceScalars:IGoogleAPIService
        get()=RetrofitScalarsClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)
}