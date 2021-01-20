package vsukharev.anytypeadapter.sample.tracks.domain.model

import java.util.*

data class Track(
    val name: String,
    val performer: String,
    val albumCoverUrl: String,
    val id: String = UUID.randomUUID().toString()
)