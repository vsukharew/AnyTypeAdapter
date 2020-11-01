package vsukharev.anytypeadapter.sample.albums.presentation

import kotlinx.coroutines.*
import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.albums.domain.interactor.AlbumsInteractor
import vsukharev.anytypeadapter.sample.albums.domain.interactor.EditorsChoiceInteractor
import vsukharev.anytypeadapter.sample.albums.presentation.model.HomePageUi
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsFragment
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsView
import vsukharev.anytypeadapter.sample.common.di.common.PerScreen
import vsukharev.anytypeadapter.sample.common.errorhandling.Result.Failure
import vsukharev.anytypeadapter.sample.common.presentation.LoadState
import vsukharev.anytypeadapter.sample.common.presentation.presenter.BasePresenter
import javax.inject.Inject

private const val NORMAL_RELOADING_DELAY = 5000L
private const val DEFAULT_RELOADING_DELAY_AFTER_RELEASING = 2000L

/**
 * [AlbumsFragment]'s presenter
 */
@InjectViewState
@PerScreen
class AlbumsPresenter @Inject constructor(
    private val albumsInteractor: AlbumsInteractor,
    private val editorsChoiceInteractor: EditorsChoiceInteractor
) : BasePresenter<AlbumsView>() {
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
                val albumsResult = withContext(Dispatchers.IO) {
                    albumsInteractor.getAlbumsBasedOnPreferences()
                }
                val editorsChoiceResult = withContext(Dispatchers.IO) {
                    editorsChoiceInteractor.getEditorsChoice()
                }
                viewState.hideProgress()
                when {
                    albumsResult is Failure -> {
                        showError(albumsResult.e)
                        loadState = LoadState.ERROR
                        break@loop
                    }
                    editorsChoiceResult is Failure -> {
                        showError(editorsChoiceResult.e)
                        loadState = LoadState.ERROR
                        break@loop
                    }
                    else -> {
                        val albums = albumsResult.dataOrNull ?: emptyList()
                        val editorsChoice = editorsChoiceResult.dataOrNull ?: emptyList()
                        val homePageUi = HomePageUi(albums, editorsChoice)
                        loadState = LoadState.NONE
                        viewState.showData(homePageUi)
                        delay(NORMAL_RELOADING_DELAY)
                    }
                }
            }
        }
    }
}