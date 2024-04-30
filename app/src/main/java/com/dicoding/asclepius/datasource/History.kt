package com.dicoding.asclepius.datasource

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.database.HistoryDAO
import com.dicoding.asclepius.database.HistoryEntity
import com.dicoding.asclepius.database.HistoryRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class History(application: Application) {
    private val historyDao: HistoryDAO
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = HistoryRoomDatabase.getDatabase(application)
        historyDao = db.historyDao()
    }

    fun getAllHistory(): LiveData<List<HistoryEntity>> = historyDao.getAllHistory()
    fun getHistoryById(id: Int): LiveData<List<HistoryEntity>> =
        historyDao.getHistoryById(id)

    fun insert(fav: HistoryEntity) {
        executorService.execute { historyDao.insert(fav) }
    }

}