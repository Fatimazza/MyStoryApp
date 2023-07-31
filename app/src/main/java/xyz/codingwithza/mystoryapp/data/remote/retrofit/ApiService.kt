package xyz.codingwithza.mystoryapp.data.remote.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import xyz.codingwithza.mystoryapp.data.remote.response.AddStoryResponse
import xyz.codingwithza.mystoryapp.data.remote.response.LoginResponse
import xyz.codingwithza.mystoryapp.data.remote.response.RegisterResponse
import xyz.codingwithza.mystoryapp.data.remote.response.StoryResponse

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
    suspend fun getAllStory(
        @Header("Authorization") token: String
    ): StoryResponse

    @GET("stories")
    fun getAllStoryByLocation(
        @Header("Authorization") token: String,
        @Query("location") location : Int = 1,
    ) : Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>
}
