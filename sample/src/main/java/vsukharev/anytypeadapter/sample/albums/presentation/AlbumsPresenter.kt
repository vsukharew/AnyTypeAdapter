package vsukharev.anytypeadapter.sample.albums.presentation

import kotlinx.coroutines.*
import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.albums.domain.interactor.AlbumsInteractor
import vsukharev.anytypeadapter.sample.albums.domain.interactor.EditorsChoiceInteractor
import vsukharev.anytypeadapter.sample.albums.presentation.model.HomePageUi
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsFragment
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsView
import vsukharev.anytypeadapter.sample.common.di.common.PerScreen
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
    private var jobAfterItemRelease: Job? = null

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
        jobAfterItemRelease?.cancel()
        jobAfterItemRelease = startJobOnMain {
            delay(RELOADING_DELAY - itemHoldingTime)
            getAlbums()
        }
    }

    private fun getAlbums() {
        mainLoadingJob = startJobOnMain {
            while (true) {
                val albums = withContext(Dispatchers.Default) {
                    albumsInteractor.getAlbumsBasedOnPreferences()
                }.dataOrNull ?: emptyList()
                val editorsChoice = withContext(Dispatchers.Default) {
                    editorsChoiceInteractor.getEditorsChoice()
                }.dataOrNull ?: emptyList()
                val homePageUi = HomePageUi(albums, editorsChoice)
                viewState.showData(homePageUi)
                delay(RELOADING_DELAY)
            }
        }
    }
}