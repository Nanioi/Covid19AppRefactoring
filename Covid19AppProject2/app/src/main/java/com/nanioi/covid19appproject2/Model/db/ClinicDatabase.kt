package com.nanioi.covid19appproject2.Model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nanioi.covid19appproject2.Model.db.dao.ClinicLocationDao
import com.nanioi.covid19appproject2.Model.entity.ClinicLocationEntity

@Database(
    entities = [ClinicLocationEntity::class],
    version = 1,
    exportSchema = false
)

abstract class ClinicDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ClinicDatabase.db"
    }

    abstract fun clinicDao(): ClinicLocationDao

}
