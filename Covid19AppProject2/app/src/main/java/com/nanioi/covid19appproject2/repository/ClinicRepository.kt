package com.nanioi.covid19appproject2.repository

import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity

interface ClinicRepository {

    suspend fun insertClinicList()

    suspend fun getClinicLocationInfo():List<ClinicLocationEntity>?

    suspend fun getClinicLocationAroundInfo(x:Double,y:Double):List<ClinicLocationEntity>?
}