package vsukharev.anytypeadapter.sample.albums.domain.interactor

import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.albums.data.AlbumsRepository
import vsukharev.anytypeadapter.sample.albums.di.AlbumsScope
import vsukharev.anytypeadapter.sample.albums.domain.model.Album
import javax.inject.Inject

private const val ALBUMS_PORTION = 5

/**
 * TODO edit comment
 * Interactor of the albums feature
 */
@AlbumsScope
class AlbumsInteractor @Inject constructor(private val repository: AlbumsRepository) {
    private var albums: List<Album>? = null

    suspend fun getAlbumsBasedOnPreferences(): Result<List<Album>> {
        return albums?.let { Result.Success(it.shuffled().take(ALBUMS_PORTION)) } ?: try {
            repository.getAlbumsBasedOnPreferences()
                .also { albums = it }
                .take(ALBUMS_PORTION)
                .let { Result.Success(it) }
        }
        catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}