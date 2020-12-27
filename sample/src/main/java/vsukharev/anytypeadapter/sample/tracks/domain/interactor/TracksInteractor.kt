package vsukharev.anytypeadapter.sample.tracks.domain.interactor

import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.tracks.data.TracksRepository
import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TracksInteractor @Inject constructor(private val repo: TracksRepository) {

    fun getTracks(): Result<List<Track>> {
        return Result.Success(repo.getTracks())
    }
}