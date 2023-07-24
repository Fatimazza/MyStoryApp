package xyz.codingwithza.mystoryapp.data.repository

import xyz.codingwithza.mystoryapp.data.remote.retrofit.ApiService

class AuthRepository private constructor(
    private val apiService: ApiService
) {

    fun registerUser() {

    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService)
            }.also { instance = it }
    }
}
