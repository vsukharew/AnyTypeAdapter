package vsukharev.anytypeadapter.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vsukharev.anytypeadapter.holder.BaseViewHolder
import vsukharev.anytypeadapter.item.AdapterItem
import java.util.concurrent.*

/**
 * Adapter that is able to display items of any view type together
 */
open class AnyTypeAdapter : RecyclerView.Adapter<BaseViewHolder<Any>>() {
    private var collection: Collection = Collection.EMPTY
    private var currentItemViewType = 0

    private val backgroundThreadExecutor = ThreadPoolExecutor(
        1,
        1,
        0L,
        TimeUnit.SECONDS,
        SynchronousQueue<Runnable>()
    )

    private val mainThreadExecutor = object : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Any> {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return collection.viewTypeToDelegateMap.get(viewType).createViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Any>, position: Int) {
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

    fun setCollection(collection: Collection, onUpdatesDispatch: ((Collection) -> Unit)? = null) {
        backgroundThreadExecutor.execute {
            val diffResult =
                DiffUtil.calculateDiff(DiffUtilCallback(this.collection.items, collection.items))
            mainThreadExecutor.execute {
                this.collection = collection
                diffResult.dispatchUpdatesTo(this)
                onUpdatesDispatch?.invoke(collection)
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