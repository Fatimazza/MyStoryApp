package xyz.codingwithza.mystoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.data.remote.request.LoginRequest
import xyz.codingwithza.mystoryapp.data.remote.request.RegisterRequest
import xyz.codingwithza.mystoryapp.data.remote.response.ErrorResponse
import xyz.codingwithza.mystoryapp.data.remote.response.LoginResponse
import xyz.codingwithza.mystoryapp.data.remote.response.RegisterResponse
import xyz.codingwithza.mystoryapp.data.remote.retrofit.ApiService


class AuthRepository private constructor(
    private val apiService: ApiService
) {

    private val result = MediatorLiveData<Result<RegisterResponse>>()
    private val loginResult = MediatorLiveData<Result<LoginResponse>>()

    fun registerUser(registerRequest: RegisterRequest): LiveData<Result<RegisterResponse>> {
        result.value = Result.Loading
        val client = apiService.register(registerRequest.name, registerRequest.email, registerRequest.password)
        client
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            result.value = Result.Success(it)
                        }
                    } else {
                        val errorBody = Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ErrorResponse::class.java
                        )
                        result.value = Result.Error(errorBody.message.toString())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    result.value = Result.Error(t.message.toString())
                }
            })
        return result
    }

    fun loginUser(loginRequest: LoginRequest): LiveData<Result<LoginResponse>> {
        loginResult.value = Result.Loading
        val client = apiService.login(loginRequest.email, loginRequest.password)
        client
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        return loginResult
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
