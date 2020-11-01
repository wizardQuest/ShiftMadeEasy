package com.pq.shiftmadeeasy.settings

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

enum class UiMode {
    LIGHT, DARK
}

class SettingsManager @Inject constructor(context: Context) {

    private val dataStore = context.createDataStore(name = "settings_pref")

    val uiModeFlow: Flow<UiMode> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference ->
            val isDarkMode = preference[IS_DARK_MODE] ?: false

            when (isDarkMode) {
                true -> UiMode.DARK
                false -> UiMode.LIGHT
            }
        }

    val userName: Flow<String> = dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[USER_NAME] ?: ""
        }

    val userProfessionFlow: Flow<String> = dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[USER_PROFESSION] ?: ""
        }

    val userProfessionIconFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[USER_PROFESSION_ICON] ?: 0
        }

    suspend fun setUiMode(uiMode: UiMode) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = when (uiMode) {
                UiMode.LIGHT -> false
                UiMode.DARK -> true
            }
        }
    }

    suspend fun setUserProfession(userProfessionName: String, userProfessionIcon: Int) {
        dataStore.edit { preferences ->
            preferences[USER_PROFESSION] = userProfessionName
            preferences[USER_PROFESSION_ICON] = userProfessionIcon
        }
    }

    suspend fun setUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
    }

    companion object {
        val IS_DARK_MODE = preferencesKey<Boolean>("dark_mode")
        val USER_NAME = preferencesKey<String>("user_name")
        val USER_PROFESSION = preferencesKey<String>("user_profession")
        val USER_PROFESSION_ICON = preferencesKey<Int>("user_profession_icon")
    }
}