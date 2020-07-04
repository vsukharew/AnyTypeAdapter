package vsukharev.anytypeadapter.sample.albums.domain.interactor

import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.albums.data.AlbumsRepository
import vsukharev.anytypeadapter.sample.albums.di.AlbumsScope
import vsukharev.anytypeadapter.sample.albums.domain.model.Album
import javax.inject.Inject

/**
 * Interactor of the albums feature
 */
@AlbumsScope
class AlbumsInteractor @Inject constructor(private val repository: AlbumsRepository) {

    suspend fun getAlbumsBasedOnPreferences(): Result<List<Album>> {
        return try {
            Result.Success(repository.getAlbumsBasedOnPreferences())
        }
        catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}