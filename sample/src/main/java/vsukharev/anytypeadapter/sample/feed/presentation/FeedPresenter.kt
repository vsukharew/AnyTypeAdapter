package vsukharev.anytypeadapter.sample.feed.presentation

import kotlinx.coroutines.*
import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.feed.domain.interactor.FeedInteractor
import vsukharev.anytypeadapter.sample.feed.presentation.model.HomePageUi
import vsukharev.anytypeadapter.sample.feed.presentation.view.FeedFragment
import vsukharev.anytypeadapter.sample.feed.presentation.view.FeedView
import vsukharev.anytypeadapter.sample.common.di.common.PerScreen
import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.common.errorhandling.Result.Failure
import vsukharev.anytypeadapter.sample.common.presentation.LoadState
import vsukharev.anytypeadapter.sample.common.presentation.presenter.BasePresenter
import javax.inject.Inject

private const val NORMAL_RELOADING_DELAY = 5000L
private const val DEFAULT_RELOADING_DELAY_AFTER_RELEASING = 2000L

/**
 * [FeedFragment]'s presenter
 */
@InjectViewState
@PerScreen
class FeedPresenter @Inject constructor(
    private val feedInteractor: FeedInteractor
) : BasePresenter<FeedView>() {
    private var itemHoldingTime = 0L
    private var mainLoadingJob: Job? = null
    private var loadingAfterItemReleaseJob: Job? = null
    private var loadState = LoadState.NONE

    override fun onFirstViewAttach() {
        loadState = LoadState.LOADING
        getAlbums()
    }

    fun onAlbumHeld() {
        itemHoldingTime = System.currentTimeMillis()
        mainLoadingJob?.cancel()
        loadingAfterItemReleaseJob?.cancel()
    }

    fun onAlbumReleased() {
        itemHoldingTime = System.currentTimeMillis() - itemHoldingTime
        loadingAfterItemReleaseJob = startJobOnMain {
            delay(timeMillis = (NORMAL_RELOADING_DELAY - itemHoldingTime)
                .takeIf { it > 0 }
                ?: DEFAULT_RELOADING_DELAY_AFTER_RELEASING
            )
            getAlbums()
        }
    }

    fun reloadData() {
        if (loadState != LoadState.ERROR) return
        viewState.hideError()
        viewState.showProgress()
        getAlbums()
    }

    private fun getAlbums() {
        mainLoadingJob = startJobOnMain {
            loop@ while (true) {
                val feedResult = withContext(Dispatchers.IO) {
                    feedInteractor.getFeed()
                }
                viewState.hideProgress()
                when (feedResult) {
                    is Failure -> {
                        showError(feedResult.e)
                        loadState = LoadState.ERROR
                        break@loop
                    }
                    is Result.Success -> {
                        val homePageUi = HomePageUi(feedResult.data.albums, feedResult.data.editorsChoice)
                        loadState = LoadState.NONE
                        viewState.showData(homePageUi)
                        delay(NORMAL_RELOADING_DELAY)
                    }
                }
            }
        }
    }
}