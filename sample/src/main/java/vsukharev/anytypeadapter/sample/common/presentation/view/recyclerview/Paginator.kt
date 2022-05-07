package vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import moxy.viewstate.strategy.alias.AddToEndSingle
import vsukharev.anytypeadapter.sample.common.extension.*
import vsukharev.anytypeadapter.sample.common.presentation.model.Page
import vsukharev.anytypeadapter.sample.common.presentation.model.emptyPage

/**
 * Class responsible for loading and showing data by pages
 * @param T - raw data that is gotten from some data source
 * @param R - common type for all the data that is displayed in the UI e.g. data items themselves, progress bar, ad blocks
 */
class Paginator<T, R> : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    private var previousState: State<T, R>? = null
    private var currentState: State<T, R> =
        State.EmptyProgress(START_PAGE, START_PAGE, emptyPage(), emptyPage())

    var render: (State<T, R>) -> Unit = {}
        set(value) {
            field = value
            value.invoke(currentState)
        }
    var sideEffects = Channel<SideEffect>()

    fun proceed(action: Action<T, R>) {
        val newState = reduce(action, currentState) {
            launch { sideEffects.send(it) }
        }
        if (newState != currentState) {
            previousState = currentState
            currentState = newState
            render.invoke(currentState)
        }
    }

    fun cancel() = sideEffects.cancel()

    private fun reduce(
        action: Action<T, R>,
        state: State<T, R>,
        sideEffectListener: (SideEffect) -> Unit
    ): State<T, R> = when (action) {
        Action.Refresh -> reduceRefreshAction(sideEffectListener, state)
        Action.LoadMore -> reduceLoadMoreAction(sideEffectListener, state)
        is Action.NewPage<T, R> -> reduceNewPageAction(action, state)
        is Action.PageLoadingError ->
            reducePageLoadingErrorAction(sideEffectListener, action, state)
        is Action.TextChanged -> reduceTextChangedAction(sideEffectListener, action, state)
    }

    private fun reduceRefreshAction(
        sideEffectListener: (SideEffect) -> Unit,
        state: State<T, R>
    ): State<T, R> {
        sideEffectListener.invoke(SideEffect.LoadPage(START_PAGE, state.searchString))
        return state.run {
            when (this) {
                is State.Empty,
                is State.EmptyError -> toEmptyProgress(START_PAGE, START_PAGE)
                is State.Data,
                is State.NewPageLoading,
                is State.PaginationError,
                is State.AllData -> toRefreshing(START_PAGE, START_PAGE)
                else -> state
            }
        }
    }

    private fun reduceLoadMoreAction(
        sideEffectListener: (SideEffect) -> Unit,
        state: State<T, R>
    ): State<T, R> {
        return state.run {
            when (this) {
                is State.Data,
                is State.PaginationError -> {
                    val pageNumber = when (searchString) {
                        null -> currentPage + 1
                        else -> currentSearchPage + 1
                    }
                    sideEffectListener.invoke(
                        SideEffect.LoadPage(pageNumber, searchString)
                    )
                    toNewPageLoading()
                }
                else -> state
            }
        }
    }

    private fun reduceNewPageAction(
        action: Action.NewPage<T, R>,
        state: State<T, R>
    ): State<T, R> {
        return state.run {
            when (this) {
                is State.Refreshing -> reduceNewPageRefreshing(action, this)
                is State.EmptyProgress -> reduceNewPageEmptyProgress(action, this)
                is State.NewPageLoading -> reduceNewPageNewPageLoading(action, this)
                else -> state
            }
        }
    }

    private fun reduceTextChangedAction(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.TextChanged,
        state: State<T, R>
    ): State<T, R> {
        return state.run {
            when (this) {
                is State.Data -> reduceTextChangedData(sideEffectListener, action, this)
                is State.AllData -> reduceTextChangedAllData(sideEffectListener, action, this)
                is State.EmptyError -> reduceTextChangedEmptyError(sideEffectListener, action, this)
                is State.Empty -> reduceTextChangedEmpty(sideEffectListener, action, this)
                is State.EmptyProgress -> reduceTextChangedEmptyProgress(sideEffectListener, action, this)
                else -> state
            }
        }
    }

    private fun reduceTextChangedEmptyProgress(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.TextChanged,
        state: State.EmptyProgress<T, R>
    ): State<T, R> {
        return state.run {
            sideEffectListener.invoke(SideEffect.LoadPage(0, action.text))
            toEmptyProgress(searchString = action.text)
        }
    }

    private fun reduceNewPageRefreshing(
        action: Action.NewPage<T, R>,
        state: State.Refreshing<T, R>
    ): State<T, R> {
        return reduceNewPageEmptyProgress(action, state.toEmptyProgress())
    }

    private fun reduceNewPageEmptyProgress(
        action: Action.NewPage<T, R>,
        state: State.EmptyProgress<T, R>
    ): State<T, R> {
        return state.run {
            when (action.searchString) {
                null -> {
                    when {
                        action.data.rawData.isEmpty() -> {
                            toEmpty()
                        }
                        else -> {
                            toData(currentPage = action.pageNumber, data = action.data)
                        }
                    }
                }
                else -> {
                    when {
                        action.data.rawData.isEmpty() && action.pageNumber == 0 -> {
                            toEmpty()
                        }
                        action.data.rawData.size < PAGE_SIZE -> {
                            toAllData(searchResults = action.data)
                        }
                        else -> {
                            toData(searchResults = action.data)
                        }
                    }
                }
            }
        }
    }

    private fun reduceNewPageNewPageLoading(
        action: Action.NewPage<T, R>,
        state: State.NewPageLoading<T, R>
    ): State<T, R> {
        return state.run {
            val currentPage = action.pageNumber + 1
            when (searchString) {
                null -> {
                    val newRawData = data.rawData + action.data.rawData
                    val newUiData = data.uiData + action.data.uiData
                    val newData = data.copy(newRawData, newUiData)
                    if (action.data.rawData.size < PAGE_SIZE) {
                        toAllData(currentPage = currentPage, data = newData)
                    } else {
                        toData(currentPage = currentPage, data = newData)
                    }
                }
                else -> {
                    val newRawData = searchResults.rawData + action.data.rawData
                    val newUiData = searchResults.uiData + action.data.uiData
                    val newData = searchResults.copy(newRawData, newUiData)
                    if (action.data.rawData.size < PAGE_SIZE) {
                        toAllData(currentSearchPage = currentPage, searchResults = newData)
                    } else {
                        toData(currentSearchPage = currentPage, searchResults = newData)
                    }
                }
            }
        }
    }

    private fun reducePageLoadingErrorAction(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.PageLoadingError,
        state: State<T, R>
    ): State<T, R> {
        return state.run {
            when (this) {
                is State.EmptyProgress,
                is State.Data,
                is State.AllData,
                is State.Refreshing -> {
                    sideEffectListener.invoke(SideEffect.ErrorEvent(action.error))
                    toEmptyError(action.error)
                }
                is State.NewPageLoading -> {
                    sideEffectListener.invoke(SideEffect.ErrorEvent(action.error))
                    toPaginationError()
                }
                else -> state
            }
        }
    }

    private fun reduceTextChangedEmptyError(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.TextChanged,
        state: State.EmptyError<T, R>
    ): State<T, R> {
        return reduceTextChangedEmpty(sideEffectListener, action, state.toEmpty())
    }

    private fun reduceTextChangedEmpty(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.TextChanged,
        state: State.Empty<T, R>
    ): State<T, R> {
        return state.run {
            if (action.text.isEmpty()) {
                when {
                    data.rawData.isEmpty() -> toEmpty(
                        currentPage = START_PAGE,
                        currentSearchPage = START_PAGE,
                        searchResults = emptyPage(),
                        searchString = null
                    )
                    else -> {
                        toData(searchString = null)
                    }
                }
            } else {
                sideEffectListener.invoke(
                    SideEffect.LoadPage(
                        currentSearchPage,
                        action.text
                    )
                )
                toEmptyProgress(searchString = action.text)
            }
        }
    }

    private fun reduceTextChangedData(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.TextChanged,
        state: State.Data<T, R>
    ): State<T, R> {
        return state.run {
            when (action.text) {
                String.EMPTY -> {
                    toData(
                        currentSearchPage = START_PAGE,
                        searchResults = emptyPage(),
                        searchString = null
                    )
                }
                else -> {
                    sideEffectListener.invoke(
                        SideEffect.LoadPage(
                            currentSearchPage,
                            action.text
                        )
                    )
                    toEmptyProgress(searchString = action.text)
                }
            }
        }
    }

    private fun reduceTextChangedAllData(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.TextChanged,
        state: State.AllData<T, R>
    ): State<T, R> {
        return state.run {
            when (action.text) {
                String.EMPTY -> {
                    if (data.rawData.size % PAGE_SIZE == 0 && state.currentPage > 0) {
                        toAllData(
                            currentSearchPage = START_PAGE,
                            searchResults = emptyPage(),
                            searchString = null
                        )
                    } else {
                        toData(
                            currentSearchPage = START_PAGE,
                            searchResults = emptyPage(),
                            searchString = null
                        )
                    }
                }
                else -> {
                    sideEffectListener.invoke(
                        SideEffect.LoadPage(
                            currentSearchPage,
                            action.text
                        )
                    )
                    toEmptyProgress(searchString = action.text)
                }
            }
        }
    }

    sealed class State<out T, out R> {
        abstract val currentPage: Int
        abstract val currentSearchPage: Int
        abstract val data: Page<T, R>
        abstract val searchResults: Page<T, R>
        abstract val searchString: String?

        data class Empty<T, R>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: Page<T, R>,
            override val searchResults: Page<T, R>,
            override val searchString: String? = null
        ) : State<T, R>()

        class EmptyProgress<T, R>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: Page<T, R>,
            override val searchResults: Page<T, R>,
            override val searchString: String? = null
        ) : State<T, R>()

        class EmptyError<T, R>(
            val error: Throwable,
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: Page<T, R>,
            override val searchResults: Page<T, R>,
            override val searchString: String? = null
        ) : State<T, R>()

        class PaginationError<T, R>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: Page<T, R>,
            override val searchResults: Page<T, R>,
            override val searchString: String? = null
        ) : State<T, R>()

        class Refreshing<T, R>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: Page<T, R>,
            override val searchResults: Page<T, R>,
            override val searchString: String? = null
        ) : State<T, R>()

        class NewPageLoading<T, R>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: Page<T, R>,
            override val searchResults: Page<T, R>,
            override val searchString: String? = null
        ) : State<T, R>()

        class Data<T, R>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: Page<T, R>,
            override val searchResults: Page<T, R>,
            override val searchString: String? = null,
        ) : State<T, R>()

        class AllData<T, R>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: Page<T, R>,
            override val searchResults: Page<T, R>,
            override val searchString: String? = null
        ) : State<T, R>()
    }

    sealed class Action<out T, out R> {
        object Refresh : Action<Nothing, Nothing>()
        object LoadMore : Action<Nothing, Nothing>()
        data class NewPage<T, R>(
            val pageNumber: Int,
            val data: Page<T, R>,
            val searchString: String? = null
        ) : Action<T, R>()

        data class PageLoadingError(val error: Throwable) : Action<Nothing, Nothing>()
        data class TextChanged(val text: String) : Action<Nothing, Nothing>()
    }

    sealed class SideEffect {
        data class LoadPage(val pageNumber: Int, val searchString: String? = null) : SideEffect()
        data class ErrorEvent(val error: Throwable) : SideEffect()
    }

    @AddToEndSingle
    interface PaginatorView<T, R> {
        fun showProgress()
        fun hideProgress()
        fun hideRefreshProgress()
        fun disableRefreshProgress()
        fun enableRefreshProgress()
        fun showSearchButton()
        fun hideSearchButton()
        fun showEmptyError(error: Throwable)
        fun hideEmptyError()
        fun showEmptyView()
        fun hideEmptyView()
        fun showData(data: List<R>, state: State<T, R>)
        fun hideData()
        fun showPaginationError(error: Throwable)
    }

    companion object {
        private const val START_PAGE = 0
        const val PAGE_SIZE = 20
    }
}