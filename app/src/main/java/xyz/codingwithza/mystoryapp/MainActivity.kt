package xyz.codingwithza.mystoryapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import xyz.codingwithza.mystoryapp.databinding.ActivityMainBinding
import xyz.codingwithza.mystoryapp.util.Util
import xyz.codingwithza.mystoryapp.view.login.LoginActivity
import xyz.codingwithza.mystoryapp.view.register.RegisterActivity

class MainActivity : AppCompatActivity(), Util {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        setupFullScreenView(window, supportActionBar)
    }

    private fun setupAction() {
        binding.welcomeLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.welcomeSignupButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
