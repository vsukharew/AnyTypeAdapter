package vsukharev.anytypeadapter.sample.albums.domain.model

import java.util.*

/**
 * The model describing what the editor chooses
 */
data class EditorsChoice(
    val id: UUID,
    val starName: String,
    val description: String,
    val imageUrl: String
)