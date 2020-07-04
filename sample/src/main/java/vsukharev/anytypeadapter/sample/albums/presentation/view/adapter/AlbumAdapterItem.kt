package vsukharev.anytypeadapter.sample.albums.presentation.view.adapter

import vsukharev.anytypeadapter.item.AdapterItem
import vsukharev.anytypeadapter.sample.albums.domain.model.Album

/**
 * The [AdapterItem] wrapping a single "based on preferences" album
 */
data class AlbumAdapterItem(val album: Album) : AdapterItem {
    override fun areItemsTheSame(other: AdapterItem): Boolean =
        other is AlbumAdapterItem && album.id == other.album.id

    override fun areContentsTheSame(other: AdapterItem): Boolean = this == other
}