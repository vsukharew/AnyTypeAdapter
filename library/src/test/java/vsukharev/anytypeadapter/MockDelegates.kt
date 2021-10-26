package vsukharev.anytypeadapter

import android.view.View
import androidx.viewbinding.ViewBinding
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.delegate.NoDataDelegate
import vsukharev.anytypeadapter.domain.Activity
import vsukharev.anytypeadapter.domain.Track
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.holder.NoDataViewHolder
import java.util.*

val headerDelegate =
    object : NoDataDelegate<ViewBinding>() {
        override fun createViewHolder(itemView: View): NoDataViewHolder<ViewBinding> {
            TODO("Not yet implemented")
        }

        override fun getItemViewType(): Int = 0
    }

val trackDelegate =
    object : AnyTypeDelegate<Track, ViewBinding, AnyTypeViewHolder<Track, ViewBinding>>() {
        override fun createViewHolder(itemView: View): AnyTypeViewHolder<Track, ViewBinding> {
            TODO("Not yet implemented")
        }

        override fun getItemViewType(): Int = 2

        override fun getItemId(item: Track): String = UUID.randomUUID().toString()
    }

val activityDelegate =
    object : AnyTypeDelegate<Activity, ViewBinding, AnyTypeViewHolder<Activity, ViewBinding>>() {
        override fun createViewHolder(itemView: View): AnyTypeViewHolder<Activity, ViewBinding> {
            TODO("Not yet implemented")
        }

        override fun getItemViewType(): Int = 3

        override fun getItemId(item: Activity): String = item.toString()
    }

val trackListDelegate =
    object : AnyTypeDelegate<List<Track>, ViewBinding, AnyTypeViewHolder<List<Track>, ViewBinding>>() {
        override fun createViewHolder(itemView: View): AnyTypeViewHolder<List<Track>, ViewBinding> {
            TODO("Not yet implemented")
        }

        override fun getItemViewType(): Int = 4

        override fun getItemId(item: List<Track>): String = item.toString()
    }