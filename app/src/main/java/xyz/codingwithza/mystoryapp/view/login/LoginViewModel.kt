package xyz.codingwithza.mystoryapp.view.login

import androidx.lifecycle.ViewModel
import xyz.codingwithza.mystoryapp.data.remote.request.LoginRequest
import xyz.codingwithza.mystoryapp.data.repository.AuthRepository

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun loginUser(loginRequest: LoginRequest) =
        authRepository.loginUser(loginRequest)
}
