package xyz.codingwithza.mystoryapp.view.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.codingwithza.mystoryapp.databinding.ActivityStoryBinding
import xyz.codingwithza.mystoryapp.view.ViewModelFactory

private lateinit var binding: ActivityStoryBinding
private lateinit var storyViewModel: StoryViewModel

class StoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        initViewModel()
    }

    private fun setupView() {
        val storyAdapter = StoryAdapter()
        binding.apply {
            rvStory.adapter = storyAdapter
            rvStory.layoutManager = LinearLayoutManager(this@StoryActivity)
            rvStory.setHasFixedSize(true)
        }
    }

    private fun initViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        storyViewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                rvStory.visibility = View.GONE
                pbStory.visibility = View.VISIBLE
            } else {
                rvStory.visibility = View.VISIBLE
                pbStory.visibility = View.GONE
            }
        }
    }
}
