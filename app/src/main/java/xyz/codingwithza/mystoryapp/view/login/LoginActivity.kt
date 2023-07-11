package xyz.codingwithza.mystoryapp.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.codingwithza.mystoryapp.databinding.ActivityLoginBinding
import xyz.codingwithza.mystoryapp.util.Util

class LoginActivity : AppCompatActivity(), Util {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFullScreenView(window, supportActionBar)
    }
}
