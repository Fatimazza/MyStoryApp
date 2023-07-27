package xyz.codingwithza.mystoryapp.data.remote.retrofit

import retrofit2.Call
import retrofit2.http.*
import xyz.codingwithza.mystoryapp.data.local.StoryResponse
import xyz.codingwithza.mystoryapp.data.remote.response.LoginResponse
import xyz.codingwithza.mystoryapp.data.remote.response.RegisterResponse

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") token: String
    ): Call<StoryResponse>
}
