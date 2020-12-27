package vsukharev.anytypeadapter.sample.tracks.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_tracks.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.Collection
import vsukharev.anytypeadapter.sample.Injector
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.presentation.BaseFragment
import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import vsukharev.anytypeadapter.sample.tracks.presentation.TracksPresenter
import vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter.TracksDelegate
import javax.inject.Inject

/**
 * Fragment showing the list of tracks
 */
class TracksFragment : BaseFragment(), TracksView {

    private val anyTypeAdapter = AnyTypeAdapter()
    private val tracksDelegate = TracksDelegate()

    @Inject
    @InjectPresenter
    lateinit var presenter: TracksPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.buildTracksComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tracks_rv.apply {
            adapter = anyTypeAdapter
        }
    }

    override fun showProgress() {
        tracks_pb.isVisible = true
    }

    override fun hideProgress() {
        tracks_pb.isVisible = false
    }

    override fun showTracks(tracks: List<Track>) {
        Collection.Builder()
            .add(tracks, tracksDelegate)
            .build()
            .let { anyTypeAdapter.setCollection(it) }
    }

    override fun showEmptyState() {
        anyTypeAdapter.setCollection(Collection.EMPTY)
    }

    override fun onDestroy() {
        Injector.destroyTracksComponent()
        super.onDestroy()
    }

    companion object {
        fun newInstance(): TracksFragment = TracksFragment()
    }
}