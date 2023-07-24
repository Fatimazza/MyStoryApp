package xyz.codingwithza.mystoryapp.view

import androidx.lifecycle.ViewModelProvider
import xyz.codingwithza.mystoryapp.data.repository.AuthRepository
import xyz.codingwithza.mystoryapp.di.Injection

class ViewModelFactory private constructor(private val authRepository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideAuthRepository())
            }.also { instance = it }
    }
}
