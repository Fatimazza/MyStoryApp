package xyz.codingwithza.mystoryapp.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import xyz.codingwithza.mystoryapp.data.local.datastore.UserModel
import xyz.codingwithza.mystoryapp.data.repository.StoryRepository

class MapsViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getAllStoriesByLocation() =
        storyRepository.getAllStoriesByLocation()

    fun getUserData(): LiveData<UserModel> {
        return storyRepository.getUserData()
    }
}
