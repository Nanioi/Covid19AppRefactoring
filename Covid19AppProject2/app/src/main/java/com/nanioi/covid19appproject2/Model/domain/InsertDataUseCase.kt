package com.nanioi.covid19appproject2.Model.domain

import com.nanioi.covid19appproject2.repository.ClinicLocationRepository

internal class InsertDataUseCase(
    private val clinicLocationRepository: ClinicLocationRepository
) : UseCase {

    suspend operator fun invoke(){
        return clinicLocationRepository.insertClinicList()
    }

}