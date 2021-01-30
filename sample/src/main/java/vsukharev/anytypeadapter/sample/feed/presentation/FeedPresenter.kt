package vsukharev.anytypeadapter.sample.feed.presentation

import kotlinx.coroutines.*
import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.common.errorhandling.Result.Failure
import vsukharev.anytypeadapter.sample.common.presentation.LoadState
import vsukharev.anytypeadapter.sample.common.presentation.LoadState.*
import vsukharev.anytypeadapter.sample.common.presentation.presenter.BasePresenter
import vsukharev.anytypeadapter.sample.feed.domain.interactor.FeedInteractor
import vsukharev.anytypeadapter.sample.feed.domain.model.Feed
import vsukharev.anytypeadapter.sample.feed.presentation.model.FeedUi
import vsukharev.anytypeadapter.sample.feed.presentation.view.FeedFragment
import vsukharev.anytypeadapter.sample.feed.presentation.view.FeedView
import javax.inject.Inject

/**
 * [FeedFragment]'s presenter
 */
@InjectViewState
class FeedPresenter @Inject constructor(
    private val feedInteractor: FeedInteractor
) : BasePresenter<FeedView>() {
    private var loadState: LoadState = NONE
    private var getFeedJob: Job? = null

    override fun onFirstViewAttach() {
        loadState = LOADING
        viewState.showProgress()
        reloadData()
    }

    fun reloadData(isStaticInterface: Boolean = false) {
        getFeedJob?.cancel()
        getFeedJob = launch {
            do {
                val reloadingDelay = if (isStaticInterface) {
                    0L
                } else {
                    when (loadState) {
                        NONE -> 1000L
                        else -> 3000L
                    }
                }
                viewState.apply {
                    when (loadState) {
                        ERROR -> {
                            delay(reloadingDelay)
                            hideError()
                            showProgress()
                        }
                        LOADING -> {
                            showProgress()
                        }
                        else -> {
                        } // do nothing
                    }
                }
                delay(reloadingDelay)
                getFeed(isStaticInterface)
            } while (isActive && !isStaticInterface)
        }
    }

    private suspend fun getFeed(isStaticInterface: Boolean = false) {
        val feedResult =
            withContext(Dispatchers.IO) { feedInteractor.getFeed(isStaticInterface) }
        viewState.hideProgress()
        when (feedResult) {
            is Failure -> {
                showError(feedResult.e)
                loadState = ERROR
            }
            is Result.Success -> {
                loadState = NONE
                viewState.showData(feedResult.data.toFeedUi())
            }
        }
    }

    private fun Feed.toFeedUi(): FeedUi {
        return FeedUi(menuItems, albums, activities, editorsChoice)
    }
}