package com.nanioi.covid19appproject2.data.repository

import com.nanioi.covid19appproject2.data.InfectionStatus
import com.nanioi.covid19appproject2.data.remote.service.InfectionStatusApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultStatusRepository(
    private val statusApi : InfectionStatusApiService,
    private val ioDispatcher: CoroutineDispatcher
) : StatusRepository {

    override suspend fun getStatusList(param: HashMap<String, String>): List<InfectionStatus> = withContext(ioDispatcher) {
        val response = statusApi.getStatus(param)

        return@withContext response ?: listOf()

    }


}