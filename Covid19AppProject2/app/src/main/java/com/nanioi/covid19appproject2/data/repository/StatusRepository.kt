package com.nanioi.covid19appproject2.data.repository

import com.nanioi.covid19appproject2.data.InfectionStatus

interface StatusRepository {
    suspend fun getStatusList(param:HashMap<String,String>) : List<InfectionStatus>
}