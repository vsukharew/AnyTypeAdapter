package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter

import vsukharev.anytypeadapter.item.AdapterItem

/**
 * TODO rename after deleting "based on preferences" label
 * The [AdapterItem] wrapping the item representing "based on preferences" section
 */
data class AlbumsSectionAdapterItem(val adapterAlbums: List<AlbumAdapterItem>) : AdapterItem {
    override fun areItemsTheSame(other: AdapterItem): Boolean = true

    override fun areContentsTheSame(other: AdapterItem): Boolean = false
}