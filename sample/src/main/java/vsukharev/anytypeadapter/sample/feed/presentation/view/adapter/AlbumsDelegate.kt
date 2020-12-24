package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.feed.domain.model.Album
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.AlbumsDelegate.Holder

/**
 * TODO rename after deleting "based on preferences" label
 * The single "based on preferences" album delegate
 */
class AlbumsDelegate(
    private val onHoldItemListener: (Boolean) -> Unit
) : BaseDelegate<Album, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.item_list_albums

    override fun getItemId(item: Album): String = item.id

    inner class Holder(itemView: View) : BaseViewHolder<Album>(itemView) {
        private val coverIv = itemView.findViewById<ImageView>(R.id.albums_list_cover_iv)
        private val container = itemView.findViewById<FrameLayout>(R.id.albums_list_container)

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
                        onHoldItemListener.invoke(true)
                        container.isPressed = true
                        true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        onHoldItemListener.invoke(false)
                        container.isPressed = false
                        true
                    }
                    else -> false
                }
            }
        }
    }
}