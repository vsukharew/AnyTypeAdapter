package vsukharev.anytypeadapter.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.item.AdapterItem
import vsukharev.anytypeadapter.item.AdapterItemMetaData
import java.util.concurrent.Executor
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Adapter that is able to display items of any view type at the same time
 */
open class AnyTypeAdapter : RecyclerView.Adapter<AnyTypeViewHolder<Any, ViewBinding>>() {
    protected var anyTypeCollection: AnyTypeCollection = AnyTypeCollection.EMPTY

    private val backgroundThreadExecutor = ThreadPoolExecutor(
        1,
        1,
        0L,
        TimeUnit.SECONDS,
        SynchronousQueue(),
        ThreadPoolExecutor.DiscardOldestPolicy()
    )
    private val mainThreadExecutor = object : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }

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
            findCurrentItemViewTypePosition(itemsMetaData, position)
                .also { currentItemViewTypePosition = it }
                .let { delegateAt(it).getItemViewType() }
        }
    }

    /**
     * Sets new [AnyTypeCollection] to adapter and fires [onUpdatesDispatch]
     * callback as soon as [AnyTypeCollection] is set
     */
    fun setCollection(collection: AnyTypeCollection, onUpdatesDispatch: ((AnyTypeCollection) -> Unit)? = null) {
        backgroundThreadExecutor.execute {
            val diffResult =
                DiffUtil.calculateDiff(DiffUtilCallback(this.anyTypeCollection.items, collection.items))
            mainThreadExecutor.execute {
                this.anyTypeCollection = collection
                diffResult.dispatchUpdatesTo(this)
                onUpdatesDispatch?.invoke(collection)
            }
        }
    }

    /**
     * Finds position inside [itemsMetaData] for the current item view type
     * given current [adapterPosition]
     * @see [AnyTypeCollection.itemsMetaData]
     */
    private fun findCurrentItemViewTypePosition(
        itemsMetaData: List<AdapterItemMetaData<Any, ViewBinding>>,
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