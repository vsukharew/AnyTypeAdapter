package vsukharev.anytypeadapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder

/**
 * Adapter that is able to display items of any view type at the same time
 */
open class AnyTypeAdapter : RecyclerView.Adapter<AnyTypeViewHolder<Any, ViewBinding>>() {
    protected var anyTypeCollection: AnyTypeCollection = AnyTypeCollection.EMPTY
    var diffStrategy: DiffStrategy = DiffStrategy.DiscardLatest()
    var isPayloadEnabled = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnyTypeViewHolder<Any, ViewBinding> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        val delegate = anyTypeCollection.itemViewTypesToDelegates[viewType]
        requireNotNull(delegate)
        return delegate.createViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: AnyTypeViewHolder<Any, ViewBinding>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.applyPayload(payloads)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: AnyTypeViewHolder<Any, ViewBinding>, position: Int) {
        with(anyTypeCollection) {
            val item = items[position]
            val itemViewType = item.itemViewType
            val delegate = itemViewTypesToDelegates[itemViewType]
            delegate?.bind(item, holder)
        }
    }

    override fun getItemCount(): Int = anyTypeCollection.size

    override fun getItemViewType(position: Int): Int {
        return anyTypeCollection.items[position].itemViewType
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
            val diffCallback = if (isPayloadEnabled) {
                PayloadDiffUtilCallback(anyTypeCollection, collection)
            } else {
                DiffUtilCallback(anyTypeCollection, collection)
            }
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            withContext(Dispatchers.Main) {
                anyTypeCollection = collection
                diffResult.dispatchUpdatesTo(adapter)
                onUpdatesDispatch?.invoke(collection)
            }
        }
        diffStrategy.calculateDiff(diffBlock)
    }

    private class PayloadDiffUtilCallback(
        oldList: AnyTypeCollection,
        newList: AnyTypeCollection
    ) : DiffUtilCallback(oldList, newList) {

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val delegate = with(oldList) {
                val itemViewType = items[oldItemPosition].itemViewType
                itemViewTypesToDelegates[itemViewType]
            }
            return delegate?.getChangePayload(
                oldList.items[oldItemPosition].data,
                newList.items[newItemPosition].data
            )
        }
    }

    private open class DiffUtilCallback(
        protected val oldList: AnyTypeCollection,
        protected val newList: AnyTypeCollection
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList.items[oldItemPosition].areItemsTheSame(newList.items[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList.items[oldItemPosition].areContentsTheSame(newList.items[newItemPosition])
    }
}