package xyz.codingwithza.mystoryapp.view.story


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import xyz.codingwithza.mystoryapp.R
import xyz.codingwithza.mystoryapp.data.remote.response.ListStoryItem
import xyz.codingwithza.mystoryapp.databinding.ItemStoryBinding

class StoryAdapter() : PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    private var storyList = ArrayList<ListStoryItem>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class StoryViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(stories: ListStoryItem) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            storyList.addAll(listOf(data))
            holder.bind(data)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(storyList[holder.bindingAdapterPosition])
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
