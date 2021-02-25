package vsukharev.anytypeadapter.sample.feed.domain.model

import java.util.*

/**
 * The model describing what the editor chooses
 */
data class EditorsChoice(
    val starName: String,
    val description: String,
    val imageUrl: String,
    val id: UUID = UUID.randomUUID()
)