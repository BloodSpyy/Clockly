package com.bloodspy.clockly.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bloodspy.clockly.data.database.models.AlarmModel

@Database(entities = [AlarmModel::class], version = 1, exportSchema = false)
abstract class ClocklyDatabase: RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object {
        private var db: ClocklyDatabase? = null
        private const val DB_NAME = "clockyApp.db"
        private val LOCK = Any()

        fun getInstance(context: Context): ClocklyDatabase {
            synchronized(LOCK) {
                db?.let { return it }

                val instance = Room.databaseBuilder(
                    context,
                    ClocklyDatabase::class.java,
                    DB_NAME
                ).build()

                db = instance
                return instance
            }
        }
    }
}