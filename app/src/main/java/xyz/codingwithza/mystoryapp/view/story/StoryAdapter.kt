package xyz.codingwithza.mystoryapp.view.story


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.codingwithza.mystoryapp.databinding.ItemStoryBinding

class StoryAdapter(): RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder (val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}
