package xyz.codingwithza.mystoryapp.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.codingwithza.mystoryapp.data.StoryPagingSource
import xyz.codingwithza.mystoryapp.data.local.datastore.UserModel
import xyz.codingwithza.mystoryapp.data.local.datastore.UserPreferences
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.data.remote.response.AddStoryResponse
import xyz.codingwithza.mystoryapp.data.remote.response.ErrorResponse
import xyz.codingwithza.mystoryapp.data.remote.response.ListStoryItem
import xyz.codingwithza.mystoryapp.data.remote.response.StoryResponse
import xyz.codingwithza.mystoryapp.data.remote.retrofit.ApiService

class StoryRepository private constructor(
    private val pref: UserPreferences,
    private val apiService: ApiService
) {

    private val addStoryResult = MediatorLiveData<Result<String>>()

    fun getUserData(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    suspend fun logout() {
        pref.logout()
    }

    fun getAllStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    fun getAllStoriesByLocation(token: String): LiveData<Result<StoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getAllStoryByLocation("Bearer $token", 1)
                emit(Result.Success(response))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun uploadStory(imageMultipart: MultipartBody.Part,
                    description: RequestBody) : MediatorLiveData<Result<String>> {
        addStoryResult.value = Result.Loading
        val client = apiService.uploadImage(imageMultipart, description)
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        addStoryResult.value = Result.Success(it.message.toString())
                    }
                } else {
                    val errorBody = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        ErrorResponse::class.java
                    )
                    addStoryResult.value = Result.Error(errorBody.message.toString())
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                addStoryResult.value = Result.Error(t.message.toString())
            }
        })
        return addStoryResult
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
