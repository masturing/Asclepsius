package com.dicoding.asclepius.database

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUsername(): Flow<String> {
        return dataStore.data.map { pref ->
            pref[USER_NAME] ?: "Guest"
        }
    }

    suspend fun saveUsername(username: String) {
        dataStore.edit { pref ->
            pref[USER_NAME] = username
        }
    }

    companion object {
        private val USER_NAME = stringPreferencesKey("user_name")
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}