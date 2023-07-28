package xyz.codingwithza.mystoryapp.view.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.codingwithza.mystoryapp.data.local.ListStoryItem
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.databinding.ActivityStoryBinding
import xyz.codingwithza.mystoryapp.view.ViewModelFactory
import xyz.codingwithza.mystoryapp.view.storydetail.StoryDetailActivity


private lateinit var binding: ActivityStoryBinding
private lateinit var storyViewModel: StoryViewModel
private lateinit var storyAdapter: StoryAdapter

class StoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        initViewModel()
        showAllStories()
        addListClickListener()
    }

    private fun setupView() {
        storyAdapter = StoryAdapter()
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

    private fun showAllStories() {
        storyViewModel.getUserData().observe(this) { user ->
            storyViewModel.getAllStories(user.token).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            storyAdapter.setData(result.data.listStory as List<ListStoryItem>)
                            showLoading(false)
                        }
                        is Result.Error -> {
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun addListClickListener() {
        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                val moveIntentWithBundle = Intent(this@StoryActivity, StoryDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(StoryDetailActivity.EXTRA_STORY, data)
                moveIntentWithBundle.putExtras(bundle)
                startActivity(moveIntentWithBundle)
            }
        })
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
