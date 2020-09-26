package vsukharev.anytypeadapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.item.AdapterItem

/**
 * Adapter that is able to display items of any view type together
 */
open class AnyTypeAdapter : RecyclerView.Adapter<BaseViewHolder<AdapterItem>>() {
    private var asyncListDiffer: AsyncListDiffer<AdapterItem>? = null
    private var collection: Collection =
        Collection.EMPTY
    private var currentItemViewType = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AdapterItem> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return collection.viewTypeToDelegateMap.get(viewType).createViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<AdapterItem>, position: Int) {
        with(collection) {
            val itemType = positionToViewTypeMap[currentItemViewType]
            val controller = viewTypeToDelegateMap[itemType]
            controller.bind(items[position], holder)
        }
    }

    override fun getItemCount(): Int = collection.size

    override fun getItemViewType(position: Int): Int {
        return with(collection.positionToViewTypeMap) {
            loop@ for (i in 1 until size()) {
                // Each two adjacent values of the map represent the positions range
                // in which the items with the same viewType are placed
                val range = keyAt(i - 1) until keyAt(i)

                // For the last items portion it's enough to know only the left bound of the range.
                // The viewType will be the same until the collection ends
                val lastViewTypeStartPosition = keyAt(size() - 1)

                // The following code is looking for the range the current position fits in
                when {
                    position in range -> {
                        currentItemViewType = range.first
                        break@loop
                    }
                    position >= lastViewTypeStartPosition -> {
                        currentItemViewType = lastViewTypeStartPosition
                        break@loop
                    }
                }
            }
            get(currentItemViewType)
        }
    }

    fun setItems(collection: Collection) {
        this.collection = collection
        asyncListDiffer?.submitList(collection.items)
            ?: AsyncListDiffer(this, Callback())
                .also {
                    asyncListDiffer = it
                    it.submitList(collection.items)
                }
    }

    private class Callback : DiffUtil.ItemCallback<AdapterItem>() {
        override fun areItemsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean {
            return oldItem.areItemsTheSame(newItem)
        }

        override fun areContentsTheSame(oldItem: AdapterItem, newItem: AdapterItem): Boolean {
            return oldItem.areContentsTheSame(newItem)
        }
    }
}