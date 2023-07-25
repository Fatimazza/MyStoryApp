package xyz.codingwithza.mystoryapp.view.register

import androidx.lifecycle.ViewModel
import xyz.codingwithza.mystoryapp.data.remote.request.RegisterRequest
import xyz.codingwithza.mystoryapp.data.repository.AuthRepository

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun registerUser(registerRequest: RegisterRequest) =
        authRepository.registerUser(registerRequest)
}
