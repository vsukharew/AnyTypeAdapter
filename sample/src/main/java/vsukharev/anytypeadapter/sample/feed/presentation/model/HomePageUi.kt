package vsukharev.anytypeadapter.sample.feed.presentation.model

import vsukharev.anytypeadapter.sample.feed.domain.model.Album
import vsukharev.anytypeadapter.sample.feed.domain.model.EditorsChoice

/**
 * The model describing content at the home tab
 */
data class HomePageUi(
    val albums: List<Album>,
    val editorsChoice: List<EditorsChoice>
)