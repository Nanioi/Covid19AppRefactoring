package com.nanioi.covid19appproject2.Model.domain

import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import com.nanioi.covid19appproject2.repository.ClinicLocationRepository

internal class GetAroundDataUseCase(
    private val clinicLocationRepository: ClinicLocationRepository
) : UseCase {

    suspend operator fun invoke(x:Double,y:Double):List<ClinicLocationEntity>?{
        return clinicLocationRepository.getClinicLocationAroundInfo(x,y)
    }

}