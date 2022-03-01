package com.nanioi.covid19appproject2.ViewModel

import com.nanioi.covid19appproject2.Model.data.InfectionStatus

sealed class StatusState {

    object UnInitialized: StatusState()

    object Loading: StatusState()

    data class Success(
        val statusList: List<InfectionStatus>
    ): StatusState()

    object Error: StatusState()

}