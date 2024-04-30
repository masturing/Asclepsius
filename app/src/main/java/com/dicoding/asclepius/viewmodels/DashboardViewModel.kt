package com.dicoding.asclepius.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.asclepius.database.HistoryEntity
import com.dicoding.asclepius.database.SettingPreferences
import com.dicoding.asclepius.datasource.History

class DashboardViewModel(private val pref: SettingPreferences,app : Application) : ViewModel() {
    private val historyData : History = History(app)
    fun getUsername() : LiveData<String> {
        return pref.getUsername().asLiveData()
    }

    fun getHistoryList() : LiveData<List<HistoryEntity>> = historyData.getAllHistory()
}