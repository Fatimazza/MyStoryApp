package xyz.codingwithza.mystoryapp.view.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import xyz.codingwithza.mystoryapp.R
import xyz.codingwithza.mystoryapp.data.local.datastore.UserModel
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.data.remote.request.RegisterRequest
import xyz.codingwithza.mystoryapp.databinding.ActivityRegisterBinding
import xyz.codingwithza.mystoryapp.util.ViewUtil
import xyz.codingwithza.mystoryapp.view.ViewModelFactory
import xyz.codingwithza.mystoryapp.view.login.LoginActivity
import java.util.*
import kotlin.concurrent.schedule

class RegisterActivity : AppCompatActivity(), ViewUtil {

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
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
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
                Snackbar.make(
                    binding.root,
                    getString(R.string.auth_blank),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else if (binding.etRegisterPassword.error != null
                || binding.etRegisterEmail.error != null
            ) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.auth_error),
                    Snackbar.LENGTH_SHORT
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
                    }
                    is Result.Success -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.register_success) + result.data.message.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        showLoading(false)
                        saveUserData(
                            UserModel(
                                registerRequest.name,
                                registerRequest.email,
                                "",
                                false
                            )
                        )
                        Timer().schedule(2000) {
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finishAffinity()
                        }
                    }
                    is Result.Error -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.register_error) + result.error,
                            Snackbar.LENGTH_SHORT
                        ).show()
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun saveUserData(userData: UserModel) {
        registerViewModel.saveUserData(userData)
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
