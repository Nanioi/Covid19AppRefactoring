package com.nanioi.covid19appproject2.Model.data

interface DataSource {
    fun getStatusData():List<InfectionStatus>?
}