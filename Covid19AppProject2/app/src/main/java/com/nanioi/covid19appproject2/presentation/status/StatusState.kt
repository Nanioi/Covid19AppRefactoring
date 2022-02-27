package com.nanioi.covid19appproject2.presentation.status

import com.nanioi.covid19appproject2.data.InfectionStatus

sealed class StatusState {

    object UnInitialized: StatusState()

    object Loading: StatusState()

    data class Success(
        val statusList: List<InfectionStatus>
    ): StatusState()

    object Error: StatusState()

}
