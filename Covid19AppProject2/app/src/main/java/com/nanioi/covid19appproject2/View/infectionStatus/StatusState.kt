package com.nanioi.covid19appproject2.View.infectionStatus

import com.nanioi.covid19appproject2.Model.data.InfectionStatus
import com.nanioi.covid19appproject2.data.StatusItem

sealed class StatusState {

    object UnInitialized: StatusState()

    object Loading: StatusState()

    data class Success(
        val statusList: List<InfectionStatus>,
        val newInfectionList : List<StatusItem>
    ): StatusState()

    object Error: StatusState()

}