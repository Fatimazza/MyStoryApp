package xyz.codingwithza.mystoryapp.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import xyz.codingwithza.mystoryapp.databinding.ActivityMainBinding
import xyz.codingwithza.mystoryapp.util.ViewUtil
import xyz.codingwithza.mystoryapp.view.ViewModelFactory
import xyz.codingwithza.mystoryapp.view.login.LoginActivity
import xyz.codingwithza.mystoryapp.view.register.RegisterActivity
import xyz.codingwithza.mystoryapp.view.story.StoryActivity

class MainActivity : AppCompatActivity(), ViewUtil {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        getUserFromPreferences()
        setupAction()
        setupFullScreenView(window, supportActionBar)
        playAnimation()
    }

    private fun initViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun getUserFromPreferences() {
        mainViewModel.getUserData().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, StoryActivity::class.java))
                finishAffinity()
            }
        }
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.ivWelcomePhoto, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            startDelay = 500
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvWelcomeTitle, View.ALPHA, 1f).apply {
            duration = 500
            startDelay = 500
        }
        val login = ObjectAnimator.ofFloat(binding.btnWelcomeLogin, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.btnWelcomeSignup, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }
        AnimatorSet().apply {
            playSequentially(title, together)
            start()
        }
    }

    private fun setupAction() {
        binding.btnWelcomeLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnWelcomeSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
