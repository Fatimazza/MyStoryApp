package xyz.codingwithza.mystoryapp.view.story


import androidx.recyclerview.widget.RecyclerView
import xyz.codingwithza.mystoryapp.databinding.ItemStoryBinding

class StoryAdapter(): RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder (val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

}
