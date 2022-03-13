package com.nanioi.covid19appproject2.Model.db

import android.content.Context
import androidx.room.Room

internal fun provideDB(context: Context): ClinicDatabase =
    Room.databaseBuilder(context, ClinicDatabase::class.java, ClinicDatabase.DB_NAME).build()

internal fun provideToDoDao(database: ClinicDatabase) = database.clinicDao()
