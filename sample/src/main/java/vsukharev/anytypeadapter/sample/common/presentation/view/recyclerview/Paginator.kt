package vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import moxy.viewstate.strategy.alias.AddToEndSingle
import vsukharev.anytypeadapter.sample.common.extension.*

/**
 * Class responsible for loading and showing data by pages
 */
class Paginator<T> : CoroutineScope by MainScope() {

    private var previousState: State<T>? = null
    private var currentState: State<T> =
        State.EmptyProgress(START_PAGE, START_PAGE, emptyList(), emptyList())

    var render: (State<T>) -> Unit = {}
        set(value) {
            field = value
            value.invoke(currentState)
        }
    var sideEffects = Channel<SideEffect>()

    fun proceed(action: Action<T>) {
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

    private fun <T> reduce(
        action: Action<T>,
        state: State<T>,
        sideEffectListener: (SideEffect) -> Unit
    ): State<T> = when (action) {
        Action.Refresh -> reduceRefreshAction(sideEffectListener, state)
        Action.LoadMore -> reduceLoadMoreAction(sideEffectListener, state)
        is Action.NewPage<T> -> reduceNewPageAction(action, state)
        is Action.PageLoadingError ->
            reducePageLoadingErrorAction(sideEffectListener, action, state)
        is Action.TextChanged -> reduceTextChangedAction(sideEffectListener, action, state)
    }

    private fun <T> reduceRefreshAction(
        sideEffectListener: (SideEffect) -> Unit,
        state: State<T>
    ): State<T> {
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

    private fun <T> reduceLoadMoreAction(
        sideEffectListener: (SideEffect) -> Unit,
        state: State<T>
    ): State<T> {
        return state.run {
            when (this) {
                is State.Data,
                is State.PaginationError -> {
                    sideEffectListener.invoke(
                        SideEffect.LoadPage(currentPage + 1, searchString)
                    )
                    toNewPageLoading()
                }
                else -> state
            }
        }
    }

    private fun <T> reduceNewPageAction(
        action: Action.NewPage<T>,
        state: State<T>
    ): State<T> {
        return state.run {
            when (this) {
                is State.Refreshing -> reduceNewPageRefreshing(action, this)
                is State.EmptyProgress -> reduceNewPageEmptyProgress(action, this)
                is State.NewPageLoading -> reduceNewPageNewPageLoading(action, this)
                else -> state
            }
        }
    }

    private fun <T> reduceTextChangedAction(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.TextChanged,
        state: State<T>
    ): State<T> {
        return state.run {
            when (this) {
                is State.Data -> reduceTextChangedData(sideEffectListener, action, this)
                is State.AllData -> reduceTextChangedAllData(sideEffectListener, action, this)
                is State.EmptyError -> reduceTextChangedEmptyError(sideEffectListener, action, this)
                is State.Empty -> reduceTextChangedEmpty(sideEffectListener, action, this)
                else -> state
            }
        }
    }

    private fun <T> reduceNewPageRefreshing(
        action: Action.NewPage<T>,
        state: State.Refreshing<T>
    ): State<T> {
        return reduceNewPageEmptyProgress(action, state.toEmptyProgress())
    }

    private fun <T> reduceNewPageEmptyProgress(
        action: Action.NewPage<T>,
        state: State.EmptyProgress<T>
    ): State<T> {
        return state.run {
            when (action.searchString) {
                null -> {
                    when {
                        action.data.isEmpty() -> {
                            toEmpty()
                        }
                        else -> {
                            toData(currentPage = action.pageNumber, data = action.data)
                        }
                    }
                }
                else -> {
                    val currentSearchPage = action.pageNumber + 1
                    when {
                        action.data.isEmpty() && action.pageNumber == 0 -> {
                            toEmpty()
                        }
                        action.data.size < PAGE_SIZE -> {
                            toAllData(
                                currentSearchPage = currentSearchPage,
                                searchResults = action.data
                            )
                        }
                        else -> {
                            toData(
                                currentSearchPage = currentSearchPage,
                                searchResults = action.data
                            )
                        }
                    }
                }
            }
        }
    }

    private fun <T> reduceNewPageNewPageLoading(
        action: Action.NewPage<T>,
        state: State.NewPageLoading<T>
    ): State<T> {
        return state.run {
            when (searchString) {
                null -> {
                    val newData = data + action.data
                    if (action.data.size < PAGE_SIZE) {
                        toAllData(data = newData)
                    } else {
                        toData(currentPage = action.pageNumber, data = newData)
                    }
                }
                else -> {
                    val currentSearchPage = action.pageNumber + 1
                    val newData = searchResults + action.data
                    toData(
                        currentSearchPage = currentSearchPage,
                        searchResults = newData
                    )
                }
            }
        }
    }

    private fun <T> reducePageLoadingErrorAction(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.PageLoadingError,
        state: State<T>
    ): State<T> {
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

    private fun <T> reduceTextChangedEmptyError(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.TextChanged,
        state: State.EmptyError<T>
    ): State<T> {
        return reduceTextChangedEmpty(sideEffectListener, action, state.toEmpty())
    }

    private fun <T> reduceTextChangedEmpty(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.TextChanged,
        state: State.Empty<T>
    ): State<T> {
        return state.run {
            if (action.text.isEmpty()) {
                when {
                    data.isEmpty() -> toEmpty(
                        currentPage = START_PAGE,
                        currentSearchPage = START_PAGE,
                        searchResults = emptyList(),
                        searchString = null
                    )
                    else -> {
                        if (previousState is State.Data) {
                            toData(searchString = null)
                        } else {
                            toAllData(searchString = null)
                        }
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

    private fun <T> reduceTextChangedData(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.TextChanged,
        state: State.Data<T>
    ): State<T> {
        return state.run {
            when (action.text) {
                String.EMPTY -> {
                    toData(
                        currentSearchPage = START_PAGE,
                        searchResults = emptyList(),
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

    private fun <T> reduceTextChangedAllData(
        sideEffectListener: (SideEffect) -> Unit,
        action: Action.TextChanged,
        state: State.AllData<T>
    ): State<T> {
        return state.run {
            when (action.text) {
                String.EMPTY -> {
                    toAllData(
                        currentSearchPage = START_PAGE,
                        searchResults = emptyList(),
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

    sealed class State<out T> {
        abstract val currentPage: Int
        abstract val currentSearchPage: Int
        abstract val data: List<T>
        abstract val searchResults: List<T>
        abstract val searchString: String?

        data class Empty<T>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: List<T>,
            override val searchResults: List<T>,
            override val searchString: String? = null
        ) : State<T>()

        class EmptyProgress<T>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: List<T>,
            override val searchResults: List<T>,
            override val searchString: String? = null
        ) : State<T>()

        class EmptyError<T>(
            val error: Throwable,
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: List<T>,
            override val searchResults: List<T>,
            override val searchString: String? = null
        ) : State<T>()

        class PaginationError<T>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: List<T>,
            override val searchResults: List<T>,
            override val searchString: String? = null
        ) : State<T>()

        class Refreshing<T>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: List<T>,
            override val searchResults: List<T>,
            override val searchString: String? = null
        ) : State<T>()

        class NewPageLoading<T>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: List<T>,
            override val searchResults: List<T>,
            override val searchString: String? = null
        ) : State<T>()

        class Data<T>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: List<T>,
            override val searchResults: List<T>,
            override val searchString: String? = null,
        ) : State<T>()

        class AllData<T>(
            override val currentPage: Int,
            override val currentSearchPage: Int,
            override val data: List<T>,
            override val searchResults: List<T>,
            override val searchString: String? = null
        ) : State<T>()
    }

    sealed class Action<out T> {
        object Refresh : Action<Nothing>()
        object LoadMore : Action<Nothing>()
        data class NewPage<T>(
            val pageNumber: Int,
            val data: List<T>,
            val searchString: String? = null
        ) : Action<T>()

        data class PageLoadingError(val error: Throwable) : Action<Nothing>()
        data class TextChanged(val text: String) : Action<Nothing>()
    }

    sealed class SideEffect {
        data class LoadPage(val pageNumber: Int, val searchString: String? = null) : SideEffect()
        data class ErrorEvent(val error: Throwable) : SideEffect()
    }

    @AddToEndSingle
    interface PaginatorView<T> {
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
        fun showData(data: List<T>, state: State<T>)
        fun hideData()
        fun showPaginationError(error: Throwable)
    }

    companion object {
        private const val START_PAGE = 0
        const val PAGE_SIZE = 20
    }
}