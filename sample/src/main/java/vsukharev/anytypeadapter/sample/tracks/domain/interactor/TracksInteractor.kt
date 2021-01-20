package vsukharev.anytypeadapter.sample.tracks.domain.interactor

import kotlinx.coroutines.delay
import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.tracks.data.TracksRepository
import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TracksInteractor @Inject constructor(private val repo: TracksRepository) {

    suspend fun getTracks(offset: Int, count: Int): Result<List<Track>> {
        delay(5000L)
        return try {
            Result.Success(repo.getTracks(offset, count))
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}