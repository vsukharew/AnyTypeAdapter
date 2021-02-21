package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.databinding.ItemListAlbumsBinding
import vsukharev.anytypeadapter.sample.feed.domain.model.Album
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.AlbumsDelegate.Holder

/**
 * Single album delegate
 */
class AlbumsDelegate(
    private val onItemClickListener: (Album) -> Unit
) : AnyTypeDelegate<Album, ItemListAlbumsBinding, Holder>() {

    override fun createViewHolder(itemView: View) = Holder(ItemListAlbumsBinding.bind(itemView))

    override fun getItemViewType(): Int = R.layout.item_list_albums

    override fun getItemId(item: Album): String = item.id

    inner class Holder(
        binding: ItemListAlbumsBinding
    ) : AnyTypeViewHolder<Album, ItemListAlbumsBinding>(binding) {
        private val coverIv = binding.albumsListCoverIv
        private val container = binding.albumsListContainer

        @SuppressLint("ClickableViewAccessibility")
        override fun bind(item: Album) {
            Glide.with(context)
                .load(item.coverUrl)
                .placeholder(R.drawable.ic_album_placeholder)
                .error(R.drawable.ic_album_placeholder)
                .into(coverIv)

            coverIv.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        container.isPressed = true
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        onItemClickListener.invoke(item)
                        container.isPressed = false
                        true
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        container.isPressed = false
                        true
                    }
                    else -> false
                }
            }
        }
    }
}