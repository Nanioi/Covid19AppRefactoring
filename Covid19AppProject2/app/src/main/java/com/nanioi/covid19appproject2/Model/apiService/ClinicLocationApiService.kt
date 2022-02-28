package com.nanioi.covid19appproject2.Model.apiService

import com.nanioi.covid19appproject2.data.ClinicResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ClinicLocationApiService {

    @GET("rprtHospService/getRprtHospService")
    suspend fun getStatus(@QueryMap param: HashMap<String,String>): ClinicResponse

}