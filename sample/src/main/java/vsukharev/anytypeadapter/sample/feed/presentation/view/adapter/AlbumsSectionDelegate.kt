package vsukharev.anytypeadapter.sample.feed.presentation.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.Collection
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
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
    onItemClickListener: (Album) -> Unit
) : AnyTypeDelegate<List<Album>, Holder>() {
    private val delegate = AlbumsDelegate(onItemClickListener)
    private val anyTypeAdapter = AnyTypeAdapter()

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.section_albums

    override fun getItemId(item: List<Album>): String = ITEM_ID

    inner class Holder(itemView: View) : AnyTypeViewHolder<List<Album>>(itemView) {
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.albums_section_rv)

        init {
            recyclerView.apply {
                (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
                adapter = anyTypeAdapter
            }
        }

        override fun bind(item: List<Album>) {
            initLayoutManager(recyclerView, item)
            Collection.Builder()
                .add(item, delegate)
                .build()
                .let { anyTypeAdapter.setCollection(it) }
        }

        private fun initLayoutManager(rv: RecyclerView, items: List<Album>) {
            rv.layoutManager = SpannedGridLayoutManager(
                { position ->
                    when {
                        shouldRender2x2Table(items) -> SpanInfo(3, 3)
                        shouldRender3x3Table(items) -> SpanInfo(2, 2)
                        shouldRenderLastPairAs3x3(position, items) -> SpanInfo(3, 3)
                        position in 0..1 -> SpanInfo(3, 3)
                        else -> SpanInfo(2, 2)
                    }
                }, 6,
                1f
            )
        }

        private fun shouldRender2x2Table(items: List<Album>): Boolean = items.size == 4
        private fun shouldRender3x3Table(items: List<Album>): Boolean = items.size == 6
        private fun shouldRenderLastPairAs3x3(position: Int, items: List<Album>): Boolean =
            items.size > 6 && position in items.lastIndex - 1..items.lastIndex
    }

    private companion object {
        val ITEM_ID: String = UUID.randomUUID().toString()
    }
}