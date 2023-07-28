package xyz.codingwithza.mystoryapp.view.addStory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.codingwithza.mystoryapp.databinding.ActivityAddStoryBinding

private lateinit var binding: ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
