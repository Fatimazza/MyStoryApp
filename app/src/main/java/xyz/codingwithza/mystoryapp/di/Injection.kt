package xyz.codingwithza.mystoryapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import xyz.codingwithza.mystoryapp.data.local.datastore.UserPreferences
import xyz.codingwithza.mystoryapp.data.repository.AuthRepository
import xyz.codingwithza.mystoryapp.data.remote.retrofit.ApiConfig
import xyz.codingwithza.mystoryapp.data.repository.StoryRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userPref")

object Injection {

    fun provideAuthRepository(context: Context): AuthRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(preferences, apiService)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }
}
