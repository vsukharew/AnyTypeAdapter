package vsukharev.anytypeadapter.sample.tracks.presentation.view

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context.SEARCH_SERVICE
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import vsukharev.anytypeadapter.adapter.AnyTypeAdapter
import vsukharev.anytypeadapter.adapter.AnyTypeCollection
import vsukharev.anytypeadapter.sample.Injector
import vsukharev.anytypeadapter.sample.MainActivity
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.common.extension.EMPTY
import vsukharev.anytypeadapter.sample.common.extension.dropFirst
import vsukharev.anytypeadapter.sample.common.lifecycle.fragmentViewBinding
import vsukharev.anytypeadapter.sample.common.presentation.BaseFragment
import vsukharev.anytypeadapter.sample.common.presentation.delegate.PaginationAdapterItem
import vsukharev.anytypeadapter.sample.common.presentation.delegate.PaginationDelegate
import vsukharev.anytypeadapter.sample.common.presentation.delegate.PartiallyColoredHeaderDelegate
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator.*
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.RecyclerViewScrollListener
import vsukharev.anytypeadapter.sample.databinding.FragmentTracksBinding
import vsukharev.anytypeadapter.sample.tracks.presentation.TracksPresenter
import vsukharev.anytypeadapter.sample.tracks.presentation.model.TracksListItem
import vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter.EmptyTracksListDelegate
import vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter.TracksDelegate
import vsukharev.anytypeadapter.sample.tracks.presentation.view.adapter.TracksErrorDelegate
import javax.inject.Inject

/**
 * Fragment showing the list of tracks
 */
@ExperimentalCoroutinesApi
@FlowPreview
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

    private val searchQueryFlow = MutableStateFlow(String.EMPTY)

    override val binding:
            FragmentTracksBinding by fragmentViewBinding(FragmentTracksBinding::inflate)

    @Inject
    @InjectPresenter
    lateinit var presenter: TracksPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.buildTracksComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tracksRv.apply {
                adapter = anyTypeAdapter
                addOnScrollListener(scrollListener)
            }
            tracksToolbar.apply {
                inflateMenu(R.menu.menu_tracks)
                val componentName = ComponentName(context, MainActivity::class.java)
                val searchView = menu.findItem(R.id.tracks_search).actionView as SearchView
                val searchableInfo = (context.getSystemService(SEARCH_SERVICE) as SearchManager)
                    .getSearchableInfo(componentName)
                searchView.setSearchableInfo(searchableInfo)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String): Boolean {
                        searchQueryFlow.value = newText
                        return true
                    }

                    override fun onQueryTextSubmit(query: String): Boolean {
                        searchQueryFlow.value = query
                        return true
                    }
                })
            }
            tracksSwr.setOnRefreshListener { presenter.refresh() }
        }
        lifecycleScope.launch {
            searchQueryFlow
                .dropFirst()
                .debounce(500L)
                .collect { presenter.search(it) }
        }
    }

    override fun onDestroy() {
        if (requireActivity().isFinishing) {
            Injector.destroyTracksComponent()
        }
        super.onDestroy()
    }

    override fun showProgress() {
        binding.tracksPb.isVisible = true
    }

    override fun hideProgress() {
        binding.tracksPb.isVisible = false
    }

    override fun hideRefreshProgress() {
        binding.tracksSwr.isRefreshing = false
    }

    override fun disableRefreshProgress() {
        binding.tracksSwr.isEnabled = false
    }

    override fun enableRefreshProgress() {
        binding.tracksSwr.isEnabled = true
    }

    override fun hideSearchButton() {
        binding.tracksToolbar.menu.findItem(R.id.tracks_search).isVisible = false
    }

    override fun showSearchButton() {
        binding.tracksToolbar.menu.findItem(R.id.tracks_search).isVisible = true
    }

    override fun showEmptyError(error: Throwable) {
        AnyTypeCollection.Builder()
            .add(errorDelegate)
            .build()
            .let { anyTypeAdapter.setCollection(it) }
    }

    override fun hideEmptyError() {
        anyTypeAdapter.setCollection(AnyTypeCollection.EMPTY)
    }

    override fun showEmptyView() {
        AnyTypeCollection.Builder()
            .add(emptyListDelegate)
            .build()
            .let { anyTypeAdapter.setCollection(it) }
    }

    override fun hideEmptyView() {
        anyTypeAdapter.setCollection(AnyTypeCollection.EMPTY)
    }

    override fun showData(
        data: List<TracksListItem>,
        state: State<TracksListItem>
    ) {
        val hasMore = state is State.Data || state is State.PaginationError || state is State.NewPageLoading
        scrollListener.apply {
            isLoading = false
            this.hasMore = hasMore
        }
        AnyTypeCollection.Builder()
            .apply {
                data.forEach {
                    when (it) {
                        is TracksListItem.Header -> add(it.value, headerDelegate)
                        is TracksListItem.TrackUi -> add(it.track, tracksDelegate)
                    }
                }
                addIf(
                    paginationItem.copy(isError = state is State.PaginationError),
                    paginationDelegate
                ) { hasMore }
            }
            .build()
            .let { anyTypeAdapter.setCollection(it) }
    }

    override fun hideData() {
        anyTypeAdapter.setCollection(AnyTypeCollection.EMPTY)
    }

    override fun showPaginationError(error: Throwable) {
        // no implementation
    }

    companion object {
        fun newInstance(): TracksFragment = TracksFragment()
    }
}