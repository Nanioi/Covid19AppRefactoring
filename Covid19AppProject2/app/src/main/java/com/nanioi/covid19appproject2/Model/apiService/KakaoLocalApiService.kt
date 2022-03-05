package com.nanioi.covid19appproject2.Model.apiService


import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.Model.data.regionInfo.RegionInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoLocalApiService {

    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_API_KEY}")
    @GET("/v2/local/geo/coord2regioncode.json")
    suspend fun getRegionInfo(
        @Query("x") longitude: Double,
        @Query("y") latitude: Double
    ): Response<RegionInfoResponse>
}
