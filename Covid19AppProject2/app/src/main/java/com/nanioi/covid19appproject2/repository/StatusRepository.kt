package com.nanioi.covid19appproject2.repository

import com.nanioi.covid19appproject2.Model.data.DataSource
import com.nanioi.covid19appproject2.Model.data.InfectionStatus
import com.nanioi.covid19appproject2.Model.network.JSoupParsingTask
import com.nanioi.covid19appproject2.Model.network.RemoteStatusData

class StatusRepository ( private val remoteStatusData : RemoteStatusData):DataSource{
    override fun getStatusData(): List<InfectionStatus>? {
        return remoteStatusData.getStatusData()
    }

}