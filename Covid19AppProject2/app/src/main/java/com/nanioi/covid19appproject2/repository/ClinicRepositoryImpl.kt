package com.nanioi.covid19appproject2.repository

import com.nanioi.covid19appproject2.Model.apiService.ClinicInfoApi
import com.nanioi.covid19appproject2.Model.db.dao.ClinicLocationDao
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClinicRepositoryImpl(
    private val clinicInfoApi: ClinicInfoApi,
    private val clinicLocationDao: ClinicLocationDao,
    private val dispatcher: CoroutineDispatcher
) : ClinicRepository {

    override suspend fun getClinicLocationInfo() = withContext(dispatcher) {
        clinicLocationDao.insertClinicList(clinicInfoApi.getClinicInfoAll())
    }

    override suspend fun getClinicLocationAroundInfo(
        city: String,
        sigungu: String
    ): List<ClinicLocationEntity> = withContext(dispatcher) {
        return@withContext clinicLocationDao.getLocationAround(city,sigungu)?: listOf()
    }


}