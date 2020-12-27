package vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.delegate_track.view.*
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter.TracksDelegate.Holder

class TracksDelegate : AnyTypeDelegate<Track, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(itemView)

    override fun getItemViewType(): Int = R.layout.delegate_track

    override fun getItemId(item: Track): String = item.id

    class Holder(itemView: View) : AnyTypeViewHolder<Track>(itemView) {
        private val coverIv = itemView.delegate_tracks_cover_iv
        private val trackTv = itemView.delegate_tracks_track_tv
        private val performerTv = itemView.delegate_tracks_performer_tv

        override fun bind(item: Track) {
            with(item) {
                Glide.with(context)
                    .load(albumCoverUrl)
                    .placeholder(R.drawable.ic_album_placeholder)
                    .error(R.drawable.ic_album_placeholder)
                    .into(coverIv)
                performerTv.text = performer
                trackTv.text = name
            }
        }
    }
}