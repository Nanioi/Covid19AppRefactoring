package com.nanioi.covid19appproject2.repository

import android.util.Log
import com.nanioi.covid19appproject2.Model.apiService.ClinicInfoApi
import com.nanioi.covid19appproject2.Model.apiService.KakaoLocalApiService
import com.nanioi.covid19appproject2.Model.db.dao.ClinicLocationDao
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ClinicLocationRepository(
    private val kakaoApiService : KakaoLocalApiService,
    private val clinicLocationDao : ClinicLocationDao,
    private val clinicInfoApi: ClinicInfoApi,
    private val ioDispatcher: CoroutineDispatcher
) : ClinicRepository {

    override suspend fun insertClinicList() = withContext(ioDispatcher){
        clinicLocationDao.insertClinicList(clinicInfoApi.getClinicInfoAll())
    }

    override suspend fun getClinicLocationInfo() : List<ClinicLocationEntity>? = withContext(ioDispatcher){
        return@withContext clinicLocationDao.getAllLocation()
    }

    override suspend fun getClinicLocationAroundInfo(
        x:Double,
        y:Double
    ): List<ClinicLocationEntity>? = withContext(ioDispatcher) {
        val response = kakaoApiService.getRegionInfo(x,y)
        return@withContext if (response.isSuccessful) {
            val region = response.body()?.documents?.firstOrNull()

            val city = region?.region1depthName!!
            var sigungu = region?.region2depthName!!.replace(" ","*")

            if("*" in sigungu){
                sigungu = sigungu.split("*")[0]
            }

            Log.d("Clinic : ", " 시: ${region.region1depthName} , 군 : ${region.region2depthName}")
            clinicLocationDao.getLocationAround(city,sigungu)
        }else{
            Log.d("Clinic : ", " 실패 ")
            clinicLocationDao.getLocationAround("","")
        }
    }

}