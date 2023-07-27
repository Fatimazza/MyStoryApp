package xyz.codingwithza.mystoryapp.data.repository


import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.codingwithza.mystoryapp.data.local.StoryResponse
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.data.remote.response.ErrorResponse
import xyz.codingwithza.mystoryapp.data.remote.retrofit.ApiService

class StoryRepository private constructor(
    private val apiService: ApiService
) {

    private val storyResult = MediatorLiveData<Result<StoryResponse>>()

    fun getAllStories(token: String) {
        val client = apiService.getAllStory(token)
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
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}
