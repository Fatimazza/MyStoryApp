package xyz.codingwithza.mystoryapp.view.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.data.remote.request.RegisterRequest
import xyz.codingwithza.mystoryapp.databinding.ActivityRegisterBinding
import xyz.codingwithza.mystoryapp.util.Util
import xyz.codingwithza.mystoryapp.view.ViewModelFactory

class RegisterActivity : AppCompatActivity(), Util {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupFullScreenView(window, supportActionBar)
        initViewModel()
        setupButtonAction()
    }

    private fun initViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance()
        registerViewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }

    private fun setupButtonAction() {
        val etRegName = binding.etRegisterName.text
        val etRegEmail = binding.etRegisterEmail.text
        val etRegPass = binding.etRegisterPassword.text
        binding.btnRegisterSignup.setOnClickListener {
            if (etRegName.isNullOrEmpty()
                || etRegEmail.isNullOrEmpty()
                || etRegPass.isNullOrEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Name, Email, and Password cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.etRegisterPassword.error != null
                || binding.etRegisterEmail.error != null
            ) {
                Toast.makeText(
                    this,
                    "One or more input is not valid",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                registerUser(
                    RegisterRequest(
                        etRegName.toString(),
                        etRegEmail.toString(),
                        etRegPass.toString()
                    )
                )
            }
        }
    }

    private fun registerUser(registerRequest: RegisterRequest) {
        registerViewModel.registerUser(registerRequest).observe(this) { result ->
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
                        showLoading(false)
                        Toast.makeText(
                            this,
                            "Regis SUCCESS " + result.data.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            this,
                            "Regis ERROR " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            etRegisterName.isEnabled = !isLoading
            etRegisterEmail.isEnabled = !isLoading
            etRegisterPassword.isEnabled = !isLoading

            if (isLoading) {
                btnRegisterSignup.visibility = View.GONE
                pbRegister.visibility = View.VISIBLE
            } else {
                btnRegisterSignup.visibility = View.VISIBLE
                pbRegister.visibility = View.GONE
            }
        }
    }
}
