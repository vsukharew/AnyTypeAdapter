package vsukharev.anytypeadapter.sample.feed.domain.model

import java.util.*

/**
 * Model of a music album
 */
data class Album(
    val name: String,
    val performer: String,
    val coverUrl: String,
    val id: String = UUID.randomUUID().toString()
)