package xyz.codingwithza.mystoryapp.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.data.remote.request.LoginRequest
import xyz.codingwithza.mystoryapp.databinding.ActivityLoginBinding
import xyz.codingwithza.mystoryapp.util.Util
import xyz.codingwithza.mystoryapp.view.ViewModelFactory

class LoginActivity : AppCompatActivity(), Util {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFullScreenView(window, supportActionBar)
        initViewModel()
        setupButtonAction()
    }

    private fun initViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance()
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun setupButtonAction() {
        val etLoginEmail = binding.etLoginEmail.text
        val etLoginPass = binding.etLoginEmail.text
        binding.btnLoginLogin.setOnClickListener {
            loginUser(
                LoginRequest(
                    etLoginEmail.toString(),
                    etLoginPass.toString()
                )
            )
        }
    }

    private fun loginUser(loginRequest: LoginRequest) {
        loginViewModel.loginUser(loginRequest).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        Toast.makeText(
                            this,
                            "~ Loading ~",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Result.Success -> {
                        Toast.makeText(
                            this,
                            "Login SUCCESS " + result.data.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(
                            this,
                            "Login ERROR " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
