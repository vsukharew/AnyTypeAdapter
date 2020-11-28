package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.Collection
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.feed.presentation.view.adapter.AlbumsSectionDelegate.Holder
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.SpannedGridLayoutManager
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.SpannedGridLayoutManager.SpanInfo
import vsukharev.anytypeadapter.sample.feed.domain.model.Album
import java.util.*

/**
 * TODO rename after deleting "based on preferences" label
 * The "based on preferences" section delegate
 */
class AlbumsSectionDelegate(
    onHoldItemListener: (Boolean) -> Unit
) : BaseDelegate<List<Album>, Holder>() {
    private val delegate = AlbumsDelegate(onHoldItemListener)
    private val anyTypeAdapter = AnyTypeAdapter()

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.section_albums

    override fun getItemId(item: List<Album>): String = ITEM_ID

    inner class Holder(itemView: View) : BaseViewHolder<List<Album>>(itemView) {
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.albums_section_rv)

        init {
            recyclerView.apply {
                layoutManager = SpannedGridLayoutManager(
                    { position ->
                        when (position) {
                            0, 1 -> SpanInfo(3, 3)
                            else -> SpanInfo(2, 2)
                        }
                    }, 6,
                    1f
                )
                (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
                adapter = anyTypeAdapter
            }
        }

        override fun bind(item: List<Album>) {
            Collection.Builder()
                .add(item, delegate)
                .build()
                .let { anyTypeAdapter.setCollection(it) }
        }
    }

    private companion object {
        val ITEM_ID: String = UUID.randomUUID().toString()
    }
}