package com.nanioi.covid19appproject2.Model.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity

@Dao
interface ClinicLocationDao {

    @Query("SELECT * FROM ClinicLocationEntity")
    suspend fun getAllLocation() : List<ClinicLocationEntity>?

    @Query("SELECT * FROM ClinicLocationEntity WHERE city = :city and sigungu = :sigungu")
    suspend fun getLocationAround(city : String, sigungu:String) : List<ClinicLocationEntity>?

    @Insert
    suspend fun insert(itemEntity : ClinicLocationEntity)

}
