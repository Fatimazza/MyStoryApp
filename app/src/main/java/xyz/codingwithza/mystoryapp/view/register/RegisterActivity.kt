package xyz.codingwithza.mystoryapp.view.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
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
        binding.btnRegisterSignup.setOnClickListener {
            registerUser(
                RegisterRequest(
                    binding.etRegisterName.text.toString(),
                    binding.etRegisterEmail.text.toString(),
                    binding.etRegisterPassword.text.toString()
                )
            )
        }
    }

    private fun registerUser(registerRequest: RegisterRequest) {

    }
}
