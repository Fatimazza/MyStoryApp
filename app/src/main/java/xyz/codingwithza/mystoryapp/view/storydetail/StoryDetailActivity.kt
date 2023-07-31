package xyz.codingwithza.mystoryapp.view.storydetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.codingwithza.mystoryapp.data.remote.response.ListStoryItem
import xyz.codingwithza.mystoryapp.databinding.ActivityStoryDetailBinding
import xyz.codingwithza.mystoryapp.util.loadImage

class StoryDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    private lateinit var binding: ActivityStoryDetailBinding
    private var story: ListStoryItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initIntentExtra()
        setupView()
    }

    private fun initIntentExtra() {
        story = intent.getParcelableExtra(EXTRA_STORY)
    }

    private fun setupView() {
        if (story != null){
            binding.apply {
                tvStoryName.text = story?.name
                tvStoryDesc.text = story?.description
                ivDetailPhoto.loadImage(story?.photoUrl)
            }
        }
    }
}
