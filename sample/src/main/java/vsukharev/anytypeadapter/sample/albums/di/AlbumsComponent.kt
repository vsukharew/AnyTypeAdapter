package vsukharev.anytypeadapter.sample.albums.di

import dagger.Component
import vsukharev.anytypeadapter.sample.albums.domain.interactor.AlbumsInteractor
import vsukharev.anytypeadapter.sample.albums.domain.interactor.EditorsChoiceInteractor

@Component
@AlbumsScope
interface AlbumsComponent {
    fun albumsInteractor(): AlbumsInteractor
    fun editorsChoiceInteractor(): EditorsChoiceInteractor
}