package xyz.codingwithza.mystoryapp.view.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.codingwithza.mystoryapp.databinding.ActivityRegisterBinding
import xyz.codingwithza.mystoryapp.util.Util

class RegisterActivity : AppCompatActivity(), Util {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFullScreenView(window, supportActionBar)
    }
}
