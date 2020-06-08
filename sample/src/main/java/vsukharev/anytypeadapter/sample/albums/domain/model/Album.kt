package vsukharev.anytypeadapter.sample.albums.domain.model

/**
 * Model of a music album
 */
data class Album(
    val id: String,
    val name: String,
    val performer: String,
    val issueYear: Int,
    val coverUrl: String
)