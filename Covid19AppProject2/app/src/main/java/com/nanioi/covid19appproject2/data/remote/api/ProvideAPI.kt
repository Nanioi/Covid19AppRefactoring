package com.nanioi.covid19appproject2.data.remote.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.data.remote.service.InfectionStatusApiService
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

internal fun provideStatusApiService(retrofit: Retrofit): InfectionStatusApiService {
    return retrofit.create(InfectionStatusApiService::class.java)
}

internal fun provideStatusRetrofit(
    okHttpClient: OkHttpClient,
    tikXmlConverterFactory: TikXmlConverterFactory,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Url.STATUS_BASE_URL)
        .addConverterFactory(tikXmlConverterFactory)
        .client(okHttpClient)
        .build()
}

internal fun provideTikXmlConverterFactory(): TikXmlConverterFactory {
    val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()

    return TikXmlConverterFactory.create(parser)
}

internal fun buildOkHttpClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        interceptor.level = HttpLoggingInterceptor.Level.NONE
    }
    return OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build()
}
