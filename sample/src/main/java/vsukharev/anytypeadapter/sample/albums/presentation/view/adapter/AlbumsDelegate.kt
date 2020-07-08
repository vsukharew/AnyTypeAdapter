package vsukharev.anytypeadapter.sample.albums.presentation.view.adapter

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.AlbumsDelegate.Holder

/**
 * The single "based on preferences" album delegate
 */
class AlbumsDelegate : BaseDelegate<AlbumAdapterItem, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.item_list_albums

    inner class Holder(itemView: View) : BaseViewHolder<AlbumAdapterItem>(itemView) {
        private val coverIv = itemView.findViewById<ImageView>(R.id.albums_list_cover_iv)

        override fun bind(item: AlbumAdapterItem) {
            Glide.with(itemView.context)
                .load(item.album.coverUrl)
                .placeholder(R.drawable.ic_album_placeholder)
                .error(R.drawable.ic_album_placeholder)
                .into(coverIv)
        }
    }
}