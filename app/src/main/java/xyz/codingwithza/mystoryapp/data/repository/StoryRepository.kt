package xyz.codingwithza.mystoryapp.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.codingwithza.mystoryapp.data.local.StoryResponse
import xyz.codingwithza.mystoryapp.data.local.datastore.UserModel
import xyz.codingwithza.mystoryapp.data.local.datastore.UserPreferences
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.data.remote.response.ErrorResponse
import xyz.codingwithza.mystoryapp.data.remote.retrofit.ApiService

class StoryRepository private constructor(
    private val pref: UserPreferences,
    private val apiService: ApiService
) {

    private val storyResult = MediatorLiveData<Result<StoryResponse>>()

    fun getUserData(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getAllStories(token: String): LiveData<Result<StoryResponse>> {
        storyResult.value = Result.Loading
        val client = apiService.getAllStory("Bearer $token")
        client
            .enqueue(object : Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            storyResult.value = Result.Success(it)
                        }
                    } else {
                        val errorBody = Gson().fromJson(
                            response.errorBody()?.charStream(),
                            ErrorResponse::class.java
                        )
                        storyResult.value = Result.Error(errorBody.message.toString())
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    storyResult.value = Result.Error(t.message.toString())
                }
            })
        return storyResult
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            preferences: UserPreferences,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(preferences, apiService)
            }.also { instance = it }
    }
}
