package com.nanioi.covid19appproject2.di

import com.nanioi.covid19appproject2.data.remote.api.buildOkHttpClient
import com.nanioi.covid19appproject2.data.remote.api.provideStatusApiService
import com.nanioi.covid19appproject2.data.remote.api.provideStatusRetrofit
import com.nanioi.covid19appproject2.data.remote.api.provideTikXmlConverterFactory
import com.nanioi.covid19appproject2.data.repository.DefaultStatusRepository
import com.nanioi.covid19appproject2.data.repository.StatusRepository
import com.nanioi.covid19appproject2.domain.status.GetStatusListUseCase
import com.nanioi.covid19appproject2.presentation.status.StatusViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.experimental.dsl.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    //UseCase
    factory { GetStatusListUseCase(get()) }

    //ViewModel
    viewModel { StatusViewModel(get()) }

    //Repository
    single<StatusRepository> { DefaultStatusRepository( get(), get()) }

    single { provideTikXmlConverterFactory()}

    single { buildOkHttpClient() }

    single { provideStatusRetrofit(get(), get()) }

    single { provideStatusApiService(get()) }
}
