package com.kavyakanaja.app.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension to create a single DataStore instance per Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Manages language selection persistence using DataStore.
 * Stores and retrieves user's preferred language ("en" or "kn").
 */
class LanguageManager(private val context: Context) {

    private val LANGUAGE_KEY = stringPreferencesKey(Constants.PREF_LANGUAGE)

    /** Flow that emits current language preference. Defaults to English. */
    val selectedLanguage: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[LANGUAGE_KEY] ?: Constants.LANG_ENGLISH }

    /** Persists language selection to DataStore */
    suspend fun setLanguage(language: String) {
        context.dataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = language
        }
    }
}