package xyz.codingwithza.mystoryapp.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import xyz.codingwithza.mystoryapp.R
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
        val etLoginPass = binding.etLoginPassword.text
        binding.btnLoginLogin.setOnClickListener {

            if (etLoginEmail.isNullOrEmpty()
                || etLoginPass.isNullOrEmpty()
            ) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.auth_blank),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else if (binding.etLoginEmail.error != null
                || binding.etLoginPassword.error != null) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.auth_error),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                loginUser(
                    LoginRequest(
                        etLoginEmail.toString(),
                        etLoginPass.toString()
                    )
                )
            }
        }
    }

    private fun loginUser(loginRequest: LoginRequest) {
        loginViewModel.loginUser(loginRequest).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                        Toast.makeText(
                            this,
                            "~ Loading ~",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Result.Success -> {
                        Toast.makeText(
                            this,
                            getString(R.string.login_success) + result.data.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        showLoading(false)
                    }
                    is Result.Error -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.login_error) + result.error,
                            Snackbar.LENGTH_SHORT
                        ).show()
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            etLoginEmail.isEnabled = !isLoading
            etLoginPassword.isEnabled = !isLoading

            if (isLoading) {
                btnLoginLogin.visibility = View.GONE
                pbLogin.visibility = View.VISIBLE
            } else {
                btnLoginLogin.visibility = View.VISIBLE
                pbLogin.visibility = View.GONE
            }
        }
    }

}
