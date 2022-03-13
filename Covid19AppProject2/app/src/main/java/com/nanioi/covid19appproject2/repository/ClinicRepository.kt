package com.nanioi.covid19appproject2.repository

import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity

interface ClinicRepository {

    suspend fun getClinicLocationInfo()

    suspend fun getClinicLocationAroundInfo(city:String,sigungu:String):List<ClinicLocationEntity>
}