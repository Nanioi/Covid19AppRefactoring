package com.nanioi.covid19appproject2.Model.network

import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.Model.apiService.InfectionStatusApiService
import com.nanioi.covid19appproject2.Model.network.Url.Infection_Status_URL
import com.nanioi.covid19appproject2.Model.network.Url.STATUS_BASE_URL
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object StatusRetrofitClient {

    val client = buildHttpClient()
    val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()

    private val statusRetrofit = Retrofit.Builder()
        .baseUrl(STATUS_BASE_URL)
        .addConverterFactory(TikXmlConverterFactory.create(parser))
        .client(client)
        .build()

    val infectionStatusApiService : InfectionStatusApiService = statusRetrofit.create(InfectionStatusApiService::class.java)

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