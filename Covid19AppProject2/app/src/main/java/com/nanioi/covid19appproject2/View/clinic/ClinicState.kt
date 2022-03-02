package com.nanioi.covid19appproject2.View.clinic

import com.nanioi.covid19appproject2.Model.data.InfectionStatus

sealed class ClinicState {

    object UnInitialized: ClinicState()

    object Loading: ClinicState()

    data class Success(
        val clinicList: List<InfectionStatus>
    ): ClinicState()

    object Error: ClinicState()
}