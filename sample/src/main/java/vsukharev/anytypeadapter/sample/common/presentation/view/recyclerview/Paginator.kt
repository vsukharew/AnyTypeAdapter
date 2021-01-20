package vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview

import kotlinx.coroutines.*
import moxy.viewstate.strategy.alias.AddToEndSingle
import vsukharev.anytypeadapter.sample.common.errorhandling.Result

/**
 * Class responsible for loading and showing data by pages
 */
class Paginator<T>(
    private val requestFactory: suspend (Int) -> Result<List<T>>,
    private val view: PaginatorView<T>
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private var getPageJob: Job? = null
    private var state: State<T> = Empty()
    private var nextPage: Int = START_PAGE

    fun refresh() = state.onRefresh()
    fun cancelLoad() = state.onCancel()
    fun loadMore() = state.loadMore()

    @AddToEndSingle
    interface PaginatorView<T> {
        fun showProgress()
        fun hideProgress()
        fun hideRefreshProgress()
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
        fun onPageLoad(dataPage: List<T>) {}
        fun onError(error: Throwable) {}
    }

    enum class PaginationState {
        LOADING,
        ERROR
    }

    open inner class CancellableState<T>(protected val data: List<T>) : State<T> {
        override fun onCancel() {
            state = Released()
            cancel()
        }
    }

    private fun loadPage(page: Int) {
        getPageJob?.cancel()
        getPageJob = launch {
            when (val result = requestFactory.invoke(page)) {
                is Result.Success -> state.onPageLoad(result.data)
                is Result.Failure -> state.onError(result.e)
            }
        }
    }

    inner class Empty : CancellableState<T>(mutableListOf()) {
        override fun onRefresh() {
            state = EmptyProgress()
            loadPage(nextPage)
        }
    }

    inner class EmptyProgress : CancellableState<T>(mutableListOf()) {
        override fun onPageLoad(dataPage: List<T>) {
            if (dataPage.isNotEmpty()) {
                state = Data(dataPage)
                with(view) {
                    hideProgress()
                    hideRefreshProgress()
                    showData(dataPage)
                }
                nextPage++
            } else {
                state = Empty()
                with(view) {
                    hideProgress()
                    hideRefreshProgress()
                    showEmptyView()
                }
            }
        }

        override fun onError(error: Throwable) {
            state = EmptyError()
            with(view) {
                hideProgress()
                showEmptyError(error)
            }
        }
    }

    inner class EmptyError : CancellableState<T>(emptyList()) {
        override fun onRefresh() {
            state = EmptyProgress()
            with(view) {
                hideEmptyError()
                showProgress()
            }
            loadPage(nextPage)
        }
    }

    inner class PaginationError(data: List<T>) : CancellableState<T>(data) {
        override fun loadMore() {
            state = NewPageLoading(data)
            view.showData(data, paginationState = PaginationState.LOADING)
            loadPage(nextPage)
        }
    }

    inner class Refreshing : CancellableState<T>(emptyList()) {
        override fun onPageLoad(dataPage: List<T>) {
            if (dataPage.isNotEmpty()) {
                state = Data(dataPage)
                nextPage = 1
                with(view) {
                    hideRefreshProgress()
                    view.showData(dataPage)
                }
            } else {
                state = Empty()
                with(view) {
                    hideData()
                    hideRefreshProgress()
                    showEmptyView()
                }
            }
        }

        override fun onError(error: Throwable) {
            super.onError(error)
            state = when {
                data.isNotEmpty() -> Data(data)
                else -> EmptyError()
            }
            with(view) {
                hideRefreshProgress()
                showPaginationError(error)
            }
        }
    }

    inner class NewPageLoading(data: List<T>) : CancellableState<T>(data) {
        override fun onPageLoad(dataPage: List<T>) {
            if (dataPage.isNotEmpty()) {
                val aggregatedData = data + dataPage
                state = Data(aggregatedData)
                nextPage++
                with(view) {
                    showData(aggregatedData)
                }
            } else {
                state = AllData(data)
                view.showData(data, allDataLoaded = true)
            }
        }

        override fun onRefresh() {
            state = Refreshing()
            loadPage(START_PAGE)
        }

        override fun onError(error: Throwable) {
            state = PaginationError(data)
            view.showData(data, paginationState = PaginationState.ERROR)
        }
    }

    inner class Data(data: List<T>) : CancellableState<T>(data) {
        override fun onRefresh() {
            state = Refreshing()
            loadPage(START_PAGE)
        }

        override fun loadMore() {
            state = NewPageLoading(data)
            view.showData(data, paginationState = PaginationState.LOADING)
            loadPage(nextPage)
        }
    }

    inner class AllData(data: List<T>) : CancellableState<T>(data) {
        override fun onRefresh() {
            super.onRefresh()
            state = Refreshing()
            loadPage(START_PAGE)
        }
    }

    inner class Released : CancellableState<T>(mutableListOf())

    private companion object {
        private const val START_PAGE = 0
    }
}