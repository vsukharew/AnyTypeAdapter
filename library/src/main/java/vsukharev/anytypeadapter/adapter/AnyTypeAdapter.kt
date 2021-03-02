package vsukharev.anytypeadapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.item.AdapterItem

/**
 * Adapter that is able to display items of any view type at the same time
 */
open class AnyTypeAdapter : RecyclerView.Adapter<AnyTypeViewHolder<Any, ViewBinding>>(),
    CoroutineScope by MainScope() {
    protected var anyTypeCollection: AnyTypeCollection = AnyTypeCollection.EMPTY
    private var diffJob: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnyTypeViewHolder<Any, ViewBinding> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return with(anyTypeCollection) {
            delegateAt(currentItemViewTypePosition).createViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: AnyTypeViewHolder<Any, ViewBinding>, position: Int) {
        with(anyTypeCollection) {
            val delegate = delegateAt(currentItemViewTypePosition)
            delegate.bind(items[position], holder)
        }
    }

    override fun getItemCount(): Int = anyTypeCollection.size

    override fun getItemViewType(position: Int): Int {
        return with(anyTypeCollection) {
            findCurrentItemViewTypePosition(this, position)
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
     * Finds position inside [anyTypeCollection] for the current item view type
     * given current [adapterPosition]
     * @see [AnyTypeCollection.itemsMetaData]
     */
    private fun findCurrentItemViewTypePosition(
        anyTypeCollection: AnyTypeCollection,
        adapterPosition: Int
    ): Int {
        return with(anyTypeCollection) {
            if (itemsMetaData.size == 1) {
                0
            } else {
                var result = 0
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