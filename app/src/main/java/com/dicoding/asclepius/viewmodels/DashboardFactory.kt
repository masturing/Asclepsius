package com.dicoding.asclepius.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.database.SettingPreferences

class DashboardFactory(private val pref: SettingPreferences,private val app : Application) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(pref, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}