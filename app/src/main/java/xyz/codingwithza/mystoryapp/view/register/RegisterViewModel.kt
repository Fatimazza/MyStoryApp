package xyz.codingwithza.mystoryapp.view.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.codingwithza.mystoryapp.data.local.datastore.UserModel
import xyz.codingwithza.mystoryapp.data.remote.request.RegisterRequest
import xyz.codingwithza.mystoryapp.data.repository.AuthRepository

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun registerUser(registerRequest: RegisterRequest) =
        authRepository.registerUser(registerRequest)

    fun saveUserData(userData: UserModel) {
        viewModelScope.launch {
            authRepository.saveUserData(userData)
        }
    }
}
