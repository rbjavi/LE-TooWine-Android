package com.jruizb.toowine.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


/**
 * TooWine
 *
 * Creado por Javier RB
 * A fecha 12/10/2021
 */
enum class PreferencesKey(val value: String) {
    ONBOARDING("onboarding")
}


object PreferencesProvider {

    fun set(context: Context, key: PreferencesKey, value: String) {
        val editor = prefs(context).edit()
        editor.putString(key.value, value).apply()
    }

    fun set(context: Context, key: PreferencesKey, value: Boolean) {
        val editor = prefs(context).edit()
        editor.putBoolean(key.value, value).apply()
    }

     fun saveState(context: Context, isFavourite: Boolean) {
        val aSharedPreferences: SharedPreferences = this.prefs(
            context
        )
        val aSharedPreferencesEdit = aSharedPreferences
            .edit()
        aSharedPreferencesEdit.putBoolean("State", isFavourite)
        aSharedPreferencesEdit.apply()
    }

     fun readState(context: Context,): Boolean {
        val aSharedPreferenes: SharedPreferences = this.prefs(
            context
        )
        return aSharedPreferenes.getBoolean("State", true)
    }

    fun bool(context: Context, key: PreferencesKey): Boolean? {
        return prefs(context).getBoolean(key.value, false)
    }


    private fun prefs(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}