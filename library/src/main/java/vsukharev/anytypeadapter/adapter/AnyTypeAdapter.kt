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
        return with(anyTypeCollection) {
            findCurrentItemViewTypePosition(positionsRanges, position)
                .also { currentItemViewTypePosition = it }
                .let { currentItemViewTypeDelegate.getItemViewType() }
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
        positionsRanges: List<IntRange>,
        adapterPosition: Int
    ): Int {
        return with(positionsRanges) {
            binarySearch {
                when {
                    adapterPosition in it -> 0
                    adapterPosition < it.first -> 1
                    else -> -1
                }
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