package xyz.codingwithza.mystoryapp.di

import xyz.codingwithza.mystoryapp.data.repository.AuthRepository
import xyz.codingwithza.mystoryapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideUserRepository(): AuthRepository {
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(apiService)
    }
}
