package vsukharev.anytypeadapter.sample.tracks.data

import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class TracksRepository @Inject constructor(
    private val tracksSource: TracksSource
) {
    fun getTracks(offset: Int, count: Int): List<Track> {
        return with(tracksSource) {
            putTracks(FakeExternalTracksSource.tracks)
            when(offset) {
                0 -> tryImitateItemsLack(offset, count)
                else -> tryImitateError(offset, count)
            }
        }
    }

    private fun tryImitateItemsLack(offset: Int, count: Int): List<Track> {
        return when (Random(System.currentTimeMillis()).nextInt(9)) {
            //One of 10 cases results in an error
            0 -> throw Exception()
            //One of 10 cases results in an empty list
            1 -> emptyList()
            else -> tracksSource.getTracks(offset, count)
        }
    }

    private fun tryImitateError(offset: Int, count: Int): List<Track> {
        return when (Random(System.currentTimeMillis()).nextInt(9)) {
            //Two of 10 cases result in an error
            in 0..2 -> throw Exception()
            else -> tracksSource.getTracks(offset, count)
        }
    }
}