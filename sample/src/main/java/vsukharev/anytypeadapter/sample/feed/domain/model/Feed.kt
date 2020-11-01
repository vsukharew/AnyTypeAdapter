package vsukharev.anytypeadapter.sample.feed.domain.model

data class Feed(
    val albums: List<Album>,
    val editorsChoice: List<EditorsChoice>
)