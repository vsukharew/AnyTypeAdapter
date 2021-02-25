package vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview

import kotlinx.coroutines.*
import moxy.viewstate.strategy.alias.AddToEndSingle
import vsukharev.anytypeadapter.sample.common.errorhandling.Result

/**
 * Class responsible for loading and showing data by pages
 */
class Paginator<T>(
    private val requestFactory: suspend (Int, String?) -> Result<List<T>>,
    private val view: PaginatorView<T>
) : CoroutineScope by MainScope() {

    private var getPageJob: Job? = null
    private var state: State<T> = Empty(emptyList(), emptyList())
    private var nextPage: Int = START_PAGE
    private var nextSearchPage: Int = START_PAGE

    fun refresh() = state.onRefresh()
    fun cancelLoad() = state.onCancel()
    fun loadMore() = state.loadMore()
    fun search(searchString: String) = state.onTextChanged(searchString)

    private fun loadPage(page: Int, searchString: String? = null) {
        getPageJob?.cancel()
        getPageJob = launch {
            when (val result = requestFactory.invoke(page, searchString)) {
                is Result.Success -> state.onPageLoad(result.data, searchString)
                is Result.Failure -> state.onError(result.e)
            }
        }
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
        fun showData(
            data: List<T>,
            allDataLoaded: Boolean = false,
            paginationState: PaginationState? = null
        )
        fun hideData()
        fun showPaginationError(error: Throwable)
    }

    interface State<T> {
        fun onRefresh() {}
        fun loadMore() {}
        fun onCancel() {}
        fun onTextChanged(text: String) {}
        fun onPageLoad(dataPage: List<T>, searchString: String? = null) {}
        fun onError(error: Throwable) {}
    }

    enum class PaginationState {
        LOADING,
        ERROR
    }

    open inner class CancellableState<T>(
        protected val data: List<T>
    ) : State<T> {
        override fun onCancel() {
            state = Released()
            cancel()
        }
    }

    open inner class SearchableState(
        data: List<T>,
        protected val searchResults: List<T>,
        protected val searchString: String? = null
    ) : CancellableState<T>(data) {
        override fun onTextChanged(text: String) {
            if (text.isNotEmpty()) {
                state = EmptyProgress(data, searchResults)
                with(view) {
                    hideData()
                    showProgress()
                }
                loadPage(START_PAGE, text)
            } else {
                state = Data(data, emptyList())
                with(view) {
                    showData(data)
                    hideProgress()
                }
            }
        }
    }

    inner class Empty(
        data: List<T>,
        searchResults: List<T>
    ) : SearchableState(data, searchResults) {
        override fun onRefresh() {
            state = EmptyProgress(data, searchResults)
            view.hideSearchButton()
            loadPage(START_PAGE, searchString)
        }
    }

    inner class EmptyProgress(
        data: List<T>,
        searchResults: List<T>
    ) : SearchableState(data, searchResults) {
        override fun onPageLoad(dataPage: List<T>, searchString: String?) {
            if (dataPage.isNotEmpty()) {
                state = when (searchString) {
                    null -> Data(dataPage, searchResults).also { nextPage++ }
                    else -> Data(data, dataPage, searchString).also { nextSearchPage++ }
                }
                with(view) {
                    hideProgress()
                    hideRefreshProgress()
                    enableRefreshProgress()
                    showSearchButton()
                    showData(dataPage)
                }
            } else {
                state = Empty(data, searchResults)
                with(view) {
                    hideProgress()
                    hideRefreshProgress()
                    enableRefreshProgress()
                    showEmptyView()
                }
            }
        }

        override fun onError(error: Throwable) {
            state = EmptyError(data, searchResults, searchString)
            with(view) {
                hideProgress()
                disableRefreshProgress()
                hideSearchButton()
                showEmptyError(error)
            }
        }
    }

    inner class EmptyError(
        data: List<T>,
        searchResults: List<T>,
        searchString: String? = null
    ) : SearchableState(data, searchResults, searchString) {
        override fun onRefresh() {
            state = EmptyProgress(data, searchResults)
            with(view) {
                hideEmptyError()
                showProgress()
            }
            loadPage(nextPage)
        }
    }

    inner class PaginationError(
        data: List<T>,
        searchResults: List<T>,
        searchString: String? = null
    ) : SearchableState(data, searchResults, searchString) {
        override fun loadMore() {
            state = NewPageLoading(data, searchResults, searchString)
            view.showData(data, paginationState = PaginationState.LOADING)
            loadPage(nextPage)
        }
    }

    inner class Refreshing(
        data: List<T>,
        searchResults: List<T>,
        searchString: String? = null
    ) : SearchableState(data, searchResults, searchString) {
        override fun onPageLoad(dataPage: List<T>, searchString: String?) {
            if (dataPage.isNotEmpty()) {
                state = when (searchString) {
                    null -> Data(dataPage, searchResults).also { nextPage = 1 }
                    else -> Data(data, dataPage, searchString).also { nextSearchPage = 1 }
                }
                with(view) {
                    hideRefreshProgress()
                    showSearchButton()
                    view.showData(dataPage)
                }
            } else {
                state = Empty(data, searchResults)
                with(view) {
                    hideData()
                    hideRefreshProgress()
                    showEmptyView()
                }
            }
        }

        override fun onError(error: Throwable) {
            state = EmptyError(data, searchResults, searchString)
            nextPage = 0
            nextSearchPage = 0
            with(view) {
                hideRefreshProgress()
                hideSearchButton()
                disableRefreshProgress()
                showEmptyError(error)
            }
        }
    }

    inner class NewPageLoading(
        data: List<T>,
        searchResults: List<T>,
        searchString: String? = null
    ) : SearchableState(data, searchResults, searchString) {

        override fun onPageLoad(dataPage: List<T>, searchString: String?) {
            val aggregatedData = when (searchString) {
                null -> data + dataPage
                else -> searchResults + dataPage
            }
            if (dataPage.isNotEmpty()) {
                state = when (searchString) {
                    null -> Data(aggregatedData, searchResults).also { nextPage++ }
                    else -> Data(data, aggregatedData).also { nextSearchPage++ }
                }
                with(view) {
                    showData(aggregatedData)
                }
            } else {
                state = AllData(data, searchResults, searchString)
                view.showData(aggregatedData, allDataLoaded = true)
            }
        }

        override fun onRefresh() {
            state = Refreshing(data, searchResults, searchString)
            loadPage(START_PAGE)
        }

        override fun onError(error: Throwable) {
            state = PaginationError(data, searchResults, searchString)
            view.showData(data, paginationState = PaginationState.ERROR)
        }
    }

    inner class Data(
        data: List<T>,
        searchResults: List<T>,
        searchString: String? = null
    ) : SearchableState(data, searchResults, searchString) {
        override fun onRefresh() {
            state = Refreshing(data, searchResults, searchString)
            loadPage(START_PAGE, searchString)
        }

        override fun loadMore() {
            state = NewPageLoading(data, searchResults, searchString)
            view.showData(
                when(searchString) {
                    null -> data
                    else -> searchResults
                },
                paginationState = PaginationState.LOADING
            )
            val page = when {
                searchString != null -> nextSearchPage
                else -> nextPage
            }
            loadPage(page, searchString)
        }
    }

    inner class AllData(
        data: List<T>,
        searchResults: List<T>,
        searchString: String? = null
    ) : SearchableState(data, searchResults, searchString) {
        override fun onRefresh() {
            super.onRefresh()
            state = Refreshing(data, searchResults, searchString)
            loadPage(START_PAGE, searchString)
        }
    }

    inner class Released : CancellableState<T>(emptyList())

    private companion object {
        private const val START_PAGE = 0
    }
}