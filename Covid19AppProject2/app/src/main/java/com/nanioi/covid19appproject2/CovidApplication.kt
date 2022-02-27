package com.nanioi.covid19appproject2

import android.app.Application
import com.nanioi.covid19appproject2.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CovidApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger(Level.ERROR)
            androidContext(this@CovidApplication)
            modules(appModule)
        }
    }
}