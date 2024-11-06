package com.bloodspy.clockly.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bloodspy.clockly.data.database.models.AlarmModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    //todo сделай здесь возможность null
    @Query("SELECT * FROM alarms ORDER BY alarmTime")
    fun getAlarms(): Flow<List<AlarmModel>>

    //todo сделай здесь возможность null
    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    suspend fun getAlarm(alarmId: Int): AlarmModel

    @Query("SELECT MIN(alarmTime) FROM alarms")
    suspend fun getNearestAlarmTime(): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAlarm(alarm: AlarmModel): Long

    @Query("DELETE FROM alarms WHERE id = :alarmId")
    suspend fun deleteAlarm(alarmId: Int)
}