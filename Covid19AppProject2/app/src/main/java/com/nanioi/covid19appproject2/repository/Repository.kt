package com.nanioi.covid19appproject2.repository

import android.util.Log
import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.Model.apiService.ClinicLocationApiService
import com.nanioi.covid19appproject2.Model.apiService.KakaoLocalApiService
import com.nanioi.covid19appproject2.Model.data.regionInfo.RegionInfo
import com.nanioi.covid19appproject2.Model.network.Url
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Repository{

    suspend fun getRegionInfo(latitude:Double,longitude:Double) : RegionInfo?{
        val regionInfo = kakaoLocalApiService
            .getRegionInfo(longitude,latitude)
            .body()
            ?.documents
            ?.firstOrNull()

        Log.d("regionInfo : ", regionInfo.toString())

        return regionInfo
    }

    private val kakaoLocalApiService: KakaoLocalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.KAKAO_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()
    }

    val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()

    val clinicLocationApiService : ClinicLocationApiService by lazy{
        Retrofit.Builder()
            .baseUrl(Url.CLINIC_BASE_URL)
            .addConverterFactory(TikXmlConverterFactory.create(parser))
            .client(buildHttpClient())
            .build()
            .create()
    }

    private fun buildHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if(BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
}