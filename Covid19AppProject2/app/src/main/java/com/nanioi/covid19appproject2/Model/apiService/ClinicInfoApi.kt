package com.nanioi.covid19appproject2.Model.apiService

import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity

interface ClinicInfoApi {
    suspend fun getClinicInfoAll():List<ClinicLocationEntity>
}