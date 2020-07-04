package vsukharev.anytypeadapter.sample.albums.presentation

import kotlinx.coroutines.delay
import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.albums.domain.interactor.AlbumsInteractor
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsView
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.AlbumAdapterItem
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.AlbumsSectionAdapterItem
import vsukharev.anytypeadapter.sample.common.di.common.PerScreen
import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.common.presentation.presenter.BasePresenter
import javax.inject.Inject
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsFragment

/**
 * Presenter [AlbumsFragment]
 */
@InjectViewState
@PerScreen
class AlbumsPresenter @Inject constructor(private val albumsInteractor: AlbumsInteractor) : BasePresenter<AlbumsView>() {

    override fun onFirstViewAttach() {
        getAlbums()
    }

    private fun getAlbums() {
        startJobOnMain {
            while (true) {
                when (val result = albumsInteractor.getAlbumsBasedOnPreferences()) {
                    is Result.Success -> {
                        viewState.showItems(AlbumsSectionAdapterItem(result.data.map {
                            AlbumAdapterItem(
                                it
                            )
                        }))
                    }
                }
                delay(5000)
            }
        }
    }
}