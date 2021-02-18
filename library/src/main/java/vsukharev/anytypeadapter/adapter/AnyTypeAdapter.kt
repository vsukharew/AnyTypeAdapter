package vsukharev.anytypeadapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.item.AdapterItem
import vsukharev.anytypeadapter.item.AdapterItemMetaData

/**
 * Adapter that is able to display items of any view type at the same time
 */
open class AnyTypeAdapter : RecyclerView.Adapter<AnyTypeViewHolder<Any>>(),
    CoroutineScope by MainScope() {
    protected var anyTypeCollection: AnyTypeCollection = AnyTypeCollection.EMPTY
    private var diffJob: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnyTypeViewHolder<Any> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return with(anyTypeCollection) {
            delegateAt(currentItemViewTypePosition).createViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: AnyTypeViewHolder<Any>, position: Int) {
        with(anyTypeCollection) {
            val delegate = delegateAt(currentItemViewTypePosition)
            delegate.bind(items[position], holder)
        }
    }

    override fun getItemCount(): Int = anyTypeCollection.size

    override fun getItemViewType(position: Int): Int {
        return with(anyTypeCollection) {
            findCurrentItemViewTypePosition(itemsMetaData, position)
                .also { currentItemViewTypePosition = it }
                .let { delegateAt(it).getItemViewType() }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        cancel()
    }

    /**
     * Sets new [AnyTypeCollection] to adapter and fires [onUpdatesDispatch]
     * callback as soon as [AnyTypeCollection] is set
     */
    fun setCollection(
        collection: AnyTypeCollection,
        onUpdatesDispatch: ((AnyTypeCollection) -> Unit)? = null
    ) {
        val adapter = this
        diffJob?.cancel()
        diffJob = launch {
            val diffResult = withContext(Dispatchers.Default) {
                DiffUtil.calculateDiff(
                    DiffUtilCallback(
                        anyTypeCollection.items,
                        collection.items
                    )
                )
            }
            anyTypeCollection = collection
            diffResult.dispatchUpdatesTo(adapter)
            onUpdatesDispatch?.invoke(collection)
        }
    }

    /**
     * Finds position inside [itemsMetaData] for the current item view type
     * given current [adapterPosition]
     * @see [AnyTypeCollection.itemsMetaData]
     */
    private fun findCurrentItemViewTypePosition(
        itemsMetaData: List<AdapterItemMetaData<Any>>,
        adapterPosition: Int
    ): Int {
        return with(itemsMetaData) {
            if (size == 1) {
                0
            } else {
                var result = 0
                // Each two adjacent values in the list represent the positions range
                // in which the items with the same viewType are placed
                val positionsRanges = zipWithNext { first, second ->
                    first.position until second.position
                }

                /**
                 * The following code is looking for the range the [adapterPosition] fits in
                 * or determines that [adapterPosition] is larger than right border of the last range
                 */
                loop@ for (i in positionsRanges.indices) {
                    when {
                        adapterPosition in positionsRanges[i] -> {
                            result = i
                            break@loop
                        }
                        adapterPosition > positionsRanges.last().last -> {
                            result = positionsRanges.size
                            break@loop
                        }
                    }
                }
                result
            }
        }
    }

    private class DiffUtilCallback(
        private val oldList: List<AdapterItem<*>>,
        private val newList: List<AdapterItem<*>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].areItemsTheSame(newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].areContentsTheSame(newList[newItemPosition])
    }
}