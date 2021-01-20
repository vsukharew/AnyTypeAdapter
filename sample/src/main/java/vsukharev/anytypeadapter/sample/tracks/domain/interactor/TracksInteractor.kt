package vsukharev.anytypeadapter.sample.tracks.domain.interactor

import kotlinx.coroutines.delay
import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.tracks.data.TracksRepository
import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class TracksInteractor @Inject constructor(private val repo: TracksRepository) {

    suspend fun getTracks(offset: Int, count: Int): Result<List<Track>> {
        delay(5000L)
        return if (offset == 0) {
            Result.Success(repo.getTracks(offset, count))
        } else {
            when (Random(System.currentTimeMillis()).nextInt(3) % 3) {
                0 -> Result.Failure(Exception())
                else -> Result.Success(repo.getTracks(offset, count))
            }
        }
    }
}