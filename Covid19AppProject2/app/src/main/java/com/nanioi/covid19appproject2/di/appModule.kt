package com.nanioi.covid19appproject2.di

import android.preference.PreferenceManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.nanioi.covid19appproject2.Model.apiService.ClinicInfoApi
import com.nanioi.covid19appproject2.Model.apiService.ClinicInfoStorageApi
import com.nanioi.covid19appproject2.Model.db.provideDB
import com.nanioi.covid19appproject2.Model.db.provideToDoDao
import com.nanioi.covid19appproject2.Model.domain.GetAllDataUseCase
import com.nanioi.covid19appproject2.Model.domain.GetAroundDataUseCase
import com.nanioi.covid19appproject2.Model.domain.InsertDataUseCase
import com.nanioi.covid19appproject2.ViewModel.ClinicViewModel
import com.nanioi.covid19appproject2.repository.*
import com.nanioi.covid19appproject2.repository.provideProductApiService
import com.nanioi.covid19appproject2.repository.provideProductRetrofit
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {

    single { Dispatchers.Main }
    single { Dispatchers.IO }

    //ViewModel
    viewModel { ClinicViewModel(get(),get(),get()) }

    // UseCase
    factory { InsertDataUseCase(get()) }
    factory { GetAllDataUseCase(get()) }
    factory { GetAroundDataUseCase(get()) }

    // Repository
    single<ClinicInfoApi> { ClinicInfoStorageApi(Firebase.storage) }

    single { provideGsonConverterFactory() }

    single { buildOkHttpClient() }

    single { provideProductRetrofit(get(), get()) }

    single { provideProductApiService(get()) }

    single<ClinicRepository> { ClinicLocationRepository(get(), get(), get(),get()) }

    //single { PreferenceManager(androidContext()) }

    // Database
    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }

}

