package com.nanioi.covid19appproject2.Model.apiService

import com.google.firebase.storage.FirebaseStorage
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity
import kotlinx.coroutines.tasks.await

internal class ClinicInfoStorageApi (
    firebaseStorage: FirebaseStorage
):ClinicInfoApi{
    private val sheetReference = firebaseStorage.reference.child(CLINIC_INFO_FILE_NAME)

    override suspend fun getClinicInfoAll(): List<ClinicLocationEntity> {
        val downloadSizeBytes = sheetReference.metadata.await().sizeBytes
        val byteArray = sheetReference.getBytes(downloadSizeBytes).await()

        return byteArray.decodeToString()
            .lines()
            .drop(1)
            .map { it.split(",") }
            .map { ClinicLocationEntity(it[0].toInt(),it[1],it[2],it[3],it[4],it[5],it[6],it[7],it[8],it[9]) }
    }

    companion object{
        private const val CLINIC_INFO_FILE_NAME = "clinic_info.csv"
    }
}