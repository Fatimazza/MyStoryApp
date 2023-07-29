package xyz.codingwithza.mystoryapp.view.addStory

import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import xyz.codingwithza.mystoryapp.data.repository.StoryRepository

class AddStoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun uploadStory(token: String, imageMultipart: MultipartBody.Part, description: RequestBody) {
        storyRepository.uploadStory(token, imageMultipart, description)
    }
}
