package com.nanioi.covid19appproject2.Model.apiService

import com.nanioi.covid19appproject2.data.InfectionStatusResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface InfectionStatusApiService {

    @GET("getCovid19InfStateJson")
    suspend fun getStatus(@QueryMap param: HashMap<String,String>): InfectionStatusResponse

}