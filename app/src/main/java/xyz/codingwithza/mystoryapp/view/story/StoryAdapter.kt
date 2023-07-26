package xyz.codingwithza.mystoryapp.view.story


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import xyz.codingwithza.mystoryapp.R
import xyz.codingwithza.mystoryapp.data.local.ListStoryItem
import xyz.codingwithza.mystoryapp.databinding.ItemStoryBinding

class StoryAdapter() : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private var storyList = ArrayList<ListStoryItem>()

    inner class StoryViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val stories = storyList[position]
        holder.apply {
            binding.apply {
                tvStoryName.text = stories.name
                tvStoryDesc.text = stories.description

                Glide.with(itemView.context)
                    .load(stories.photoUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_baseline_image_search_24))
                    .error(R.drawable.ic_baseline_broken_image_24)
                    .into(ivStoryPhoto)
            }
        }
    }

    override fun getItemCount(): Int = storyList.size
}
