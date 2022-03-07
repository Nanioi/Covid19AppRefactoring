package com.nanioi.covid19appproject2.Model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    abstract fun clinicDao(): ClinicLocationDao

    companion object {
        const val DB_NAME = "ClinicDatabase.db"

        fun build(context:Context): ClinicDatabase =
            Room.databaseBuilder(context,ClinicDatabase::class.java, DB_NAME).build()
    }
}

// Repository를 쓰기 위해 Dao, Entity 정의
// Dao, Storage 접근할 수 있는 것, manager 필요
