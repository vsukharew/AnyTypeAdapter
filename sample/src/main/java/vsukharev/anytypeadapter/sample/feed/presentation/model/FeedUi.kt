package vsukharev.anytypeadapter.sample.feed.presentation.model

import vsukharev.anytypeadapter.sample.common.presentation.delegate.IconWithTextAdapterItem
import vsukharev.anytypeadapter.sample.feed.domain.model.Activity
import vsukharev.anytypeadapter.sample.feed.domain.model.Album
import vsukharev.anytypeadapter.sample.feed.domain.model.EditorsChoice

/**
 * The model describing content at the feed tab
 */
data class FeedUi(
    val albums: List<Album>,
    val menuItems: List<IconWithTextAdapterItem>,
    val editorsChoice: List<EditorsChoice>,
    val activities: List<Activity>
)