package vsukharev.anytypeadapter.sample.tracks.presentation

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.common.extension.EMPTY
import vsukharev.anytypeadapter.sample.common.presentation.model.Page
import vsukharev.anytypeadapter.sample.common.presentation.presenter.BasePresenter
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator.*
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator.Companion.PAGE_SIZE
import vsukharev.anytypeadapter.sample.tracks.domain.interactor.TracksInteractor
import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import vsukharev.anytypeadapter.sample.tracks.presentation.model.TracksListItem
import vsukharev.anytypeadapter.sample.tracks.presentation.view.TracksView
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InjectViewState
class TracksPresenter @Inject constructor(
    private val tracksInteractor: TracksInteractor
) : BasePresenter<TracksView>() {
    private var lastHeader = String.EMPTY
    private var lastHeaderDuringSearch = String.EMPTY

    private val paginator = Paginator<Track, TracksListItem>().apply {
        render = { state ->
            with(viewState) {
                when (state) {
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
                        showEmptyError(state.error)
                    }
                    is State.Refreshing -> {
                        hideSearchButton()
                    }
                    is State.NewPageLoading -> {
                        val data = with(state) {
                            searchResults.uiData.ifEmpty { data.uiData }
                        } + TracksListItem.Progress
                        showData(data, state)
                    }
                    is State.PaginationError -> {
                        val data = state.data.uiData + TracksListItem.Retry
                        showData(data, state)
                    }
                    is State.AllData -> {
                        hideRefreshProgress()
                        hideProgress()
                        enableRefreshProgress()
                        val data = with(state) {
                            searchResults.uiData.ifEmpty { data.uiData }
                        }
                        showData(data, state)
                    }
                    is State.Data -> {
                        enableRefreshProgress()
                        hideRefreshProgress()
                        hideProgress()
                        showSearchButton()
                        val data = with(state) {
                            searchResults.uiData.ifEmpty {
                                data.uiData
                            }
                        }
                        showData(data, state)
                    }
                }
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        launch(Dispatchers.IO) {
            paginator.sideEffects.consumeAsFlow().collectLatest { effect ->
                when (effect) {
                    is SideEffect.LoadPage -> {
                        Log.d("Channels", "receive: presenter - effect: $effect")
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
        )
        val action = when (result) {
            is Result.Success -> {
                val uiData = insertHeadersBetweenTracks(result.data, searchString)
                val data = Page(result.data, uiData)
                Action.NewPage(page, data, searchString)
            }
            is Result.Failure -> Action.PageLoadingError(result.e)
        }
        withContext(Dispatchers.Main) {
            paginator.proceed(action)
        }
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