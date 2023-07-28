package xyz.codingwithza.mystoryapp.view.addStory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.codingwithza.mystoryapp.databinding.ActivityAddStoryBinding


class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonAction()
    }

    private fun setupButtonAction() {
        binding.btnAddGallery.setOnClickListener {

        }
    }
}
