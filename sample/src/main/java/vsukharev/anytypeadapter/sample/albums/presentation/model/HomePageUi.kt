package vsukharev.anytypeadapter.sample.albums.presentation.model

import vsukharev.anytypeadapter.sample.albums.domain.model.Album
import vsukharev.anytypeadapter.sample.albums.domain.model.EditorsChoice

/**
 * The model describing content at the home tab
 */
data class HomePageUi(
    val albums: List<Album>,
    val editorsChoice: List<EditorsChoice>
)