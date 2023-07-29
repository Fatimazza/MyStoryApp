package xyz.codingwithza.mystoryapp.data.remote.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import xyz.codingwithza.mystoryapp.data.local.StoryResponse
import xyz.codingwithza.mystoryapp.data.remote.response.AddStoryResponse
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

    @Multipart
    @POST("/v1/stories/guest")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>
}
