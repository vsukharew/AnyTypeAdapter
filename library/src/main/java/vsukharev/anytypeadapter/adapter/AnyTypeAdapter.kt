package vsukharev.anytypeadapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.item.AdapterItem

/**
 * Adapter that is able to display items of any view type at the same time
 */
open class AnyTypeAdapter : RecyclerView.Adapter<AnyTypeViewHolder<Any, ViewBinding>>() {
    protected var anyTypeCollection: AnyTypeCollection = AnyTypeCollection.EMPTY
    var diffStrategy: DiffStrategy = DiffStrategy.DiscardLatest()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnyTypeViewHolder<Any, ViewBinding> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return anyTypeCollection.currentItemViewTypeDelegate.createViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnyTypeViewHolder<Any, ViewBinding>, position: Int) {
        with(anyTypeCollection) {
            currentItemViewTypeDelegate.bind(items[position], holder)
        }
    }

    override fun getItemCount(): Int = anyTypeCollection.size

    override fun getItemViewType(position: Int): Int {
        return anyTypeCollection.run {
            findCurrentItemViewTypePosition(position).also { currentItemViewTypePosition = it }
            currentItemViewTypeDelegate.getItemViewType()
        }
    }

    /**
     * Sets new [AnyTypeCollection] to adapter and fires [onUpdatesDispatch]
     * callback as soon as [AnyTypeCollection] is set
     */
    fun setCollection(
        collection: AnyTypeCollection,
        onUpdatesDispatch: ((AnyTypeCollection) -> Unit)? = null
    ) {
        val diffBlock = suspend {
            val adapter = this
            val diffResult = DiffUtil.calculateDiff(
                DiffUtilCallback(
                    anyTypeCollection.items,
                    collection.items
                )
            )
            withContext(Dispatchers.Main) {
                anyTypeCollection = collection
                diffResult.dispatchUpdatesTo(adapter)
                onUpdatesDispatch?.invoke(collection)
            }
        }
        diffStrategy.calculateDiff(diffBlock)
    }

    private class DiffUtilCallback(
        private val oldList: List<AdapterItem<Any>>,
        private val newList: List<AdapterItem<Any>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].areItemsTheSame(newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].areContentsTheSame(newList[newItemPosition])
    }
}