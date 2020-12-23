package vsukharev.anytypeadapter.sample.feed.domain.model

data class Feed(
    val albums: List<Album>,
    val menuItems: List<MenuItem>,
    val editorsChoice: List<EditorsChoice>,
    val activities: List<Activity>
)