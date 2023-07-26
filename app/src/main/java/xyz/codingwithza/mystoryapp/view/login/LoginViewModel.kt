package xyz.codingwithza.mystoryapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import xyz.codingwithza.mystoryapp.data.local.datastore.UserModel
import xyz.codingwithza.mystoryapp.data.remote.request.LoginRequest
import xyz.codingwithza.mystoryapp.data.repository.AuthRepository

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun loginUser(loginRequest: LoginRequest) =
        authRepository.loginUser(loginRequest)

    fun getUserData(): LiveData<UserModel> {
        return authRepository.getUserData()
    }
}
