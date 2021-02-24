package vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter

import android.view.View
import com.bumptech.glide.Glide
import vsukharev.anytypeadapter.delegate.AnyTypeDelegate
import vsukharev.anytypeadapter.holder.AnyTypeViewHolder
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.databinding.DelegateTrackBinding
import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter.TracksDelegate.Holder

class TracksDelegate : AnyTypeDelegate<Track, DelegateTrackBinding, Holder>() {

    override fun createViewHolder(itemView: View): Holder = Holder(
        DelegateTrackBinding.bind(itemView)
    )

    override fun getItemViewType(): Int = R.layout.delegate_track

    override fun getItemId(item: Track): String = item.id

    class Holder(
        binding: DelegateTrackBinding
    ) : AnyTypeViewHolder<Track, DelegateTrackBinding>(binding) {
        private val coverIv = binding.delegateTracksCoverIv
        private val trackTv = binding.delegateTracksTrackTv
        private val performerTv = binding.delegateTracksPerformerTv

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