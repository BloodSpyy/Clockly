package com.bloodspy.clockly.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bloodspy.clockly.data.database.models.AlarmModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarms ORDER BY alarmTime")
    fun getAlarmsFlow(): Flow<List<AlarmModel>>

    @Query("SELECT * FROM alarms WHERE isActive = 1")
    suspend fun getActivatedAlarms(): List<AlarmModel>?

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    suspend fun getAlarm(alarmId: Int): AlarmModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarm(alarm: AlarmModel): Long

    @Query("DELETE FROM alarms WHERE id = :alarmId")
    suspend fun deleteAlarm(alarmId: Int)
}