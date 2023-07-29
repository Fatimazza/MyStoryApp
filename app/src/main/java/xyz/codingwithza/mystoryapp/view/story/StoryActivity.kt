package xyz.codingwithza.mystoryapp.view.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import xyz.codingwithza.mystoryapp.view.main.MainActivity
import xyz.codingwithza.mystoryapp.R
import xyz.codingwithza.mystoryapp.data.local.ListStoryItem
import xyz.codingwithza.mystoryapp.data.remote.Result
import xyz.codingwithza.mystoryapp.databinding.ActivityStoryBinding
import xyz.codingwithza.mystoryapp.view.ViewModelFactory
import xyz.codingwithza.mystoryapp.view.addStory.AddStoryActivity
import xyz.codingwithza.mystoryapp.view.storydetail.StoryDetailActivity


class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var storyAdapter: StoryAdapter

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

            swipeStoryLayout.setOnRefreshListener { showAllStories() }
            fabStoryAdd.setOnClickListener {
                startActivity(Intent(this@StoryActivity, AddStoryActivity::class.java))
            }
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
                            binding.swipeStoryLayout.isRefreshing = false
                        }
                        is Result.Error -> {
                            showLoading(false)
                            binding.swipeStoryLayout.isRefreshing = false
                            Snackbar.make(
                                binding.root,
                                getString(R.string.load_error) + result.error,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showAllStories()
    }

    private fun addListClickListener() {
        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                val moveIntentWithBundle = Intent(this@StoryActivity, StoryDetailActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(StoryDetailActivity.EXTRA_STORY, data)
                moveIntentWithBundle.putExtras(bundle)
                startActivity(moveIntentWithBundle,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@StoryActivity).toBundle())
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMenuItem(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMenuItem(action: Int) {
        when (action) {
            R.id.action_logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle(R.string.auth_logout)
                    setMessage(getString(R.string.auth_logout_warning))
                    setPositiveButton(getString(R.string.button_yes)) { _, _ ->
                        storyViewModel.logout()
                        startActivity(Intent(this@StoryActivity, MainActivity::class.java))
                        finishAffinity()
                    }
                    setNegativeButton(getString(R.string.button_no)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_story, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
