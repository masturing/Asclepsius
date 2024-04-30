package com.dicoding.asclepius.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: HistoryEntity)

    @Query("SELECT  * from history")
    fun getAllHistory(): LiveData<List<HistoryEntity>>

    @Query("SELECT  * from history WHERE id = :id")
    fun getHistoryById(id: Int): LiveData<List<HistoryEntity>>
}