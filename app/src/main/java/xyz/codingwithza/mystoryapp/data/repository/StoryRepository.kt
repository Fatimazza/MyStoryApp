package xyz.codingwithza.mystoryapp.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.codingwithza.mystoryapp.data.local.StoryResponse
import xyz.codingwithza.mystoryapp.data.local.datastore.UserModel
import xyz.codingwithza.mystoryapp.data.local.datastore.UserPreferences
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.data.remote.response.AddStoryResponse
import xyz.codingwithza.mystoryapp.data.remote.response.ErrorResponse
import xyz.codingwithza.mystoryapp.data.remote.retrofit.ApiService

class StoryRepository private constructor(
    private val pref: UserPreferences,
    private val apiService: ApiService
) {

    private val storyResult = MediatorLiveData<Result<StoryResponse>>()
    private val addstoryResult = MediatorLiveData<Result<AddStoryResponse>>()

    fun getUserData(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    suspend fun logout() {
        pref.logout()
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

    fun uploadStory(token: String,
                    imageMultipart: MultipartBody.Part,
                    description: RequestBody
    ) : LiveData<Result<AddStoryResponse>> {
        addstoryResult.value = Result.Loading
        
        return addstoryResult
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
