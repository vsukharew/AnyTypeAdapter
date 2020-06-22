package vsukharev.anytypeadapter.sample.albums.di

import dagger.Component
import vsukharev.anytypeadapter.sample.albums.domain.interactor.AlbumsInteractor

@Component()
@AlbumsScope
interface AlbumsComponent {
    fun albumsInteractor(): AlbumsInteractor
}