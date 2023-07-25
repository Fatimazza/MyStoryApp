package xyz.codingwithza.mystoryapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.codingwithza.mystoryapp.data.repository.AuthRepository
import xyz.codingwithza.mystoryapp.di.Injection
import xyz.codingwithza.mystoryapp.view.login.LoginViewModel
import xyz.codingwithza.mystoryapp.view.register.RegisterViewModel

class ViewModelFactory private constructor(private val authRepository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideAuthRepository())
            }.also { instance = it }
    }
}
