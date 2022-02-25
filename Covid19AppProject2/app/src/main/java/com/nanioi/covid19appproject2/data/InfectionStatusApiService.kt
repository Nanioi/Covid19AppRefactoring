package com.nanioi.covid19appproject2.data

import com.nanioi.covid19appproject2.BuildConfig
import retrofit2.http.GET

interface InfectionStatusApiService {

    @GET("?serviceKey=Tdo19TVJxANWay1HQ1dxcwGA5sJ73wYF%2FVfvQaLyA1iBPWkttg74N9jzEUDGlG6J3ItutuWKuzOutjEblPuQIg%3D%3D"
    + "&pageNo=1" + "&numOfRows=10&startCreateDt=20220101&endCreateDt=20220225")
    suspend fun getStatus():InfectionStatus

}