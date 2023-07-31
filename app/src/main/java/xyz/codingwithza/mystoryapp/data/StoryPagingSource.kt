package xyz.codingwithza.mystoryapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import xyz.codingwithza.mystoryapp.data.remote.response.ListStoryItem

class StoryPagingSource() : PagingSource<Int, ListStoryItem>() {
    
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        TODO("Not yet implemented")
    }

}
