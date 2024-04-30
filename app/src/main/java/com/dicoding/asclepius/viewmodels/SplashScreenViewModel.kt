package com.dicoding.asclepius.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.asclepius.database.SettingPreferences

class SplashScreenViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getUsername(): LiveData<String> {
        return pref.getUsername().asLiveData()
    }
}