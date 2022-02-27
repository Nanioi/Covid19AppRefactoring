package com.nanioi.covid19appproject2.data.remote.service

import com.nanioi.covid19appproject2.data.InfectionStatus
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface InfectionStatusApiService {

    @GET("getCovid19InfStateJson")
    suspend fun getStatus(@QueryMap param: HashMap<String,String>): List<InfectionStatus>?

}

