package vsukharev.anytypeadapter.sample.feed.domain.model

/**
 * Model of a music album
 */
data class Album(
    val id: String,
    val name: String,
    val performer: String,
    val coverUrl: String
)