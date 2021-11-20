package vsukharev.anytypeadapter.sample.tracks.presentation

import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.common.errorhandling.map
import vsukharev.anytypeadapter.sample.common.extension.EMPTY
import vsukharev.anytypeadapter.sample.common.presentation.presenter.BasePresenter
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator.*
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator.Companion.PAGE_SIZE
import vsukharev.anytypeadapter.sample.tracks.domain.interactor.TracksInteractor
import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import vsukharev.anytypeadapter.sample.tracks.presentation.model.TracksListItem
import vsukharev.anytypeadapter.sample.tracks.presentation.view.TracksView
import javax.inject.Inject

@InjectViewState
class TracksPresenter @Inject constructor(
    private val tracksInteractor: TracksInteractor
) : BasePresenter<TracksView>() {

    private var lastHeader = String.EMPTY
    private var lastHeaderDuringSearch = String.EMPTY

    private val paginator = Paginator<TracksListItem>().apply {
        render = {
            with(viewState) {
                when (it) {
                    is State.Empty -> {
                        hideRefreshProgress()
                        hideProgress()
                        enableRefreshProgress()
                        showEmptyView()
                    }
                    is State.EmptyProgress -> {
                        disableRefreshProgress()
                        hideEmptyView()
                        hideEmptyError()
                        hideData()
                        showProgress()
                    }
                    is State.EmptyError -> {
                        hideRefreshProgress()
                        hideEmptyView()
                        hideProgress()
                        hideData()
                        enableRefreshProgress()
                        showSearchButton()
                        showEmptyError(it.error)
                    }
                    is State.Refreshing -> {
                        hideSearchButton()
                    }
                    is State.NewPageLoading -> {
                        showData(it.data, it)
                    }
                    is State.PaginationError -> {
                        showData(it.data, it)
                    }
                    is State.AllData -> {
                        hideRefreshProgress()
                        hideProgress()
                        enableRefreshProgress()
                        val data = with(it) {
                            if (searchResults.isNotEmpty()) {
                                searchResults
                            } else {
                                data
                            }
                        }
                        showData(data, it)
                    }
                    is State.Data -> {
                        enableRefreshProgress()
                        hideRefreshProgress()
                        hideProgress()
                        showSearchButton()
                        val data = with(it) {
                            if (searchResults.isNotEmpty()) {
                                searchResults
                            } else {
                                data
                            }
                        }
                        showData(data, it)
                    }
                }
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        launch {
            paginator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is SideEffect.LoadPage -> {
                        loadNewPage(effect.pageNumber, effect.searchString)
                    }
                    is SideEffect.ErrorEvent -> {

                    }
                }
            }
        }
        paginator.proceed(Action.Refresh)
    }

    override fun onDestroy() {
        super.onDestroy()
        paginator.cancel()
    }

    fun loadMore() {
        paginator.proceed(Action.LoadMore)
    }

    fun refresh() {
        lastHeader = String.EMPTY
        paginator.proceed(Action.Refresh)
    }

    fun search(searchString: String) {
        paginator.proceed(Action.TextChanged(searchString))
    }

    private suspend fun loadNewPage(page: Int, searchString: String? = null) {
        val result = tracksInteractor.getTracks(
            page * PAGE_SIZE,
            PAGE_SIZE,
            searchString
        ).map { tracks -> insertHeadersBetweenTracks(tracks, searchString) }
        val action = when (result) {
            is Result.Success -> Action.NewPage(page, result.data, searchString)
            is Result.Failure -> Action.PageLoadingError(result.e)
        }
        paginator.proceed(action)
    }

    private fun insertHeadersBetweenTracks(
        tracks: List<Track>,
        searchString: String? = null
    ): List<TracksListItem> {
        val groupedTracks = tracks.groupBy { it.name.first().toString() }
        return mutableListOf<TracksListItem>().apply {
            groupedTracks.forEach { (header, tracks) ->
                val lastHeader = if (searchString.isNullOrEmpty()) {
                    lastHeader.also { lastHeader = header }
                } else {
                    lastHeaderDuringSearch.also {
                        lastHeaderDuringSearch = header
                    }
                }
                if (lastHeader != header) {
                    add(TracksListItem.Header(header))
                }
                tracks.forEach { add(TracksListItem.TrackUi(it)) }
            }
        }
    }
}