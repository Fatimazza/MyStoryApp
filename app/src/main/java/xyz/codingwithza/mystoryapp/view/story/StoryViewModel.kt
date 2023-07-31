package xyz.codingwithza.mystoryapp.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.launch
import xyz.codingwithza.mystoryapp.data.local.datastore.UserModel
import xyz.codingwithza.mystoryapp.data.remote.response.ListStoryItem
import xyz.codingwithza.mystoryapp.data.repository.StoryRepository

class StoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getAllStories(): LiveData<PagingData<ListStoryItem>> =
        storyRepository.getAllStories().cachedIn(viewModelScope)

    fun getUserData(): LiveData<UserModel> {
        return storyRepository.getUserData()
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }
}
