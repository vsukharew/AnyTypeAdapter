package vsukharev.anytypeadapter.sample.albums.domain.interactor

import vsukharev.anytypeadapter.sample.albums.data.AlbumsRepository
import vsukharev.anytypeadapter.sample.albums.domain.model.Album
import javax.inject.Inject

class AlbumsInteractor @Inject constructor(private val repository: AlbumsRepository) {

    suspend fun getAlbumsBasedOnPreferences(): List<Album> = repository.getAlbumsBasedOnPreferences()
}