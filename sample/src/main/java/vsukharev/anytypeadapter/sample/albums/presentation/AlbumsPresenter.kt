package vsukharev.anytypeadapter.sample.albums.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.albums.domain.interactor.AlbumsInteractor
import vsukharev.anytypeadapter.sample.albums.domain.interactor.EditorsChoiceInteractor
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsFragment
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsView
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.AlbumAdapterItem
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.AlbumsSectionAdapterItem
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.editorschoice.EditorsChoiceAdapterItem
import vsukharev.anytypeadapter.sample.albums.presentation.view.adapter.editorschoice.EditorsChoiceSectionAdapterItem
import vsukharev.anytypeadapter.sample.common.di.common.PerScreen
import vsukharev.anytypeadapter.sample.common.errorhandling.Result
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
                val albums =
                    withContext(Dispatchers.Default) { albumsInteractor.getAlbumsBasedOnPreferences() }
                val editorsChoice =
                    withContext(Dispatchers.Default) { editorsChoiceInteractor.getEditorsChoice() }
                when {
                    albums is Result.Success && editorsChoice is Result.Success -> {
                        viewState.showItems(AlbumsSectionAdapterItem(albums.data.map {
                            AlbumAdapterItem(
                                it
                            )
                        }) to EditorsChoiceSectionAdapterItem(editorsChoice.data.map { EditorsChoiceAdapterItem(it) }))
                    }
                }
                delay(5000)
            }
        }
    }
}