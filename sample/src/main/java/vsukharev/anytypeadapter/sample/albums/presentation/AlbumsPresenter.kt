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
import vsukharev.anytypeadapter.sample.common.presentation.presenter.BasePresenter
import javax.inject.Inject

private const val RELOADING_DELAY = 5000L

/**
 * [AlbumsFragment]'s presenter
 */
@InjectViewState
@PerScreen
class AlbumsPresenter @Inject constructor(
    private val albumsInteractor: AlbumsInteractor,
    private val editorsChoiceInteractor: EditorsChoiceInteractor
) : BasePresenter<AlbumsView>() {
    private var isItemHeld = false
    private var itemHoldingTime = 0L
    private var mainLoadingJob: Job? = null
    private var loadingAfterItemReleaseJob: Job? = null

    override fun onFirstViewAttach() {
        getAlbums()
    }

    fun onAlbumHeld() {
        isItemHeld = true
        itemHoldingTime = System.currentTimeMillis()
    }

    fun onAlbumReleased() {
        itemHoldingTime = System.currentTimeMillis() - itemHoldingTime
        mainLoadingJob?.cancel()
        loadingAfterItemReleaseJob?.cancel()
        loadingAfterItemReleaseJob = startJobOnMain {
            delay(RELOADING_DELAY - itemHoldingTime)
            getAlbums()
        }
    }

    fun reloadData() {
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
                        break@loop
                    }
                    editorsChoiceResult is Failure -> {
                        showError(editorsChoiceResult.e)
                        break@loop
                    }
                    else -> {
                        val albums = albumsResult.dataOrNull ?: emptyList()
                        val editorsChoice = editorsChoiceResult.dataOrNull ?: emptyList()
                        val homePageUi = HomePageUi(albums, editorsChoice)
                        viewState.showData(homePageUi)
                        delay(RELOADING_DELAY)
                    }
                }
            }
        }
    }
}