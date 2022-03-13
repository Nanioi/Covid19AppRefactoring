package com.nanioi.covid19appproject2.Model.domain

import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import com.nanioi.covid19appproject2.repository.ClinicLocationRepository

internal class GetAllDataUseCase(
    private val clinicLocationRepository: ClinicLocationRepository
) : UseCase {

    suspend operator fun invoke():List<ClinicLocationEntity>?{
        return clinicLocationRepository.getClinicLocationInfo()
    }

}