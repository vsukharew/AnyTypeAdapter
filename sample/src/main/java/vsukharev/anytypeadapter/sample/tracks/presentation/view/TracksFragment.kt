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
import vsukharev.anytypeadapter.sample.common.presentation.delegate.PaginationAdapterItem
import vsukharev.anytypeadapter.sample.common.presentation.delegate.PaginationDelegate
import vsukharev.anytypeadapter.sample.common.presentation.delegate.PartiallyColoredHeaderDelegate
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator.*
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.RecyclerViewScrollListener
import vsukharev.anytypeadapter.sample.tracks.presentation.TracksPresenter
import vsukharev.anytypeadapter.sample.tracks.presentation.model.TracksListItem
import vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter.EmptyTracksListDelegate
import vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter.TracksDelegate
import vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter.TracksErrorDelegate
import javax.inject.Inject

/**
 * Fragment showing the list of tracks
 */
class TracksFragment : BaseFragment(), TracksView {

    private val anyTypeAdapter = AnyTypeAdapter()
    private val tracksDelegate = TracksDelegate()
    private val errorDelegate = TracksErrorDelegate { presenter.refresh() }
    private val emptyListDelegate = EmptyTracksListDelegate()
    private val headerDelegate = PartiallyColoredHeaderDelegate(
        android.R.color.white,
        android.R.color.white
    )
    private val paginationDelegate = PaginationDelegate { presenter.loadMore() }
    private val paginationItem = PaginationAdapterItem(false)

    private val scrollListener: RecyclerViewScrollListener by lazy {
        RecyclerViewScrollListener { presenter.loadMore() }
    }

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
            addOnScrollListener(scrollListener)
        }
        tracks_swr.setOnRefreshListener { presenter.refresh() }
    }

    override fun onDestroy() {
        if (requireActivity().isFinishing) {
            Injector.destroyTracksComponent()
        }
        super.onDestroy()
    }

    override fun showProgress() {
        tracks_pb.isVisible = true
    }

    override fun hideProgress() {
        tracks_pb.isVisible = false
    }

    override fun hideRefreshProgress() {
        tracks_swr.isRefreshing = false
    }

    override fun disableRefreshProgress() {
        tracks_swr.isEnabled = false
    }

    override fun enableRefreshProgress() {
        tracks_swr.isEnabled = true
    }

    override fun showEmptyError(error: Throwable) {
        Collection.Builder()
            .add(errorDelegate)
            .build()
            .let { anyTypeAdapter.setCollection(it) }
    }

    override fun hideEmptyError() {
        anyTypeAdapter.setCollection(Collection.EMPTY)
    }

    override fun showEmptyView() {
        Collection.Builder()
            .add(emptyListDelegate)
            .build()
            .let { anyTypeAdapter.setCollection(it) }
    }

    override fun hideEmptyView() {
        anyTypeAdapter.setCollection(Collection.EMPTY)
    }

    override fun showData(
        data: List<TracksListItem>,
        allDataLoaded: Boolean,
        paginationState: PaginationState?
    ) {
        scrollListener.apply {
            isLoading = false
            hasMore = !allDataLoaded && paginationState != PaginationState.ERROR
        }
        Collection.Builder()
            .apply {
                data.forEach {
                    when (it) {
                        is TracksListItem.Header -> add(it.value, headerDelegate)
                        is TracksListItem.TrackUi -> add(it.track, tracksDelegate)
                    }
                }
                addIf(
                    paginationItem.copy(isError = paginationState == PaginationState.ERROR),
                    paginationDelegate
                ) { paginationState != null }
            }
            .build()
            .let { anyTypeAdapter.setCollection(it) }
    }

    override fun hideData() {
        anyTypeAdapter.setCollection(Collection.EMPTY)
    }

    override fun showPaginationError(error: Throwable) {
        // no implementation
    }

    companion object {
        fun newInstance(): TracksFragment = TracksFragment()
    }
}