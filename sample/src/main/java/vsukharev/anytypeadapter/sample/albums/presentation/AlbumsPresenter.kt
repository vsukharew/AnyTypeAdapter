package vsukharev.anytypeadapter.sample.albums.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.albums.domain.interactor.AlbumsInteractor
import vsukharev.anytypeadapter.sample.albums.domain.interactor.EditorsChoiceInteractor
import vsukharev.anytypeadapter.sample.albums.presentation.model.HomePageUi
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsFragment
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsView
import vsukharev.anytypeadapter.sample.common.di.common.PerScreen
import vsukharev.anytypeadapter.sample.common.presentation.presenter.BasePresenter
import javax.inject.Inject

/**
 * [AlbumsFragment]'s presenter
 */
@InjectViewState
@PerScreen
class AlbumsPresenter @Inject constructor(
    private val albumsInteractor: AlbumsInteractor,
    private val editorsChoiceInteractor: EditorsChoiceInteractor
) : BasePresenter<AlbumsView>() {

    override fun onFirstViewAttach() {
        getAlbums()
    }

    private fun getAlbums() {
        startJobOnMain {
            while (true) {
                val albums = withContext(Dispatchers.Default) {
                    albumsInteractor.getAlbumsBasedOnPreferences()
                }.dataOrNull ?: emptyList()
                val editorsChoice = withContext(Dispatchers.Default) {
                    editorsChoiceInteractor.getEditorsChoice()
                }.dataOrNull ?: emptyList()
                val homePageUi = HomePageUi(albums, editorsChoice)
                viewState.showData(homePageUi)
                delay(5000)
            }
        }
    }
}