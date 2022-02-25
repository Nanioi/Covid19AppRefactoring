package com.nanioi.covid19appproject2.data.network

import android.util.Log
import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.data.InfectionStatus
import com.nanioi.covid19appproject2.data.InfectionStatusApiService
import com.nanioi.covid19appproject2.data.network.Url.STATUS_BASE_URL
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create

object RetrofitClient {

    suspend fun getStatus() {
        val client = buildHttpClient()
        val retrofit = Retrofit.Builder()
            .baseUrl(STATUS_BASE_URL)
            .addConverterFactory(TikXmlConverterFactory.create(TikXml.Builder().exceptionOnUnreadXml(false).build()))
            .client(buildHttpClient())
            .build()
        val api : InfectionStatusApiService = retrofit.create()
        val data = api.getStatus()
        Log.d("Sample : ", data.toString())
    }
//    private val statusApi: InfectionStatusApiService by lazy {
//        Retrofit.Builder()
//            .baseUrl(STATUS_BASE_URL)
//            .addConverterFactory(TikXmlConverterFactory.create(TikXml.Builder().exceptionOnUnreadXml(false).build()))
//            .client(buildHttpClient())
//            .build()
//            .create()
//    }

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