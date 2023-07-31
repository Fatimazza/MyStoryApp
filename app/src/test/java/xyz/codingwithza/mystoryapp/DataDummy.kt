package xyz.codingwithza.mystoryapp

import xyz.codingwithza.mystoryapp.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
            "2022-01-08T06:34:18.598Z",
            "Fatima",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse a turpis aliquet, viverra est nec, fermentum nibh.",
                -16.002,
            "story-FvU4u0Vp2S3PMsFg",
                -10.212
            )
            items.add(story)
        }
        return items
    }
}
