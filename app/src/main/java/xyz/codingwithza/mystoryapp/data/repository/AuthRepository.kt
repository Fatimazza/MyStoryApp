package xyz.codingwithza.mystoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.codingwithza.mystoryapp.data.local.datastore.UserModel
import xyz.codingwithza.mystoryapp.data.local.datastore.UserPreferences
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.data.remote.request.LoginRequest
import xyz.codingwithza.mystoryapp.data.remote.request.RegisterRequest
import xyz.codingwithza.mystoryapp.data.remote.response.ErrorResponse
import xyz.codingwithza.mystoryapp.data.remote.response.LoginResponse
import xyz.codingwithza.mystoryapp.data.remote.response.RegisterResponse
import xyz.codingwithza.mystoryapp.data.remote.retrofit.ApiService


class AuthRepository private constructor(
    private val pref: UserPreferences,
    private val apiService: ApiService
) {

    private val registrationResult = MediatorLiveData<Result<RegisterResponse>>()
    private val loginResult = MediatorLiveData<Result<LoginResponse>>()

    suspend fun saveUserData(userEntity: UserModel) {
        pref.saveUserData(userEntity)
    }

    fun getUserData(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun registerUser(registerRequest: RegisterRequest): LiveData<Result<RegisterResponse>> {
        registrationResult.value = Result.Loading
        val client = apiService.register(registerRequest.name, registerRequest.email, registerRequest.password)
        client
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            registrationResult.value = Result.Success(it)
                        }
                    } else {
                        val errorBody = Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ErrorResponse::class.java
                        )
                        registrationResult.value = Result.Error(errorBody.message.toString())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    registrationResult.value = Result.Error(t.message.toString())
                }
            })
        return registrationResult
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
                    if (response.isSuccessful) {
                        response.body()?.let {
                            loginResult.value = Result.Success(it)
                        }
                    } else {
                        val errorBody = Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ErrorResponse::class.java
                        )
                        loginResult.value = Result.Error(errorBody.message.toString())
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginResult.value = Result.Error(t.message.toString())
                }
            })
        return loginResult
    }
    
    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(
            preferences: UserPreferences,
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(preferences, apiService)
            }.also { instance = it }
    }
}
