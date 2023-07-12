package xyz.codingwithza.mystoryapp.data.remote.retrofit

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import xyz.codingwithza.mystoryapp.data.remote.response.RegisterResponse

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse
}
