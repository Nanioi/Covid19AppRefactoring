package com.nanioi.covid19appproject2.Model.db.dao

import androidx.room.*
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity

@Dao
interface ClinicLocationDao {

    @Transaction
    @Query("SELECT * FROM ClinicLocationEntity")
    suspend fun getAllLocation() : List<ClinicLocationEntity>?

    @Query("SELECT * FROM ClinicLocationEntity WHERE city = :city and sigungu = :sigungu")
    suspend fun getLocationAround(city : String, sigungu:String) : List<ClinicLocationEntity>?

    @Insert
    suspend fun insert(itemEntity : ClinicLocationEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClinicList(clinicListEntity: List<ClinicLocationEntity>){
        clinicListEntity.map { item->
            ClinicLocationEntity(item.id,item.city,item.sigungu,item.clinic_name,item.address
                ,item.weekday_operating_time,item.saturday_operating_time,item.sunday_operating_time,
                item.holiday_operating_time,item.telephone_num)
        }
    }

}
