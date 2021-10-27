package vsukharev.anytypeadapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.item.AdapterItem

/**
 * Adapter that is able to display items of any view type at the same time
 */
open class AnyTypeAdapter : RecyclerView.Adapter<AnyTypeViewHolder<Any, ViewBinding>>(),
    CoroutineScope by MainScope() {
    var diffStrategy: DiffStrategy = DiffStrategy.DiscardLatest
    private val diffActor =
        actor<() -> Unit?>(capacity = Channel.BUFFERED) { consumeEach { it.invoke() } }
    private var diffJob: Job? = null

    protected var anyTypeCollection: AnyTypeCollection = AnyTypeCollection.EMPTY

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
        val diffBlock = {
            val adapter = this
            val diffResult = DiffUtil.calculateDiff(
                DiffUtilCallback(
                    anyTypeCollection.items,
                    collection.items
                )
            )
            anyTypeCollection = collection
            diffResult.dispatchUpdatesTo(adapter)
            onUpdatesDispatch?.invoke(collection)
        }
        diffJob = when (diffStrategy) {
            DiffStrategy.Queue -> {
                launch {
                    diffActor.send(diffBlock)
                }
            }
            DiffStrategy.DiscardLatest -> {
                diffJob?.cancel()
                launch {
                    diffActor.send(diffBlock)
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

    sealed class DiffStrategy {
        object Queue : DiffStrategy()
        object DiscardLatest : DiffStrategy()
    }
}