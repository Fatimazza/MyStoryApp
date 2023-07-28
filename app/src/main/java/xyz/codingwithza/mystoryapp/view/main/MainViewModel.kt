package xyz.codingwithza.mystoryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import xyz.codingwithza.mystoryapp.data.local.datastore.UserModel
import xyz.codingwithza.mystoryapp.data.repository.AuthRepository

class MainViewModel (
    private val authRepository: AuthRepository
) : ViewModel() {

    fun getUserData(): LiveData<UserModel> {
        return authRepository.getUserData()
    }
}
