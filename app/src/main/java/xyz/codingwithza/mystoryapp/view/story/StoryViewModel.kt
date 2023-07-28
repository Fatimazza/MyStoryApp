package xyz.codingwithza.mystoryapp.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.codingwithza.mystoryapp.data.local.datastore.UserModel
import xyz.codingwithza.mystoryapp.data.repository.StoryRepository

class StoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getAllStories(token: String) =
        storyRepository.getAllStories(token)

    fun getUserData(): LiveData<UserModel> {
        return storyRepository.getUserData()
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }
}
