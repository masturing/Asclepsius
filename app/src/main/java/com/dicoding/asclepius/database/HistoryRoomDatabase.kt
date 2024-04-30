package com.dicoding.asclepius.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [HistoryEntity::class], version = 1)
abstract class HistoryRoomDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDAO

    companion object {
        @Volatile
        private var INSTANCE: HistoryRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): HistoryRoomDatabase {
            if (INSTANCE == null) {
                synchronized(HistoryRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryRoomDatabase::class.java, "history_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as HistoryRoomDatabase
        }
    }

}