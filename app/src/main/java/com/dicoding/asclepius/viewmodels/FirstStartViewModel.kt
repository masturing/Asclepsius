package com.dicoding.asclepius.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.database.SettingPreferences
import kotlinx.coroutines.launch

class FirstStartViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun setUsername(username: String) {
        viewModelScope.launch {
            pref.saveUsername(username)
        }
    }
}