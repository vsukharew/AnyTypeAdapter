package vsukharev.anytypeadapter.sample.feed.presentation

import kotlinx.coroutines.*
import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.R
import vsukharev.anytypeadapter.sample.feed.domain.interactor.FeedInteractor
import vsukharev.anytypeadapter.sample.feed.presentation.model.FeedUi
import vsukharev.anytypeadapter.sample.feed.presentation.view.FeedFragment
import vsukharev.anytypeadapter.sample.feed.presentation.view.FeedView
import vsukharev.anytypeadapter.sample.common.di.common.PerScreen
import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.common.errorhandling.Result.Failure
import vsukharev.anytypeadapter.sample.common.presentation.LoadState
import vsukharev.anytypeadapter.sample.common.presentation.delegate.IconWithTextAdapterItem
import vsukharev.anytypeadapter.sample.common.presentation.presenter.BasePresenter
import vsukharev.anytypeadapter.sample.feed.data.CHART_MENU_ITEM_ID
import vsukharev.anytypeadapter.sample.feed.data.RELEASES_MENU_ITEM_ID
import vsukharev.anytypeadapter.sample.feed.domain.model.Feed
import javax.inject.Inject

private const val ALBUMS_RELOADING_DELAY = 1000L

/**
 * [FeedFragment]'s presenter
 */
@InjectViewState
@PerScreen
class FeedPresenter @Inject constructor(
    private val feedInteractor: FeedInteractor
) : BasePresenter<FeedView>() {
    private var loadState = LoadState.NONE
    private var getFeedJob: Job? = null

    override fun onFirstViewAttach() {
        loadState = LoadState.LOADING
        getFeed()
    }

    fun reloadData() {
        if (loadState != LoadState.ERROR) return
        viewState.hideError()
        viewState.showProgress()
        getFeed()
    }

    fun getFeed(isStaticInterface: Boolean = false) {
        getFeedJob?.cancel()
        getFeedJob = startJobOnMain {
            do {
                val feedResult =
                    withContext(Dispatchers.IO) { feedInteractor.getFeed(isStaticInterface) }
                viewState.hideProgress()
                when (feedResult) {
                    is Failure -> {
                        showError(feedResult.e)
                        loadState = LoadState.ERROR
                    }
                    is Result.Success -> {
                        val feedUi = feedResult.data.toFeedUi(isStaticInterface)
                        loadState = LoadState.NONE
                        viewState.showData(feedUi)
                    }
                }
                delay(ALBUMS_RELOADING_DELAY)
            } while (isActive && !isStaticInterface)
        }
    }

    private fun Feed.toFeedUi(isStaticInterface: Boolean): FeedUi {
        val iconWithTextItems = menuItems.map {
            IconWithTextAdapterItem(
                id = it.id,
                text = it.name,
                imageResId = when (it.id) {
                    RELEASES_MENU_ITEM_ID -> R.drawable.ic_fresh_release
                    CHART_MENU_ITEM_ID -> R.drawable.ic_chart
                    else -> R.drawable.ic_mic
                }
            )
        }
        return FeedUi(
            isStaticInterface = isStaticInterface,
            albums = albums,
            menuItems = iconWithTextItems,
            activities = activities,
            editorsChoice = editorsChoice
        )
    }
}