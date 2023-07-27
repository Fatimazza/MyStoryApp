package xyz.codingwithza.mystoryapp.data.repository


import androidx.lifecycle.MediatorLiveData
import retrofit2.Callback
import xyz.codingwithza.mystoryapp.data.local.StoryResponse
import xyz.codingwithza.mystoryapp.data.remote.retrofit.ApiService

class StoryRepository private constructor(
    private val apiService: ApiService
){

    private val storyResult = MediatorLiveData<Result<StoryResponse>>()

    fun getAllStories(token: String) {
        val client = apiService.getAllStory(token)
        client
            .enqueue(object : Callback<StoryResponse> {})
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
