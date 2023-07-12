package xyz.codingwithza.mystoryapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import xyz.codingwithza.mystoryapp.BuildConfig

class ApiConfig {
    fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}
