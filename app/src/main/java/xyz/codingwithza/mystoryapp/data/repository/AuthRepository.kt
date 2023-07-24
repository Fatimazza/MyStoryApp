package xyz.codingwithza.mystoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import retrofit2.Callback
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.data.remote.request.RegisterRequest
import xyz.codingwithza.mystoryapp.data.remote.response.RegisterResponse
import xyz.codingwithza.mystoryapp.data.remote.retrofit.ApiService


class AuthRepository private constructor(
    private val apiService: ApiService
) {

    private val result = MediatorLiveData<Result<RegisterResponse>>()

    fun registerUser(registerRequest: RegisterRequest): LiveData<Result<RegisterResponse>> {
        result.value = Result.Loading
        val client = apiService.register(registerRequest.name, registerRequest.email, registerRequest.password)
        client
            .enqueue(object : Callback<RegisterResponse> {})
        return result
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
