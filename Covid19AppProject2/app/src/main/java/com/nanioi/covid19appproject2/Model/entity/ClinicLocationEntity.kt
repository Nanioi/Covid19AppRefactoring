package com.nanioi.covid19appproject2.Model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClinicLocationEntity(
    @PrimaryKey val id : Long?,
    val city: String?,
    val sigungu: String?,
    val clinic_name: String?,
    val address : String?,
    val weekday_operating_time : String?,
    val saturday_operating_time : String?,
    val holiday_operating_time : String?,
    val telephone_num: String
)

