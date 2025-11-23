package com.mobiverse.nebula.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobiverse.nebula.data.dao.MessageDao
import com.mobiverse.nebula.data.entity.SatelliteMessageEntity

@Database(entities = [SatelliteMessageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nebula_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
