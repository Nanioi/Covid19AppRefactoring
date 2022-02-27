package com.nanioi.covid19appproject2.domain.status

import com.nanioi.covid19appproject2.BuildConfig
import com.nanioi.covid19appproject2.data.InfectionStatus
import com.nanioi.covid19appproject2.data.repository.StatusRepository
import com.nanioi.covid19appproject2.domain.UseCase
import java.util.*
import kotlin.collections.HashMap

internal class GetStatusListUseCase (
    private val statusRepository : StatusRepository
): UseCase{
    suspend operator fun invoke(param : HashMap<String,String>) : List<InfectionStatus>? {
        return statusRepository.getStatusList(param)
    }
}
