package xyz.codingwithza.mystoryapp.view.storydetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import xyz.codingwithza.mystoryapp.data.local.ListStoryItem
import xyz.codingwithza.mystoryapp.databinding.ActivityStoryDetailBinding

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

                Glide.with(this@StoryDetailActivity)
                    .load(story?.photoUrl)
                    .into(ivDetailPhoto)
            }
        }
    }
}
