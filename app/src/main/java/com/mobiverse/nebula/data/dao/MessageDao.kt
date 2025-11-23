package com.mobiverse.nebula.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobiverse.nebula.data.entity.SatelliteMessageEntity

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: SatelliteMessageEntity)

    @Query("SELECT * FROM satellite_messages WHERE id = :id")
    suspend fun get(id: String): SatelliteMessageEntity?

    @Query("SELECT * FROM satellite_messages WHERE status = 'PENDING' ORDER BY timestamp ASC")
    suspend fun getPendingMessages(): List<SatelliteMessageEntity>

    @Query("DELETE FROM satellite_messages WHERE id = :id")
    suspend fun delete(id: String)
}
