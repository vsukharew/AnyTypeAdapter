package vsukharev.anytypeadapter.sample.albums.presentation.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.Collection
import vsukharev.anytypeadapter.delegate.BaseDelegate
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.AlbumsSectionDelegate.Holder
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.SpannedGridLayoutManager
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.SpannedGridLayoutManager.SpanInfo

/**
 * TODO rename after deleting "based on preferences" label
 * The "based on preferences" section delegate
 */
class AlbumsSectionDelegate(
    onHoldItemListener: (Boolean) -> Unit
) : BaseDelegate<AlbumsSectionAdapterItem, Holder>() {
    private val delegate = AlbumsDelegate(onHoldItemListener)

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.section_albums

    inner class Holder(itemView: View) : BaseViewHolder<AlbumsSectionAdapterItem>(itemView) {
        private val recyclerView = itemView.findViewById<RecyclerView>(R.id.albums_section_rv)
        private val anyTypeAdapter = AnyTypeAdapter()

        init {
            recyclerView.apply {
                layoutManager = SpannedGridLayoutManager({ position ->
                    when (position) {
                        0, 1 -> SpanInfo(3, 3)
                        else -> SpanInfo(2, 2)
                    }
                }, 6,
                    1f
                )
                adapter = anyTypeAdapter
            }
        }

        override fun bind(item: AlbumsSectionAdapterItem) {
            Collection.Builder()
                .add(item.adapterAlbums, delegate)
                .build()
                .let { anyTypeAdapter.setItems(it) }
        }
    }
}